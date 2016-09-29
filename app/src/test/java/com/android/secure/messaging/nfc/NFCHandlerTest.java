package com.android.secure.messaging.nfc;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by christophershirley on 9/17/16.
 */
public class NFCHandlerTest {

    NFCHandler nfcHandler;
    @Before
    public void setUp() throws Exception {
        //nfcHandler = new NFCHandler();
    }

    @Test
    public void deviceHasNFC() throws Exception {

        TestCase.assertTrue(nfcHandler.deviceHasNFC());
    }

    @Test
    public void accept() throws Exception {

        TestCase.assertTrue(nfcHandler.accept());
    }

    @Test
    public void request() throws Exception {

        TestCase.assertTrue(nfcHandler.request());
    }

}