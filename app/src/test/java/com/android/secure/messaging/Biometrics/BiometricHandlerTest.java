package com.android.secure.messaging.Biometrics;

/**
 * Created by silanr on 9/19/2016.
 */

import android.app.Activity;
import android.content.Context;

import com.android.secure.messaging.BuildConfig;
import com.android.secure.messaging.CustomRobolectricTestRunner;
import com.android.secure.messaging.contacts.ContactHandler;
import com.android.secure.messaging.contacts.ContactsActivity;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

@RunWith(CustomRobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class BiometricHandlerTest {

    private Activity biometricActivity;
    private BiometricHandler biometricHandler;

    @Before
    public void setUp() throws Exception {

        biometricActivity = Robolectric.setupActivity(BiometricActivity.class);
        //boolean available = biometricActivity.checkBiometricStatus();
        //biometricHandler = biometricActivity.fingerprintHandler.biometricHandler;
    }

    @Test
    public void bioIsAvailable() throws Exception{
       TestCase.assertTrue(biometricHandler.isBiometricsAvailable());
    }

    @Test
    public void bioIsNotAvailable() throws Exception{
        TestCase.assertFalse(biometricHandler.isBiometricsAvailable());
    }

    @Test
    public void bioIsEnabled() throws Exception{
        TestCase.assertTrue(biometricHandler.isBiometricsEnabled());
    }

    @Test
    public void bioIsDisabled() throws Exception{
        TestCase.assertFalse(biometricHandler.isBiometricsEnabled());
    }

   /* @Test
    public void grantAccess() throws Exception{
        TestCase.assertTrue(biometricHandler.grantAccess());
    }

    @Test
    public void denyAccess() throws Exception{
        TestCase.assertTrue(biometricHandler.grantAccess());
    }*/
}
