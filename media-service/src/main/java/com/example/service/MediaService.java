package com.example.service;

import com.example.domain.entity.MediaFile;
import com.example.repository.MediaFileRepository;
import com.example.util.error.IdInvalidException;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    // Upload file
    public String uploadFile(MultipartFile file, String type) throws Exception {
        if (file.isEmpty()) {
            throw new IdInvalidException("File rỗng");
        }

        long maxSize = 5 * 1024 * 1024; // 5MB
        if (file.getSize() > maxSize) {
            throw new IdInvalidException("File quá lớn, tối đa 5MB");
        }

        String contentType = file.getContentType();
        if (!("image/png".equals(contentType) || "image/jpeg".equals(contentType))) {
            throw new IdInvalidException("Chỉ hỗ trợ PNG hoặc JPEG");
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            originalFileName = "file";
        }
        // Xử lý tên file để objectKey không có khoảng trắng hoặc ký tự đặc biệt
        originalFileName = originalFileName.trim()
                .replaceAll("[\\s]+", "_")
                .replaceAll("[^a-zA-Z0-9_\\.-]", "");

        String objectKey = type + "/" + UUID.randomUUID() + "-" + originalFileName;

        // Upload lên MinIO
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectKey)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(contentType)
                        .build()
        );

        // Lưu metadata vào DB
        MediaFile media = MediaFile.builder()
                .type(type)
                .originalFileName(file.getOriginalFilename())
                .objectKey(objectKey)
                .contentType(contentType)
                .size(file.getSize())
                .build();

        repository.save(media);

        return objectKey;
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

}
