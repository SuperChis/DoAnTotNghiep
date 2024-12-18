package org.example.ezyshop.controller;

import org.example.ezyshop.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    FileStorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Lưu file và lấy URL
            String fileUrl = storageService.storeFile(file);
            return ResponseEntity.ok().body("File uploaded successfully: " + fileUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not upload file: " + e.getMessage());
        }
    }
}
