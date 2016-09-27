package com.android.secure.messaging;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.os.Parcelable;
import java.nio.charset.Charset;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.secure.messaging.nfc.NFCHandler;

import static android.nfc.NdefRecord.createMime;

public class NFCActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {

    private static NFCHandler nfcHandler;
    NfcAdapter mNfcAdapter;
    TextView textView;
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

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcHandler = new NFCHandler(this,mNfcAdapter);
        mNfcAdapter.setNdefPushMessageCallback(this, this);


    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String text = ("Beam me up, Android!\n\n" +
                "Beam Time: " + System.currentTimeMillis());
        NdefMessage msg = new NdefMessage( NdefRecord.createApplicationRecord(getPackageName()));
        return msg;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    void processIntent(Intent intent) {
        textView = (TextView) findViewById(R.id.textView);
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        Toast.makeText(this,new String(msg.getRecords()[0].getPayload()),Toast.LENGTH_LONG).show();
    }
}
