package com.android.secure.messaging.nfc;

import android.app.Activity;
import android.nfc.NfcAdapter;

import com.android.secure.messaging.BuildConfig;
import com.android.secure.messaging.CustomRobolectricTestRunner;
import com.android.secure.messaging.Home;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Created by christophershirley on 9/17/16.
 */
@RunWith(CustomRobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class NFCHandlerTest {

    Activity homeActivity;
    NFCHandler nfcHandler;
    NfcAdapter nfcAdapter;
    @Before
    public void setUp() throws Exception {
        homeActivity = Robolectric.setupActivity(Home.class);
        nfcAdapter = NfcAdapter.getDefaultAdapter(homeActivity);
        nfcHandler = new NFCHandler(homeActivity.getApplicationContext(),nfcAdapter);
    }

    @Test
    public void deviceHasNFC() throws Exception {

        TestCase.assertTrue(nfcHandler.deviceHasNFC());
    }



}