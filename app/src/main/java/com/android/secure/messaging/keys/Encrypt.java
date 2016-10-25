package com.android.secure.messaging.keys;

import java.security.PublicKey;

import javax.crypto.Cipher;

/**
 * Created by cjs07f on 10/24/16.
 */

public class Encrypt {
    private PublicKey publicKey;

    public Encrypt(PublicKey pkey) {
        publicKey = pkey;

    }

    public void setPublicKey(PublicKey pKey)
    {
        publicKey = pKey;
    }

    public byte[] encrypt(byte[] array) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] cipherText = cipher.doFinal(array);
            return cipherText;
        } catch (Exception e) {
            return null;
        }
    }
}

