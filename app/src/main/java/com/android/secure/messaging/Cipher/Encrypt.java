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
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Encrypt extends Activity {

    TextView textView1;
    TextView textView2;
    EditText editText1;
    EditText editText2;
    Button button1;
    String publicKeyFilename = null;
    String message = null;
    byte[] pubKeyBytes = null;
    PublicKey publicKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt);

        File public_file = new File(this.getFilesDir(), getString(R.string.key_public));
        pubKeyBytes = readFile(public_file);
        editText2 = (EditText) findViewById(R.id.editText2);
        try
        {
            publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubKeyBytes));
        } catch (Exception e)
        {
            editText2.setText("Encrypt 1\n" + e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.encrypt, menu);
        return true;
    }

    public void onClick(View view)
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
    }

    public void copyText(View view)
    {
        editText2 =(EditText) findViewById(R.id.editText2);
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simple text",editText2.getText().toString());
        clipboard.setPrimaryClip(clip);
    }

    public byte[] encrypt(byte[] array)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] cipherText = cipher.doFinal(array);
            return cipherText;
        }
        catch(Exception e)
        {
            return null;
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

}