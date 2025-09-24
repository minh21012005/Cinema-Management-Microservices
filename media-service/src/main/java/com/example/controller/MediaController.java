package com.example.controller;

import com.example.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    // Upload tạm thời
    @PostMapping("/upload/temp")
    @PreAuthorize("hasPermission(null, 'FILE_UPLOAD')")
    public ResponseEntity<String> uploadTemp(@RequestParam("file") MultipartFile file) throws Exception {
        String objectKey = mediaService.uploadTempFile(file);
        return ResponseEntity.ok(objectKey);
    }

    // Commit file khi tạo object
    @PostMapping("/commit")
    @PreAuthorize("hasPermission(null, 'FILE_UPLOAD')")
    public ResponseEntity<String> commitFile(@RequestParam("objectKey") String objectKey,
                                             @RequestParam("type") String type) throws Exception {
        String newKey = mediaService.commitFile(objectKey, type);
        return ResponseEntity.ok(newKey);
    }

    // Get presigned URL
    @GetMapping("/url")
    @PreAuthorize("hasPermission(null, 'FILE_VIEW')")
    public ResponseEntity<String> getUrl(@RequestParam("objectKey") String objectKey,
                                         @RequestParam(defaultValue = "3600", name = "expireSeconds")
                                         long expireSeconds) throws Exception {
        String url = mediaService.getPresignedUrl(objectKey, expireSeconds);
        return ResponseEntity.ok(url);
    }
}
