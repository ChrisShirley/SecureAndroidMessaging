package com.android.secure.messaging.nfc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.app.Activity;

/**
 * Created by christophershirley on 9/17/16.
 */
public class NFCHandler {

    Context context;
    NfcAdapter mNfcAdapter;
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    public NFCHandler(Context ctx,NfcAdapter adapter)
    {
        context = ctx;
        mNfcAdapter = adapter;
    }

    public boolean deviceHasNFC()
    {
        //Check if NFC is available on device

        if(mNfcAdapter != null)
            return true;


        return false;
    }



    public boolean isNFCEnabled()
    {
        if(mNfcAdapter.isEnabled()) {

            return true;
        }
        else {
             AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("NFC not enabled. Please enable NFC to continue and try again.").setPositiveButton("Continue", dialogClickListener)
                    .setNegativeButton("Cancel", dialogClickListener).show();
        }

        return false;

    }

      boolean accept()
    {
        return false;
    }

      boolean request()
    {
        return false;
    }
}
