package com.example.Common;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class TokenGenerator {
	

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	public String generateToken() {
		 Random random = new Random();
	        
	        StringBuilder token = new StringBuilder();
	        
	        for (int i = 0; i < 6; i++) {
	        	
	        	 int randomIndex = random.nextInt(CHARACTERS.length());
	             token.append(CHARACTERS.charAt(randomIndex));
	        }
	        
	        return token.toString();
	    }

	public String generateInviteCode() {
		Random random = new Random();
		
		StringBuilder inviteCode = new StringBuilder();
		
		for(int i = 0; i < 8; i++) {
			int randomIndex = random.nextInt(CHARACTERS.length());
			inviteCode.append(CHARACTERS.charAt(randomIndex));
		}
		return inviteCode.toString();
	}
	  
}
