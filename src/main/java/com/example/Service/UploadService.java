package com.example.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadService {

    @Value("${upload.dir}")
    private String uploadDir;

    @Value("${static-folder}")
    private String staticFolder;



    
    public String saveFile(MultipartFile file, String subFolder) throws IOException {
	    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	    if (fileName.contains("..")) {
	        throw new IOException("Tên file không hợp lệ: " + fileName);
	    }

	    String fileExtension = fileName.substring(fileName.lastIndexOf("."));
	    String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

	    Path uploadPath = Paths.get(uploadDir + File.separator + subFolder);
	    if (!Files.exists(uploadPath)) {
	        Files.createDirectories(uploadPath);
	    }

	    Path copyLocation = Paths.get(uploadPath + File.separator + uniqueFileName);
	    Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
	    
	    return uniqueFileName;
	}
}
