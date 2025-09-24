package com.example.controller;

import com.example.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    // Upload file
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file,
                                         @RequestParam("type") String type) throws Exception {
        String objectKey = mediaService.uploadFile(file, type);
        return ResponseEntity.ok(objectKey);
    }

    // Get presigned URL
    @GetMapping("/url")
    public ResponseEntity<String> getUrl(@RequestParam("objectKey") String objectKey,
                                         @RequestParam(defaultValue = "3600", name = "expireSeconds")
                                         long expireSeconds) throws Exception {
        String url = mediaService.getPresignedUrl(objectKey, expireSeconds);
        return ResponseEntity.ok(url);
    }
}
