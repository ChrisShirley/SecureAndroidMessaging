package com.android.secure.messaging.Biometrics;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.test.mock.MockContext;

import com.android.secure.messaging.BuildConfig;
import com.android.secure.messaging.CustomRobolectricTestRunner;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.annotation.Config;


/**
 * Created by christophershirley on 11/24/16.
 */
@RunWith(CustomRobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class FingerprintHandlerTest {

    FingerprintHandler fingerprintHandler;
    FingerprintManager fingerprintManager;
    Context context;

    @Before
    public void setUp() throws Exception {
        context = new MockContext();
        fingerprintHandler = new FingerprintHandler(context);
        fingerprintManager = Mockito.mock(FingerprintManager.class);
    }


    @SuppressWarnings("MissingPermission")
    @Test
    public void hasScanner() throws Exception {
        Mockito.when(fingerprintManager.isHardwareDetected()).thenReturn(true);
        TestCase.assertTrue(fingerprintHandler.hasScanner(fingerprintManager));
        Mockito.when(fingerprintManager.isHardwareDetected()).thenReturn(false);
        TestCase.assertFalse(fingerprintHandler.hasScanner(fingerprintManager));

    }

}