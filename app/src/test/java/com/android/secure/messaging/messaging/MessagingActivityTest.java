package com.android.secure.messaging.messaging;

import android.app.Activity;
import android.content.Intent;

import com.android.secure.messaging.BuildConfig;
import com.android.secure.messaging.CustomRobolectricTestRunner;
import com.android.secure.messaging.Home;
import com.android.secure.messaging.contacts.Contact;
import com.google.gson.Gson;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by christophershirley on 11/27/16.
 */
@RunWith(CustomRobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MessagingActivityTest {

    private Activity homeActivity;
    private Intent intent;
    @Before
    public void setUp() throws Exception {
        homeActivity = Robolectric.setupActivity(Home.class);
        intent = new Intent(homeActivity, MessagingActivity.class);
        List<Contact> contacts = createContacts(4);
        intent.putExtra("Contact", new Gson().toJson(contacts.get(2)));


    }

    public List<Contact> createContacts(int numberOfContactsToCreate)
    {
        List<Contact> testContacts = new ArrayList<>();
        for(int count=0; count<numberOfContactsToCreate; count++)
        {
            Contact testContact = new Contact("testName"+count,
                    "testEmail"+count,"testKey"+count);
            testContacts.add(testContact);
        }

        return testContacts;
    }

    @Test
    public void getContact(){
        MessagingActivity messagingActivity = new MessagingActivity();
        Contact contact = messagingActivity.getContact(intent.getExtras());
        TestCase.assertEquals(contact.getName(),"testName2");
        TestCase.assertEquals(contact.getEmail(),"testEmail2");
        TestCase.assertEquals(contact.getKey(),"testKey2");
    }
}