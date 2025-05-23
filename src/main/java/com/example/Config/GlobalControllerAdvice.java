package com.example.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.Entity.SystemSetting;
import com.example.Service.SystemService;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private SystemService systemService;

    @ModelAttribute("systemSetting")
    public SystemSetting getSystemSetting() {
        return systemService.getLatestSettings();
    }
} 