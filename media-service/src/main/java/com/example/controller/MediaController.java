package com.example.controller;

import com.example.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/upload/{type}")
    public ResponseEntity<String> upload(
            @PathVariable("type") String type,
            @RequestParam("file") MultipartFile file) {
        try {
            String url = mediaService.uploadFile(file, type);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
