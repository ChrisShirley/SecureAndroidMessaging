package com.android.secure.messaging;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.android.secure.messaging.email.SendEmail;
import com.android.secure.messaging.email.EmailGenerator;
import com.android.secure.messaging.RandomStringGenerator.RandomStringGenerator;
import com.android.secure.messaging.email.EmailHandler;

import android.view.WindowManager;

public class BiometricActivity extends AppCompatActivity {

    private FingerprintManager fingerprintManager;
    private FingerprintManager.CryptoObject cryptoObject;
    private CancellationSignal cancellationSignal;
    private static Context context;
    BiometricKeyGenerator biometricKeyGenerator = new BiometricKeyGenerator();
    BiometricCipher biometricCipher;
    FingerprintHandler fingerprintHandler;
//    EmailGenerator emailGenerator = new EmailGenerator();
//    private String domain = "@secureandroidmessaging.com";
//    String generatedEmail;
//    String generatedPassword;
    private static boolean inOnCreate = false;

    //RandomStringGenerator rsg = new RandomStringGenerator();
    Preferences preferencesHandler = new PreferencesHandler();
    //SendEmail ecs = new SendEmail();
    EmailHandler emailHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_biometric);
        inOnCreate = true;
        context = this;
        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        fingerprintHandler = new FingerprintHandler(this);
        emailHandler = new EmailHandler(context);

        if(!checkBiometricStatus())
            return;

        biometricKeyGenerator.generateKey();
        biometricCipher = new BiometricCipher(biometricKeyGenerator.getKeyStore(), biometricKeyGenerator.getBiometricKey());

        //Just needed for testing, can be deleted later
        /*
        System.out.println("This is the email: " + preferencesHandler.getPreference(context, preferencesHandler.getEmailPrefName()));
        preferencesHandler.resetPreferences(context);
        System.out.println("This is the email: " + preferencesHandler.getPreference(context, preferencesHandler.getEmailPrefName()));
        System.out.println("This is the password: " + preferencesHandler.getPreference(context, preferencesHandler.getPasswordPrefName()));
         */
        if (biometricCipher.initCipher()) {
            cryptoObject = new FingerprintManager.CryptoObject(biometricCipher.getCipher());
            fingerprintHandler.startAuthentication(fingerprintManager, cryptoObject);
            cancellationSignal = fingerprintHandler.getCancellationSignal();
            startBiometricAuthentication();
        }



    }

    private void startBiometricAuthentication()
    {
            ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT);
            fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, fingerprintHandler, null);

    }

    private boolean checkBiometricStatus()
    {
        ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT);
        if(!fingerprintHandler.hasScanner(fingerprintManager)) {
            createBiometricAlert();
            return false;
        }
        else if(!fingerprintManager.hasEnrolledFingerprints()) {
            createNoFingerPrintAlert();
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!inOnCreate) {
            if (checkBiometricStatus())
                startBiometricAuthentication();
        }
        else
            inOnCreate=false;
        //Toast.makeText(this, "Resuming!", Toast.LENGTH_LONG).show();

    }

    public void createNoFingerPrintAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Fingerprints Detected");

        // Set an EditText view to get user input


        builder.setMessage("You must have at least one fingerprint on file." +
                "Please exit the application, add fingerprints, and try again.").setNegativeButton("Exit", dialogClickListener);

        final AlertDialog biometricDialog = builder.create();
        biometricDialog.show();


    }

    public void createBiometricAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Fingerprint Scanner Detected");

        // Set an EditText view to get user input


        builder.setMessage("Not having a fingerprint scanner increases your security risk." +
                "Would you like to continue?").setPositiveButton("Continue", dialogClickListener)
                .setNegativeButton("Exit", dialogClickListener);

        final AlertDialog biometricDialog = builder.create();
        biometricDialog.show();


    }

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        startHome();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        finish();
                        System.exit(0);
                        break;
                }
            }
        };

    public void startHome() {
        emailHandler = new EmailHandler(context);

        //testing purposes, resets preferences each time app loads to re-create email
        preferencesHandler.resetPreferences(context);
        //Check to see if the application has already assigned an email address
        if(emailHandler.getUniqueEmail() == null){
            //If null is returned, create email address.
            emailHandler.requestUniqueEmailAddress();
        }
        /*
        emailHandler.send("testaccount@secureandroidmessaging.com", preferencesHandler.getPreference(context,
                preferencesHandler.getEmailPrefName()), preferencesHandler.getPreference(context,
                preferencesHandler.getPasswordPrefName()), "this is a test");
        emailHandler.read("testaccount@secureandroidmessaging.com", "Sweng501#");
        */
        Intent i = new Intent(context, Home.class);
        context.startActivity(i);
    }

}
