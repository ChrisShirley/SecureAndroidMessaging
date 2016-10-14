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
import com.android.secure.messaging.Preferences.Preferences;
import com.android.secure.messaging.Preferences.PreferencesHandler;
import com.android.secure.messaging.email.EmailCommService;
import com.android.secure.messaging.email.EmailGenerator;
import com.android.secure.messaging.email.RandomStringGenerator;

public class BiometricActivity extends AppCompatActivity {

    //final private String emailPrefName = "SAMEmail";
    private FingerprintManager fingerprintManager;
    private FingerprintManager.CryptoObject cryptoObject;
    private CancellationSignal cancellationSignal;
    private BiometricKeyGenerator biometricKeyGenerator = new BiometricKeyGenerator();
    private BiometricCipher biometricCipher;
    private FingerprintHandler fingerprintHandler;
    private EmailGenerator emailGenerator = new EmailGenerator();
    private String domain = "@secureandroidmessaging.com";
    private String generatedEmail;
    private String generatedPassword;

    RandomStringGenerator rsg = new RandomStringGenerator();
    Preferences preferencesHandler = new PreferencesHandler();
    EmailCommService ecs = new EmailCommService();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric);

        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        biometricKeyGenerator.generateKey();
        biometricCipher = new BiometricCipher(biometricKeyGenerator.getKeyStore(), biometricKeyGenerator.getBiometricKey());

        generatedEmail = rsg.generateRandomEmail(12);
        generatedPassword = rsg.generateRandomPassword(10);

        try {
            //Generators random email and password
            emailGenerator.execute(generatedEmail, generatedPassword);
            //Sets generated email and password in shared preferences
            preferencesHandler.setPreference(getApplicationContext(), preferencesHandler.emailPrefName, generatedEmail + domain);
            preferencesHandler.setPreference(getApplicationContext(), preferencesHandler.passwordPrefName, generatedPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (biometricCipher.initCipher()) {

            cryptoObject = new FingerprintManager.CryptoObject(biometricCipher.getCipher());
            fingerprintHandler = new FingerprintHandler(this);
            fingerprintHandler.startAuthentication(fingerprintManager, cryptoObject);
            cancellationSignal = fingerprintHandler.getCancellationSignal();
            ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT);
            fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, fingerprintHandler, null);
        }
    }

    public void startHome(Context context) {
        System.out.println("The email preference is: " + preferencesHandler.getPreference(context, preferencesHandler.emailPrefName));
        System.out.println("The email preference is: " + preferencesHandler.getPreference(context, preferencesHandler.passwordPrefName));

        if (preferencesHandler.getPreference(getApplicationContext(), preferencesHandler.emailPrefName) == null) {
            System.out.println("Email Pref name is null");
            System.out.println(preferencesHandler.getPreference(getApplicationContext(), preferencesHandler.emailPrefName));
        } else {
            System.out.println("Email pref name is not null");
            System.out.println(preferencesHandler.getPreference(getApplicationContext(), preferencesHandler.emailPrefName));
        }
        Intent i = new Intent(context, Home.class);
        context.startActivity(i);
        ((Activity) context).setContentView(R.layout.activity_home);

    }

}
