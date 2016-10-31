package com.android.secure.messaging.messaging;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.secure.messaging.R;
import com.android.secure.messaging.keys.Decrypt;
import com.android.secure.messaging.keys.Keys;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MessagingActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_messaging);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(getIntent().getStringExtra("contact"));
        setSupportActionBar(toolbar);
        List<String> messages = new ArrayList<>();

        TextView textView = (TextView) findViewById(R.id.messaging_text_view);
        textView.append(" Bob!");
        //Get messages from server and decrypt
        //messages = getMessages();

        //Add messages to database

        //displayMessages
        textView.append("\n dummy message");


    }

    private List<String> getMessages()
    {
        List<String> serverMessages = new ArrayList<>();
        //Get messages from the server

        return serverMessages;

    }


}
