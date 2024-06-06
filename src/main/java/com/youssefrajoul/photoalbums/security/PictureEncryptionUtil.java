package com.youssefrajoul.photoalbums.security;

import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class PictureEncryptionUtil {

    private static final String ALGORITHM = "AES";

    public static String encrypt(byte[] data, PublicKey publicKey) throws Exception {
        try {
            // Generate AES key
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            SecretKey aesKey = keyGen.generateKey();

            // Encrypt data with AES key
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encryptedData = aesCipher.doFinal(data);

            // Encrypt AES key with RSA
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedAesKey = rsaCipher.doFinal(aesKey.getEncoded());

            // Combine encrypted AES key and encrypted data
            byte[] combined = new byte[encryptedAesKey.length + encryptedData.length];
            System.arraycopy(encryptedAesKey, 0, combined, 0, encryptedAesKey.length);
            System.arraycopy(encryptedData, 0, combined, encryptedAesKey.length, encryptedData.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new Exception("Error encrypting data with public key", e);
        }
    }

    public static String decrypt(byte[] encryptedData, Key privatKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privatKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }

    public static PublicKey stringToKey(String publicK) {
        PublicKey pubKey = null;
        try {
            byte[] publicBytes = Base64.getDecoder().decode(publicK);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            pubKey = keyFactory.generatePublic(keySpec);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return pubKey;
    }
}