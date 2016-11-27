package com.android.secure.messaging.email;

import android.app.Activity;
import android.os.Bundle;


import com.android.secure.messaging.BuildConfig;
import com.android.secure.messaging.CustomRobolectricTestRunner;
import com.android.secure.messaging.Home;
import com.android.secure.messaging.Preferences.Preferences;
import com.android.secure.messaging.Preferences.PreferencesHandler;

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
public class EmailHandlerTest {

    private EmailHandler emailHandler;
    private Activity homeActivity;

    @Before
    public void setUp() throws Exception {
        homeActivity = Robolectric.setupActivity(Home.class);
        emailHandler =  EmailHandler.getInstance(homeActivity);
        PreferencesHandler  preferencesHandler = new PreferencesHandler();
        preferencesHandler.setPreference(homeActivity,"SAMEmail","test@test.com");
    }

    @Test
    public void getUniqueEmail()
    {
        TestCase.assertEquals(emailHandler.getUniqueEmail(),"test@test.com");
    }






}