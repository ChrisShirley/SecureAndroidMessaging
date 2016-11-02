package com.android.secure.messaging.keys;

import android.test.mock.MockContext;

import com.thoughtworks.xstream.mapper.Mapper;

import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;

import static org.junit.Assert.*;


/**
 * Created by AJ on 10/31/2016.
 */
public class EncryptTest {
    // Create Keys object "uKeys"
    Keys uKeys = Keys.getInstance();
    // Create Encrypt object "uEncrypt"
    Encrypt uEncrypt;

    @Before
    public void setUp() throws Exception {
        // Generate Keys object
        KeyPairGenerator generator;
        generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();
        PublicKey publicKey = pair.getPublic();

        uEncrypt = new Encrypt(publicKey);
    }

    @Test
    public void setPublicKey() throws Exception {
        // call setPublicKey method
        //uEncrypt.setPublicKey(publicKey);
    }

    public byte[] uCipherTestStr;
    public byte[] uCipherCompStr;

    @Test
    public void encrypt() throws Exception {
        // Input & comparison strings
        String TestStr = "Encryption Test";

        // Capture encrypted "TestStr" from uEncrypt object into uCipherTestStr
        byte[] uCipherTestStr = uEncrypt.encrypt(TestStr.getBytes());

        // Verify call to encrypt method went through encryption path
        assertFalse(uCipherTestStr==null);

        // Capture encrypted "TestStr" from uEncrypt object into uCipherCompStr
        byte[] uCipherCompStr  = uEncrypt.encrypt(TestStr.getBytes());

        // Verify repeatability of encryption method
        // Error/ Not Error? Encryption value is different every time through for both
        // assertArrayEquals(uCipherCompStr, uCipherTestStr);
    }

}