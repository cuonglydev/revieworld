package com.example.Security;

import org.springframework.security.core.context.SecurityContextHolder;

import com.example.Entity.Admin;
import com.example.Entity.User;

public class SecurityUtils {

	  public static Admin getCurrentAdmin() {
	        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        if (principal instanceof CustomAdminDetails) {
	            return ((CustomAdminDetails) principal).getAdmin();
	        }
	        return null;
	    }

	    public static User getCurrentUser() {
	        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        if (principal instanceof CustomUserDetails) {
	            return ((CustomUserDetails) principal).getUser();
	        }
	        return null;
	    }
}
