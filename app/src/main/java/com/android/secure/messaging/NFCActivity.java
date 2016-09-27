package com.android.secure.messaging;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.secure.messaging.nfc.NFCHandler;

public class NFCActivity extends AppCompatActivity {

    private static NFCHandler nfcHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_nfc);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your nfc action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                if(nfcHandler.deviceHasNFC())
                    nfcHandler.isNFCEnabled();
            }
        });

        nfcHandler = new NFCHandler(this);


    }

}
