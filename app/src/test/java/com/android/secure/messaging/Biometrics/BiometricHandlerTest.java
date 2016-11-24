package com.android.secure.messaging.Biometrics;

/**
 * Created by silanr on 9/19/2016.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.test.mock.MockContext;

import com.android.secure.messaging.BuildConfig;
import com.android.secure.messaging.CustomRobolectricTestRunner;
import com.android.secure.messaging.contacts.ContactHandler;
import com.android.secure.messaging.contacts.ContactsActivity;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static android.content.Context.FINGERPRINT_SERVICE;

@RunWith(CustomRobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class BiometricHandlerTest {

    private Activity biometricActivity;
    private BiometricHandler biometricHandler;
    private FingerprintManager fingerprintManager;
    private Context context;

    @Before
    public void setUp() throws Exception {


        //biometricActivity = Robolectric.setupActivity(BiometricActivity.class);
        //boolean available = biometricActivity.checkBiometricStatus();
        //fingerprintHandler = new FingerprintHandler(biometricActivity.getBaseContext());
        context = Mockito.mock(Context.class);
        fingerprintManager = Mockito.mock(FingerprintManager.class);
        biometricHandler = new BiometricHandler(context, fingerprintManager);
    }

    @Test
    public void bioIsAvailable() throws Exception {

        ContextCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT);
        Mockito.when(fingerprintManager.isHardwareDetected()).thenReturn(true);
        TestCase.assertTrue(biometricHandler.isBiometricsAvailable());
        Mockito.when(fingerprintManager.isHardwareDetected()).thenReturn(false);
        TestCase.assertFalse(biometricHandler.isBiometricsAvailable());
        Mockito.when(ContextCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT)).thenReturn(-1);
        TestCase.assertFalse(biometricHandler.isBiometricsAvailable());
    }

    @Test
    public void bioIsNotAvailable() throws Exception{
        TestCase.assertFalse(biometricHandler.isBiometricsAvailable());
    }

    @Test
    public void bioIsEnabled() throws Exception{
        Mockito.when(ContextCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT)).thenReturn(0);
        TestCase.assertFalse(biometricHandler.isBiometricsEnabled());

        Mockito.when(biometricHandler.getmFingerprintManager().hasEnrolledFingerprints()).thenReturn(true);
        TestCase.assertTrue(biometricHandler.isBiometricsEnabled());
    }


}
