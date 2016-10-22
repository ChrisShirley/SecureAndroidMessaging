package com.android.secure.messaging.Cipher;

/**
 * Created by nerlacker on 10/21/2016.
 */
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;


public class Decrypt extends Activity
{
    EditText editText1;
    byte[] privKeyBytes = null;
    PrivateKey privateKey = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);

        editText1 = (EditText) findViewById(R.id.editText1);

        File private_file = new File(this.getFilesDir(), getString(R.string.key_private));
        privKeyBytes = readFile(private_file);
        try
        {
            privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privKeyBytes));
        } catch (Exception e)
        {
            editText1.setText("Encrypt 1\n" + e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.decrypt, menu);
        return true;
    }

    public void onClick(View view)
    {
        editText1 = (EditText) findViewById(R.id.editText1);

        try
        {
            byte[] messageBytes = editText1.getText().toString().getBytes("ISO-8859-1");
            byte[] buffer = new byte[64];
            int index = 0;
            byte[] bites = new byte[1];
            byte[] decryptedBytes;
            String decrypted_string = "";

            while(index < messageBytes.length)
            {
                for(int i = 0; i < 64; i++)
                {
                    if(index >= messageBytes.length) buffer[i] = bites[0];
                    else buffer[i] = messageBytes[index];
                    index++;
                }
                decryptedBytes = decrypt(privateKey, buffer);
                decrypted_string += new String(decryptedBytes, "ISO-8859-1");
            }
            editText1.setText(decrypted_string);
        }
        catch(Exception e)
        {
            editText1.setText("Section 2" + e.toString());
        }

    }

    public byte[] readFile(File input_file)
    {
        editText1 = (EditText) findViewById(R.id.editText1);
        File file = input_file;
        byte[] result = null;
        try
        {
            InputStream input =  new BufferedInputStream(new FileInputStream(file));
            result = readAndClose(input);
        }
        catch (Exception ex)
        {
            editText1.setText("Encrypted 3\n" + ex.toString());
        }
        return result;
    }

    public byte[] readAndClose(InputStream aInput)
    {
        editText1 = (EditText) findViewById(R.id.editText1);

        //carries the data from input to output :
        byte[] bucket = new byte[32*1024];
        ByteArrayOutputStream result = null;
        try
        {
            try
            {
                result = new ByteArrayOutputStream(bucket.length);
                int bytesRead = 0;
                while(bytesRead != -1)
                {
                    bytesRead = aInput.read(bucket);
                    if(bytesRead > 0)
                    {
                        result.write(bucket, 0, bytesRead);
                    }
                }
            }
            finally
            {
                aInput.close();
            }
        }
        catch (Exception ex)
        {
            editText1.setText("Encrypt 4\n" + ex.toString());
        }
        return result.toByteArray();
    }

    public byte[] decrypt(PrivateKey privateKey, byte[] array)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[]newPlainText = cipher.doFinal(array);
            return newPlainText;
        }
        catch(Exception e)
        {
            return null;
        }
    }
}
