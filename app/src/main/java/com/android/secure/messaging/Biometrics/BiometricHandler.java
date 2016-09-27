package com.android.secure.messaging.Biometrics;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by silanr on 9/19/2016.
 */
public class BiometricHandler extends Activity {


    FingerprintManager fingerprintManager;
    boolean biometricsAvailable;
    boolean biometricsEnabled;
    Context context;

    //Method to declare content, needed in order for getSystemService to work.  When you create an
    //new instance of the BiometricHandler class, use the following format:
    //BiometricHandler name = new BiometricHandler(context)
    public BiometricHandler(Context mContext) {
        //Set class-level context variable equal to the context that is passed in upon creation
        //of new instance of BiometricHandler class
        context = mContext;
        fingerprintManager = (FingerprintManager) context.getSystemService(FINGERPRINT_SERVICE);
    }

    public boolean isBiometricsAvailable() {
        biometricsAvailable = fingerprintManager.isHardwareDetected();
        System.out.println("Device has fingerprint scanner: " + biometricsAvailable);
        return biometricsAvailable;
    }

    public boolean isBiometricsEnabled() {
        biometricsEnabled = fingerprintManager.hasEnrolledFingerprints();
        System.out.println("Device has fingerprints enrolled: " + biometricsEnabled);
        return biometricsEnabled;
    }

    boolean grantAccess() {

        return false;
    }

}
