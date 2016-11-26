package com.android.secure.messaging.Preferences;

import android.app.Activity;
import android.content.Context;
import android.nfc.NfcAdapter;

import com.android.secure.messaging.BuildConfig;
import com.android.secure.messaging.CustomRobolectricTestRunner;
import com.android.secure.messaging.Home;
import com.android.secure.messaging.nfc.NFCHandler;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

/**
 * Created by christophershirley on 9/17/16.
 */
@RunWith(CustomRobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class PreferencesHandlerTest {

    Activity homeActivity;
    PreferencesHandler preferencesHandler;

    @Before
    public void setUp() throws Exception {
        homeActivity = Robolectric.setupActivity(Home.class);
        preferencesHandler = new PreferencesHandler();
    }

    @Test
    public void getPreference()
    {
        preferencesHandler.setPreference(homeActivity,"testName","testValue");
        TestCase.assertEquals(preferencesHandler.getPreference(homeActivity,"testName"),"testValue");
    }

    @Test
    public void getEmailPrefName()
    {
        TestCase.assertEquals(preferencesHandler.getEmailPrefName(),"SAMEmail");
    }

    @Test
    public void getPasswordPrefName()
    {
        TestCase.assertEquals(preferencesHandler.getPasswordPrefName(),"SAMPassword");
    }

    @Test
    public void getNewEmailPrefName()
    {
        TestCase.assertEquals(preferencesHandler.getNewEmailPrefName(),"NewEmail");
    }


}