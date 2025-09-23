package com.example.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile file, String type) throws Exception {
        // Chọn folder dựa trên type
        String folder = switch (type.toLowerCase()) {
            case "movie" -> "movies/";
            case "food" -> "foods/";
            case "drink" -> "drinks/";
            case "combo" -> "combos/";
            default -> throw new IllegalArgumentException("Invalid type");
        };

        String fileName = folder + UUID.randomUUID() + "-" + file.getOriginalFilename();

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        // Trả URL trực tiếp
        return "http://127.0.0.1:9000/" + bucket + "/" + fileName;
    }
}
