package com.youssefrajoul.photoalbums.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class KeyPairUtil {
    /**
     * Generates a KeyPair (private and public keys)
     * 
     * @return the generated KeyPair
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // You can adjust the key size as per your requirement
        return keyPairGenerator.generateKeyPair();
    }
}
