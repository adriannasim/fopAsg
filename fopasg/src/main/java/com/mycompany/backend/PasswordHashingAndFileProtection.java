package com.mycompany.backend;

import java.security.MessageDigest;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class PasswordHashingAndFileProtection {

    // Hash user password using SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error while hashing password: " + e.getMessage());
        }
    }

    // Verify password by comparing hashes
    public static boolean verifyPassword(String hashedPassword, String inputPassword) {
        String inputHash = hashPassword(inputPassword);
        return hashedPassword.equals(inputHash);
    }

    // Encrypt file content using user password
    public static String encryptFileContent(String content, String password) {
        try {
            SecretKey key = generateKey(password);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(content.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error while encrypting file: " + e.getMessage());
        }
    }

    // Decrypt file content using user password
    public static String decryptFileContent(String encryptedContent, String password) {
        try {
            SecretKey key = generateKey(password);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decoded = Base64.getDecoder().decode(encryptedContent);
            byte[] original = cipher.doFinal(decoded);
            return new String(original, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Error while decrypting file: " + e.getMessage());
        }
    }

    // Generate AES key from password
    private static SecretKey generateKey(String password) {
        try {
            byte[] keyBytes = password.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            keyBytes = sha.digest(keyBytes);
            return new SecretKeySpec(keyBytes, 0, 16, "AES");
        } catch (Exception e) {
            throw new RuntimeException("Error while generating key: " + e.getMessage());
        }
    }
}
