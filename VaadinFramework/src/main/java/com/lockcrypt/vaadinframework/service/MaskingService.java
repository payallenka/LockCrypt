package com.lockcrypt.vaadinframework.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Encrypts passwords with AES-256-GCM.
 * Every password gets its own random key: the key is stored by MySQLHandle and
 * the masked password by MongoDBDatabase, so neither database alone is enough
 * to get the original password back.
 */
@Service
public class MaskingService {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int KEY_LENGTH = 32;      // bytes, AES-256
    private static final int IV_LENGTH = 12;       // bytes, recommended for GCM
    private static final int TAG_LENGTH = 128;     // bits

    private final SecureRandom secureRandom = new SecureRandom();

    public String generateKey() {
        byte[] key = new byte[KEY_LENGTH];
        secureRandom.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    public String encrypt(String orgPassword, String key) {
        byte[] iv = new byte[IV_LENGTH];
        secureRandom.nextBytes(iv);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, toSecretKey(key), new GCMParameterSpec(TAG_LENGTH, iv));
            byte[] encrypted = cipher.doFinal(orgPassword.getBytes(StandardCharsets.UTF_8));

            // the iv is not secret, it is stored in front of the encrypted bytes
            byte[] maskedValue = ByteBuffer.allocate(iv.length + encrypted.length).put(iv).put(encrypted).array();
            return Base64.getEncoder().encodeToString(maskedValue);
        } catch (GeneralSecurityException e) {
            throw new MaskingException("Could not encrypt password", e);
        }
    }

    public String decryptPassword(String key, String maskedPassword) {
        try {
            ByteBuffer maskedValue = ByteBuffer.wrap(Base64.getDecoder().decode(maskedPassword));
            byte[] iv = new byte[IV_LENGTH];
            maskedValue.get(iv);
            byte[] encrypted = new byte[maskedValue.remaining()];
            maskedValue.get(encrypted);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, toSecretKey(key), new GCMParameterSpec(TAG_LENGTH, iv));
            return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
        } catch (GeneralSecurityException | IllegalArgumentException | BufferUnderflowException e) {
            // wrong key, tampered data or not our format at all
            throw new MaskingException("Could not decrypt password", e);
        }
    }

    private SecretKeySpec toSecretKey(String key) {
        return new SecretKeySpec(Base64.getDecoder().decode(key), "AES");
    }
}
