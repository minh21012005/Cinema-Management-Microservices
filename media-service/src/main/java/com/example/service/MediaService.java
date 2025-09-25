package com.example.service;

import com.example.domain.entity.MediaFile;
import com.example.repository.MediaFileRepository;
import com.example.util.error.IdInvalidException;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class MediaService {

    private final MediaFileRepository repository;
    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    public MediaService(MediaFileRepository repository, MinioClient minioClient) {
        this.repository = repository;
        this.minioClient = minioClient;
    }

    // Tạo presigned URL
    public String getPresignedUrl(String objectKey, long expireSeconds) throws Exception {
        if (objectKey == null || objectKey.trim().isEmpty()) {
            throw new IdInvalidException("objectKey không được để trống");
        }

        if (expireSeconds <= 0) {
            expireSeconds = 3600; // default 1h
        }

        // Tùy chọn: giới hạn expireSeconds tối đa
        long maxExpire = 24 * 3600; // 1 ngày
        if (expireSeconds > maxExpire) {
            expireSeconds = maxExpire;
        }

        // Tạo presigned URL
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucketName)
                        .object(objectKey)
                        .method(Method.GET)
                        .expiry((int) expireSeconds)
                        .build()
        );
    }


    // Upload file tạm -> chỉ lưu MinIO
    public String uploadTempFile(MultipartFile file) throws Exception {
        validateFile(file);

        String objectKey = "temp/" + UUID.randomUUID() + "-" + cleanFileName(file.getOriginalFilename());

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectKey)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        return objectKey; // không lưu DB
    }

    // Commit file -> move sang thư mục chính + ghi DB
    public String commitFile(String objectKey, String targetType) throws Exception {
        if (objectKey == null || !objectKey.startsWith("temp/")) {
            throw new IdInvalidException("objectKey không hợp lệ hoặc không phải file tạm");
        }

        // Lấy metadata từ MinIO
        var stat = minioClient.statObject(
                io.minio.StatObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectKey)
                        .build()
        );

        String fileName = objectKey.substring(objectKey.lastIndexOf("/") + 1);
        String contentType = stat.contentType();
        long size = stat.size();

        // Tạo key mới trong thư mục chính
        String newKey = targetType + "/" + UUID.randomUUID() + "-" + fileName;

        // Copy từ temp sang thư mục chính
        minioClient.copyObject(
                io.minio.CopyObjectArgs.builder()
                        .bucket(bucketName)
                        .object(newKey)
                        .source(
                                io.minio.CopySource.builder()
                                        .bucket(bucketName)
                                        .object(objectKey)
                                        .build()
                        )
                        .build()
        );

        // Xoá file temp
        minioClient.removeObject(
                io.minio.RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectKey)
                        .build()
        );

        // Ghi DB
        MediaFile media = MediaFile.builder()
                .type(targetType)
                .originalFileName(fileName)
                .objectKey(newKey)
                .contentType(contentType)
                .size(size)
                .build();
        repository.save(media);

        return newKey;
    }

    public void deleteFile(String key) {
        try {
            if (key != null && !key.isEmpty()) {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(bucketName)
                                .object(key)
                                .build()
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi xóa file: " + e.getMessage(), e);
        }
    }

    // Cronjob dọn temp
    @Scheduled(fixedRate = 6 * 60 * 60 * 1000) // mỗi 6h
    public void cleanTempFiles() throws Exception {
        Iterable<Result<Item>> items = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .prefix("temp/")
                        .build()
        );

        for (Result<Item> result : items) {
            Item item = result.get();
            // Nếu file cũ hơn 24h thì xóa
            if (item.lastModified().toInstant().isBefore(Instant.now().minus(24, ChronoUnit.HOURS))) {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(bucketName)
                                .object(item.objectName())
                                .build()
                );
            }
        }

    }

    private void validateFile(MultipartFile file) throws IdInvalidException {
        if (file.isEmpty()) throw new IdInvalidException("File rỗng");
        if (file.getSize() > 5 * 1024 * 1024) throw new IdInvalidException("File quá lớn, tối đa 5MB");

        String contentType = file.getContentType();
        if (!("image/png".equals(contentType) || "image/jpeg".equals(contentType))) {
            throw new IdInvalidException("Chỉ hỗ trợ PNG hoặc JPEG");
        }
    }

    private String cleanFileName(String fileName) {
        if (fileName == null) return "file";
        return fileName.trim()
                .replaceAll("[\\s]+", "_")
                .replaceAll("[^a-zA-Z0-9_\\.-]", "");
    }
}
