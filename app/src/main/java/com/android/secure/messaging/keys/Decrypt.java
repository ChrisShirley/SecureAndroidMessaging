package com.android.secure.messaging.keys;

import java.security.PrivateKey;

import javax.crypto.Cipher;

/**
 * Created by cjs07f on 10/24/16.
 */

public class Decrypt {

    private PrivateKey myPrivateKey;

    public Decrypt(PrivateKey privateKey)
    {
        myPrivateKey = privateKey;
    }

    public byte[] decrypt( byte[] array)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, myPrivateKey);
            byte[]newPlainText = cipher.doFinal(array);
            return newPlainText;
        }
        catch(Exception e)
        {
            return null;
        }
    }
}
