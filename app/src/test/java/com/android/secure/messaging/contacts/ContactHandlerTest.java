package com.android.secure.messaging.contacts;

import android.os.Bundle;



import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by christophershirley on 9/17/16.
 */
public class ContactHandlerTest {

    ContactHandler contactHandler;

    @Before
    public void setUp() throws Exception {
        contactHandler= new ContactHandler();


    }

    @Test
    public void getContact() throws Exception {

        TestCase.assertTrue(contactHandler.getContact(null));
    }

    @Test
    public void accept() throws Exception {

        TestCase.assertTrue(contactHandler.saveContact(new Bundle()));
    }



}

