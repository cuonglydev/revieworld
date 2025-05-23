package com.example.RestController;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.tuple.Pair;

@RestController
public class FileDownloadController {

	  @Value("${upload.dir}")
	    private String uploadDir;  

	    private final String prefix = "/RevieworldPhotos/";

	    
	    private Pair<String, String> parsePhotoPath(String photoPath) {
	        if (!photoPath.startsWith(prefix)) {
	            throw new IllegalArgumentException("Đường dẫn ảnh không hợp lệ: " + photoPath);
	        }
	        String relativePath = photoPath.substring(prefix.length()); // vd: "images/abc.png"
	        Path path = Paths.get(relativePath);
	        String subFolder = path.getParent() != null ? path.getParent().toString().replace("\\", "/") : "";
	        String filename = path.getFileName().toString();
	        return Pair.of(subFolder, filename);
	    }

	    @GetMapping("/download")
	    public ResponseEntity<Resource> downloadFile(@RequestParam("photoPath") String photoPath) {
	        try {
	            Pair<String, String> pair = parsePhotoPath(photoPath);
	            String subFolder = pair.getLeft();
	            String filename = pair.getRight();

	            Path filePath = Paths.get(uploadDir, subFolder, filename).normalize();

	            if (!Files.exists(filePath)) {
	                return ResponseEntity.notFound().build();
	            }

	            Resource resource = new UrlResource(filePath.toUri());
	            if (!resource.exists() || !resource.isReadable()) {
	                return ResponseEntity.badRequest().build();
	            }

	            String contentType = Files.probeContentType(filePath);
	            if (contentType == null) {
	                contentType = "application/octet-stream";
	            }

	            return ResponseEntity.ok()
	                    .contentType(MediaType.parseMediaType(contentType))
	                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
	                    .body(resource);

	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.internalServerError().build();
	        }
	    }
	}