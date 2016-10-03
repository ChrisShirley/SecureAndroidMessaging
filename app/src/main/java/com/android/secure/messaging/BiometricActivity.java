package com.android.secure.messaging;

import android.Manifest;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.content.ContextCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.secure.messaging.Biometrics.BiometricCipher;
import com.android.secure.messaging.Biometrics.BiometricKeyGenerator;
import com.android.secure.messaging.Biometrics.FingerprintHandler;

public class BiometricActivity extends AppCompatActivity {

    private FingerprintManager fingerprintManager;
    private FingerprintManager.CryptoObject cryptoObject;
    private CancellationSignal cancellationSignal;
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


        System.out.println("Key: " + biometricKeyGenerator.getBiometricKey());
        System.out.println("KeyStore: " + biometricKeyGenerator.getKeyStore());

        if(biometricCipher.initCipher()){
            System.out.println("Got into the biometricCipher.initCipher() if loop");
            Toast.makeText(this, "In the loop", Toast.LENGTH_LONG).show();
            cryptoObject = new FingerprintManager.CryptoObject(biometricCipher.getCipher());
            fingerprintHandler = new FingerprintHandler(this);
            fingerprintHandler.startAuthentication(fingerprintManager,cryptoObject);
            cancellationSignal = fingerprintHandler.getCancellationSignal();
            ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT);
            fingerprintManager.authenticate(cryptoObject, cancellationSignal ,0, fingerprintHandler,null);

        }

    }
}
