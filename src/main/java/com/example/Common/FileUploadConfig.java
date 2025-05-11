package com.example.Common;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.PostConstruct;


@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

	 @Value("${upload.dir}")
	    private String uploadDir;
	 
	 @Value("${static-folder}")
	    private String staticFolder;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler( staticFolder + "**")
                .addResourceLocations("file:" + uploadDir);
    }
}
