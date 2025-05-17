package com.example.Common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class ImageUploadController {

    @Value("${upload.dir}")
    private String uploadDir;

    @Value("${static-folder}")
    private String staticFolder;

    @PostMapping("/api/upload-image")
    public @ResponseBody String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        // Tạo tên file duy nhất
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir + fileName);
        Files.createDirectories(uploadPath.getParent());
        Files.write(uploadPath, file.getBytes());
        // Trả về URL ảnh (đường dẫn này phải truy cập được từ trình duyệt)
        return staticFolder + fileName;
    }
} 