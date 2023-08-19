package it.regione.rer.parser;

import java.security.GeneralSecurityException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

// 05E4328FBB1EAE9C6D948EA688BA4BCE8D5C3C98FB83B733DE
//byte[] encrypted    = "05E4328FBB1EAE9C6D948EA688BA4BCE8D5C3C98FB83B733DE==".getBytes();
//byte[] db_system_id = "d64eeaae-f3d4-437a-a148-3baf369dec64".getBytes();
public class CCC {
	  public static byte[] decryptPassword(byte[] result) throws GeneralSecurityException {
	    byte constant = result[0];
	    if (constant != (byte)5) {
	        throw new IllegalArgumentException();
	    }

	    byte[] secretKey = new byte[8];
	    System.arraycopy(result, 1, secretKey, 0, 8);

	    byte[] encryptedPassword = new byte[result.length - 9];
	    System.arraycopy(result, 9, encryptedPassword, 0, encryptedPassword.length);

	    byte[] iv = new byte[8];
	    for (int i = 0; i < iv.length; i++) {
	        iv[i] = 0;
	    }

	    Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
	    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretKey, "DES"), new IvParameterSpec(iv));
	    return cipher.doFinal(encryptedPassword);
	  }

	  public static void main(String [] args) {
		args = new String[] {"05E4328FBB1EAE9C6D948EA688BA4BCE8D5C3C98FB83B733DE"};
		
	    if (args.length != 1) {
	      System.err.println("Usage:  java Decrypt <password>");
	      System.exit(1);
	    }

	    if (args[0].length() % 2 != 0) {
	      System.err.println("Password must consist of hex pairs.  Length is odd (not even).");
	      System.exit(2);
	    }

	    byte [] secret = new byte[args[0].length() / 2];
	    for (int i = 0; i < args[0].length(); i += 2) {
	      String pair = args[0].substring(i, i + 2);
	      secret[i / 2] = (byte)(Integer.parseInt(pair,16));
	    }

	    try {
	      System.out.println(new String(decryptPassword(secret)));
	    } catch (GeneralSecurityException e) {
	      e.printStackTrace();
	      System.exit(3);
	    }
	  }
	}