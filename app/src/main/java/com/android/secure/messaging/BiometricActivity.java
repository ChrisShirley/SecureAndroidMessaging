package com.android.secure.messaging;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.secure.messaging.Biometrics.BiometricCipher;
import com.android.secure.messaging.Biometrics.BiometricKeyGenerator;
import com.android.secure.messaging.Biometrics.FingerprintHandler;
import com.android.secure.messaging.Preferences.PreferencesHandler;
import com.android.secure.messaging.email.EmailCommService;
import com.android.secure.messaging.email.EmailGenerator;
import com.android.secure.messaging.email.RandomStringGenerator;

public class BiometricActivity extends AppCompatActivity {

    private FingerprintManager fingerprintManager;
    private FingerprintManager.CryptoObject cryptoObject;
    private CancellationSignal cancellationSignal;
    BiometricKeyGenerator biometricKeyGenerator = new BiometricKeyGenerator();
    BiometricCipher biometricCipher;
    FingerprintHandler fingerprintHandler;
    EmailGenerator emailGenerator = new EmailGenerator();
    private String domain = "@secureandroidmessaging.com";
    String generatedEmail;
    String generatdPassword;

    RandomStringGenerator rsg = new RandomStringGenerator();
    PreferencesHandler preferencesHandler = new PreferencesHandler();
    EmailCommService ecs = new EmailCommService();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric);

        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);


        ecs.execute();

        biometricKeyGenerator.generateKey();
        biometricCipher = new BiometricCipher(biometricKeyGenerator.getKeyStore(), biometricKeyGenerator.getBiometricKey());


        generatedEmail = rsg.generateRandomEmail(12);
        generatdPassword = rsg.generateRandomPassword(10);

        try {
            emailGenerator.execute(generatedEmail, generatdPassword);
            //preferencesHandler.setEmailAddress(getApplicationContext(),generatedEmail + domain);
            System.out.println("This is what is in the email file: " + preferencesHandler.getEmailAddress(getApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("Key: " + biometricKeyGenerator.getBiometricKey());
        // System.out.println("KeyStore: " + biometricKeyGenerator.getKeyStore());

        if (biometricCipher.initCipher()) {
            //   System.out.println("Got into the biometricCipher.initCipher() if loop");
            //Toast.makeText(this, "In the loop", Toast.LENGTH_LONG).show();
            cryptoObject = new FingerprintManager.CryptoObject(biometricCipher.getCipher());
            fingerprintHandler = new FingerprintHandler(this);
            fingerprintHandler.startAuthentication(fingerprintManager, cryptoObject);
            cancellationSignal = fingerprintHandler.getCancellationSignal();
            ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT);
            fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, fingerprintHandler, null);
        }
    }

    public void startHome(Context context) {
        Intent i = new Intent(context, Home.class);
        context.startActivity(i);
        ((Activity) context).setContentView(R.layout.activity_home);

    }

}
