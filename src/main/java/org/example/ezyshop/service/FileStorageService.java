package org.example.ezyshop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {
    private final String uploadDir;

    // Cấu hình đường dẫn lưu file
    public FileStorageService(@Value("${file.upload-dir:src/main/resources/uploads}") String uploadDir) {
        this.uploadDir = new File(uploadDir).getAbsolutePath();
        File directory = new File(this.uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path filePath = Paths.get(uploadDir, fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/" + fileName; // Trả về URL tương đối
    }
}
