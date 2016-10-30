package com.android.secure.messaging.keys;

import android.view.View;
import android.widget.EditText;

import java.security.PublicKey;

import javax.crypto.Cipher;

/**
 * Created by cjs07f on 10/24/16.
 * Modified by nick on 10/30/2016
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

/*    public void onClick(View view)
    {
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        message = editText2.getText().toString();

        try
        {
            byte[] messageBytes = message.getBytes("ISO-8859-1");
            byte[] buffer = new byte[64];
            int index = 0;
            byte[] bites = new byte[1];
            byte[] encryptedBytes;
            String encrypted_string = "";

            while(index < messageBytes.length)
            {
                for(int i = 0; i < 64; i++)
                {
                    if(index >= messageBytes.length) buffer[i] = bites[0];
                    else buffer[i] = messageBytes[index];
                    index++;
                }
                encryptedBytes = encrypt(buffer);
                encrypted_string += new String(encryptedBytes, "ISO-8859-1");
            }
            editText2.setText(encrypted_string);
        }
        catch(Exception e)
        {
            editText1.setText("Encrypt 2\n" + e.toString());
        }
    }   */

}

