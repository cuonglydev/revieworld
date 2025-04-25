package com.example.Common;

import java.text.Normalizer;

import org.springframework.stereotype.Service;

@Service
public class Slugify {

	  public static String toSlug(String input) {
		  
	        input = input.replaceAll("đ", "d").replaceAll("Đ", "D");

	        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
	        
	        String noDiacritics = normalized.replaceAll("\\p{M}", "");

	        String formattedToSlug = noDiacritics.replaceAll("\\s+", "-");

	        formattedToSlug = formattedToSlug.replaceAll("[^a-zA-Z0-9-]", "");

	        return formattedToSlug.toLowerCase();
	    }


}
