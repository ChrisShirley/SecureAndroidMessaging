package com.android.secure.messaging.Biometrics;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.os.CancellationSignal;
import android.widget.Toast;

/**
 * Created by Ryan on 10/2/16.
 */

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    CancellationSignal cancellationSignal;
    Context mContext;

    public FingerprintHandler(Context context){
        mContext = context;
    }
    public void startAuthentication(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){

        cancellationSignal = new CancellationSignal();

        if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED){
            return;
        }


    }

    //Override methods from AuthenticationCallBack super class
    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString){
        Toast.makeText(mContext, "Authentication Error\n" + errString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationFailed(){
        Toast.makeText(mContext, "Authentication Failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        Toast.makeText(mContext, "Successful Authentication!", Toast.LENGTH_LONG).show();
    }
}
