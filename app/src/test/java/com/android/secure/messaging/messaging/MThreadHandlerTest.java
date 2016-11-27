package com.android.secure.messaging.messaging;

import android.app.Activity;

import com.android.secure.messaging.BuildConfig;
import com.android.secure.messaging.CustomRobolectricTestRunner;
import com.android.secure.messaging.Home;
import com.android.secure.messaging.contacts.Contact;

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
 * Created by christophershirley on 11/26/16.
 */
@RunWith(CustomRobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MThreadHandlerTest {

    Activity homeActivity = Robolectric.setupActivity(Home.class);
    MThreadHandler mThreadHandler;

    @Before
    public void setUp() throws Exception {

        mThreadHandler = new MThreadHandler(homeActivity);


    }

    @Test
    public void getContact() throws Exception {
        List<Contact> contacts = createContacts(5);
        Contact result = mThreadHandler.getContact("testName4",contacts);
        TestCase.assertEquals(result.getName(),"testName4");
        TestCase.assertEquals(result.getEmail(),"testEmail4");
        TestCase.assertEquals(result.getKey(),"testKey4");
    }

    @Test
    public void getAllThreads() throws Exception {
        int numberOfThreads = 4;
        addTestThreads(numberOfThreads);
        List<String> threads = mThreadHandler.getAllThreads();

        TestCase.assertTrue(threads.size()==numberOfThreads);
    }

    public void addTestThreads(int numberOfThreadsToCreate)
    {
        for(int count=0; count<numberOfThreadsToCreate; count++)
             mThreadHandler.saveThread("test"+count);

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



}