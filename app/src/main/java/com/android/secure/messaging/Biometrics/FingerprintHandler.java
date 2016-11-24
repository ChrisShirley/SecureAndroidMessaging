package com.android.secure.messaging.Biometrics;


import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.widget.Toast;

/**
 * Created by Ryan on 10/2/16.
 */

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    CancellationSignal cancellationSignal;
    Context mContext;
    BiometricHandler biometricHandler;
    //BiometricActivity biometricActivity = new BiometricActivity();

    public FingerprintHandler(Context context){
        mContext = context;

    }

    public void startAuthentication(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){

        cancellationSignal = new CancellationSignal();

        if(!biometricHandler.isBiometricsEnabled()){
            return;
        }


    }

    public boolean hasScanner(FingerprintManager fingerprintManager)
    {
        if(biometricHandler==null)
            biometricHandler = new BiometricHandler(mContext,fingerprintManager);
        return biometricHandler.isBiometricsAvailable();
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
        BiometricActivity.startHome();
        //BiometricActivity.startHome();
    }


    public CancellationSignal getCancellationSignal() {
        return cancellationSignal;
    }
}
