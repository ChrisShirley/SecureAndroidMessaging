package com.android.secure.messaging.Biometrics;

/**
 * Created by silanr on 9/19/2016.
 */

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

public class BiometricHandlerTest {

    BiometricHandler biometricHandler;

    @Before
    public void setUp() throws Exception {
        //biometricHandler = new BiometricHandler();
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

    @Test
    public void grantAccess() throws Exception{
        TestCase.assertTrue(biometricHandler.grantAccess());
    }

    @Test
    public void denyAccess() throws Exception{
        TestCase.assertTrue(biometricHandler.grantAccess());
    }
}
