package com.worldlightapps.classroomlibrary;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
	
	private static final String SALT = "8h39n9&b#c0ebh0*neo33hbi398";

	public static byte[] hashPassword(String password) {
		try {
			MessageDigest digester = MessageDigest.getInstance("SHA-1");
			byte[] bytes = (password + SALT).getBytes("UTF-8");
			digester.update(bytes);
			byte[] digest = digester.digest();
			return digest;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean checkPassword(String offeredPassword, byte[] hashedPassword) {
		byte[] offeredPasswordHash = hashPassword(offeredPassword);
		for (int i = 0; i < offeredPasswordHash.length; i++) {
			if (offeredPasswordHash[i] != hashedPassword[i]) {
				return false;
			}
		}
		return true;
	}
}
