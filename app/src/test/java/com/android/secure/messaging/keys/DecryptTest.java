package com.android.secure.messaging.keys;

import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.PrivateKey;


import static org.junit.Assert.*;

/**
 * Created by AJ on 11/1/2016.
 */
public class DecryptTest {
    // Create Keys object "uKeys"
    Keys uKeys = Keys.getInstance();
    // Create Encrypt object "uEncrypt"
    Encrypt uEncrypt;
    // Create Decrypt object "uDecrypt"
    Decrypt uDecrypt;

    @Before
    public void setUp() throws Exception {
        // Generate Keys object
        KeyPairGenerator generator;
        generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();
        PublicKey publicKey = pair.getPublic();
        PrivateKey privateKey = pair.getPrivate();

        uEncrypt = new Encrypt(publicKey);
        uDecrypt = new Decrypt(privateKey);
    }

    public byte[] uEncryptedTestStr;
    public byte[] uDecryptedTestStr;

    @Test
    public void decrypt() throws Exception {
        // Input & comparison strings
        String TestStr = "SAM Message";

        // Capture encrypted "TestStr" from encrypt() method into uEncryptedTestStr
        byte[] uEncryptedTestStr = uEncrypt.encrypt(TestStr.getBytes());

        // Capture Decrypted "TestStr" from decrypt() method into uDecryptedTestStr
        byte[] uDecryptedTestStr = uDecrypt.decrypt(uEncryptedTestStr);

        // Verify call to decrypt method went through decryption path
        assertFalse(uDecryptedTestStr==null);

        // Verify encrypted and decrypted TestStr's are not equal
        assertFalse(uEncryptedTestStr==uDecryptedTestStr);

        // Verify encrypted "TestStr" was decrypted correctly
        assertArrayEquals(TestStr.getBytes(), uDecryptedTestStr);
    }

}