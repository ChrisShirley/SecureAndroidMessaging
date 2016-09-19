package com.android.secure.messaging.contacts;

import android.os.Bundle;



import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by christophershirley on 9/17/16.
 */
public class ContactTest {

    Contact contact;

    @Before
    public void setUp() throws Exception {
        contact = new Contact(new Bundle());


    }

    @Test
    public void getEmail() throws Exception {

        TestCase.assertTrue(contact.getEmail()!=null);
    }

    @Test
    public void getName() throws Exception {

        TestCase.assertTrue(contact.getName()!=null);
    }

    @Test
    public void getKey() throws Exception {

        TestCase.assertTrue(contact.getKey()!=null);
    }


}