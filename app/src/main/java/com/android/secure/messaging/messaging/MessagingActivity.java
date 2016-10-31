package com.android.secure.messaging.messaging;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.android.secure.messaging.R;
import com.android.secure.messaging.keys.Decrypt;
import com.android.secure.messaging.keys.Keys;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MessagingActivity extends AppCompatActivity {

    Decrypt decrypt;
    Keys keys= Keys.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        decrypt = new Decrypt(keys.getPrivateKey());
        setContentView(R.layout.activity_messaging);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //getIntent().getStringExtra("contact"));
        setSupportActionBar(toolbar);
        List<String> messages = new ArrayList<>();
        //Get messages from server and decrypt
        //messages = getMessages();

        //Add messages to database

        //displayMessages
        messages.add("dummy message");


    }

    private List<String> getMessages()
    {
        List<String> encryptedMessages = new ArrayList<>();
        //Get messages from the server

        return decryptMessages(encryptedMessages);

    }

    private List<String> decryptMessages(List<String> encryptedMessages)
    {
        List<String> decryptedMessages = new ArrayList<>();
        for (String message : encryptedMessages)
            try {
                decryptedMessages.add(new String(decrypt.decrypt(message.getBytes("ISO-8859-1"))));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        return decryptedMessages;
    }
}
