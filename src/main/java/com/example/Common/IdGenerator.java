package com.example.Common;

import java.util.Random;

public class IdGenerator {

	private static final String PREFIX = "GD";
	private static final int LENGTH = 10;
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	public static String generator() {
		Random random = new Random();
		StringBuilder sb = new StringBuilder(PREFIX);
		
		for(int i = 0; i < LENGTH; i++) {
			 int index = random.nextInt(CHARACTERS.length());
	         sb.append(CHARACTERS.charAt(index));
		}
		
		return sb.toString();
	}
}
