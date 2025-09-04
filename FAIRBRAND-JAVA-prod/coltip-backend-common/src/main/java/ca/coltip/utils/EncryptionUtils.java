package ca.coltip.utils;

import ca.coltip.constants.EncryptionConstants;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class EncryptionUtils {

    private static final int IV_LENGTH = 12; // 12 bytes for GCM IV (AES standard)
    private static final int TAG_LENGTH = 16; // 16 bytes for GCM authentication tag
    private static final int KEY_SIZE = 256; // AES-256
    private EncryptionUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Encode a string to base 64
     *
     * @param toEncode the string to encode
     * @return the encoded string
     */
    public static String encodeToBase64(String toEncode) {
        if (toEncode == null) return null;

        return Base64.getEncoder().encodeToString(toEncode.getBytes());
    }

    /**
     * Decode a string from base 64
     *
     * @param toDecode the string to decode
     * @return the decoded string
     */
    public static String decodeFromBase64(String toDecode) {
        if (toDecode == null) return null;
        byte[] data = Base64.getDecoder().decode(toDecode);
        return new String(data);
    }

    /**
     * Use to generate AES key
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(EncryptionConstants.AES_KEY_SIZE);
        SecretKey sk = keyGen.generateKey();

        return Base64.getEncoder().encodeToString(sk.getEncoded());
    }

    /**
     * Use to encrypt the pin with secret key
     *
     * @param plainText
     * @param key
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     */
    public static String encrypt(String plainText, String key) throws Exception {
        // Generate a random IV
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[IV_LENGTH];
        secureRandom.nextBytes(iv);

        // Create a SecretKeySpec from the given key (Base64 encoded)
        byte[] decodedKey = Base64.getDecoder().decode(key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(decodedKey, "AES");

        // Create GCMParameterSpec with IV and tag length (16 bytes)
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH * 8, iv);

        // Initialize Cipher for encryption
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);

        // Encrypt the plain text
        byte[] encryptedText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        // Concatenate IV, encrypted text, and authentication tag
        byte[] encryptedMessage = new byte[IV_LENGTH + encryptedText.length];
        System.arraycopy(iv, 0, encryptedMessage, 0, IV_LENGTH);
        System.arraycopy(encryptedText, 0, encryptedMessage, IV_LENGTH, encryptedText.length);

        // Return the encrypted message (Base64 encoded)
        return Base64.getEncoder().encodeToString(encryptedMessage);
    }

    // Method to decrypt the cipher text
    public static String decrypt(String encryptedText, String key) throws Exception {
        // Decode the encrypted text from Base64
        byte[] decodedEncryptedText = Base64.getDecoder().decode(encryptedText);

        // Extract the IV (first 12 bytes)
        byte[] iv = new byte[IV_LENGTH];
        System.arraycopy(decodedEncryptedText, 0, iv, 0, IV_LENGTH);

        // Extract the ciphertext (the rest except the last 16 bytes for tag)
        byte[] cipherText = new byte[decodedEncryptedText.length - IV_LENGTH];
        System.arraycopy(decodedEncryptedText, IV_LENGTH, cipherText, 0, cipherText.length);

        // Create SecretKeySpec from the Base64 encoded key
        byte[] decodedKey = Base64.getDecoder().decode(key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(decodedKey, "AES");

        // Set up GCMParameterSpec with IV and tag length
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH * 8, iv);

        // Initialize Cipher for decryption
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);

        // Decrypt the ciphertext
        byte[] decryptedBytes = cipher.doFinal(cipherText);

        // Return the decrypted text as a String
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static String getIV(String encryptedText){
        byte[] encryptedTextBytes = Base64.getDecoder().decode(encryptedText);
        byte[] IV = Arrays.copyOfRange(encryptedTextBytes, 0, EncryptionConstants.GCM_IV_LENGTH);
        return Base64.getEncoder().encodeToString(IV);
    }
}
