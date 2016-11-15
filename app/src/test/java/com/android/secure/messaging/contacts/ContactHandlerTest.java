package com.android.secure.messaging.contacts;

import android.app.Activity;
import android.os.Bundle;
import android.test.mock.MockContext;

import com.android.secure.messaging.BuildConfig;
import com.android.secure.messaging.CustomRobolectricTestRunner;
import com.android.secure.messaging.Home;

import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import android.content.Context;


import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.annotation.Config;

import java.util.List;

/**
 * Created by christophershirley on 9/17/16.
 */
@RunWith(CustomRobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ContactHandlerTest {

    private Activity contactsActivity;
    private ContactHandler contactHandler;

    @Before
    public void setUp() throws Exception {
        contactsActivity = Robolectric.setupActivity(ContactsActivity.class);

        contactHandler = new ContactHandler(contactsActivity.getApplicationContext());

    }

    @Test
    public void getContact() throws Exception {

        //TestCase.assertTrue(!contactHandler.getContact(null));
    }

    @Test
    public void getAllContacts() {


    }

    @Test
    public void saveContact(){
        TestCase.assertTrue(contactHandler.saveContact("SAM", "sam12345@sam.com", "testKey"));
        TestCase.assertFalse(contactHandler.saveContact(null, "sam12345@sam.com", "testKey"));
        TestCase.assertFalse(contactHandler.saveContact("SAM", null, "testKey"));
        TestCase.assertFalse(contactHandler.saveContact("SAM", "sam12345@sam.com", null));

    }

    @Test
    public void updateContact() {

    }

    @Test
    public void deleteContact() {

    }

}

