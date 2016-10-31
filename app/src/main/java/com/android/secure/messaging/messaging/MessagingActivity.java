package com.android.secure.messaging.messaging;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.secure.messaging.R;
import com.android.secure.messaging.keys.Decrypt;
import com.android.secure.messaging.keys.Keys;

public class MessagingActivity extends AppCompatActivity {

    Decrypt decrypt;
    Keys keys= Keys.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decrypt = new Decrypt(keys.getPrivateKey());
        setContentView(R.layout.activity_messaging);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }
}
