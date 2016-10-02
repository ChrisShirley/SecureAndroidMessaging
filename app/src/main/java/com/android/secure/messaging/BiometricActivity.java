package com.android.secure.messaging;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.secure.messaging.Biometrics.BiometricCipher;
import com.android.secure.messaging.Biometrics.BiometricKeyGenerator;
import com.android.secure.messaging.Biometrics.FingerprintHandler;

public class BiometricActivity extends AppCompatActivity {

    private FingerprintManager fingerprintManager;
    private FingerprintManager.CryptoObject cryptoObject;
    BiometricKeyGenerator biometricKeyGenerator = new BiometricKeyGenerator();
    BiometricCipher biometricCipher;
    FingerprintHandler fingerprintHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric);


        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        biometricKeyGenerator.generateKey();
        biometricCipher = new BiometricCipher(biometricKeyGenerator.getKeyStore(),biometricKeyGenerator.getBiometricKey());

        if(biometricCipher.initCipher()){
            cryptoObject = new FingerprintManager.CryptoObject(biometricCipher.getCipher());
            fingerprintHandler = new FingerprintHandler(this);
            fingerprintHandler.startAuthentication(fingerprintManager,cryptoObject);
        }

    }
}
