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
    public void getAllContacts() {
        // Create multiple local contacts for name, email, and key strings
        String uName0 = "SAM0", uEmail0 = "sam0@sam.com", uKey0 = "testKey0";
        String uName1 = "SAM1", uEmail1 = "sam1@sam.com", uKey1 = "testKey1";
        // Create a local contact objects
        Contact uLocalContact0 = new Contact(uName0, uEmail0 , uKey0);
        Contact uLocalContact1 = new Contact(uName1, uEmail1 , uKey1);
        // ensure at least two local contacts can be saved into the database
        contactHandler.saveContact(uName0, uEmail0, uKey0);
        contactHandler.saveContact(uName1, uEmail1, uKey1);

        // get the list of saved contacts
        List<Contact> uContacts = contactHandler.getAllContacts();

        // compare the first contact object values in the list
        TestCase.assertEquals(uContacts.get(0).getName(), uLocalContact0.getName());
        TestCase.assertEquals(uContacts.get(0).getEmail(), uLocalContact0.getEmail());
        TestCase.assertEquals(uContacts.get(0).getKey(), uLocalContact0.getKey());
        // compare the second contact object values in the list
        TestCase.assertEquals(uContacts.get(1).getName(), uLocalContact1.getName());
        TestCase.assertEquals(uContacts.get(1).getEmail(), uLocalContact1.getEmail());
        TestCase.assertEquals(uContacts.get(1).getKey(), uLocalContact1.getKey());
        // ensure the two object in the contact list are not equal
        TestCase.assertNotSame(uContacts.get(0).getName(), uContacts.get(1).getName());
        TestCase.assertNotSame(uContacts.get(0).getEmail(), uContacts.get(1).getEmail());
        TestCase.assertNotSame(uContacts.get(0).getKey(), uContacts.get(1).getKey());
    }

    @Test
    public void saveContact(){
        // Create multiple local contacts for name, email, and key strings
        String uName = "SAM", uEmail = "sam@sam.com", uKey = "testKey";
        // verify saveContact returns true for a successful save into the database
        TestCase.assertTrue(contactHandler.saveContact( uName, uEmail, uKey));
        // verify saveContact returns false for an unsuccessful save attempt into the database
        TestCase.assertFalse(contactHandler.saveContact( null, uEmail, uKey));
        TestCase.assertFalse(contactHandler.saveContact(uName, null,   uKey));
        TestCase.assertFalse(contactHandler.saveContact(uName, uEmail, null));

    }

    @Test
    public void updateContact() {
        // Create multiple local contacts for name, email, and key strings
        String uName0 = "SAM0", uEmail0 = "sam0@sam.com", uKey0 = "testKey0";
        String uName1 = "SAM1", uEmail1 = "sam1@sam.com", uKey1 = "testKey1";
        String uName2 = "SAM2", uEmail2 = "sam2@sam.com", uKey2 = "testKey2";
        // Create a local contact objects
        Contact uLocalContact0 = new Contact(uName0, uEmail0 , uKey0);
        Contact uLocalContact1 = new Contact(uName1, uEmail1 , uKey1);
        Contact uLocalContact2 = new Contact(uName2, uEmail2 , uKey2);
        // ensure at least two local contacts can be saved into the database
        contactHandler.saveContact(uName0, uEmail0, uKey0);
        contactHandler.saveContact(uName1, uEmail1, uKey1);
        // get the list of saved contacts
        List<Contact> uContacts = contactHandler.getAllContacts();
        // compare the first contact object values in the list
        TestCase.assertEquals(uContacts.get(0).getName(), uLocalContact0.getName());
        TestCase.assertEquals(uContacts.get(0).getEmail(), uLocalContact0.getEmail());
        TestCase.assertEquals(uContacts.get(0).getKey(), uLocalContact0.getKey());
        // compare the second contact object values in the list
        TestCase.assertEquals(uContacts.get(1).getName(), uLocalContact1.getName());
        TestCase.assertEquals(uContacts.get(1).getEmail(), uLocalContact1.getEmail());
        TestCase.assertEquals(uContacts.get(1).getKey(), uLocalContact1.getKey());
        // ensure the two object in the contact list are not equal
        TestCase.assertNotSame(uContacts.get(0).getName(), uContacts.get(1).getName());
        TestCase.assertNotSame(uContacts.get(0).getEmail(), uContacts.get(1).getEmail());
        TestCase.assertNotSame(uContacts.get(0).getKey(), uContacts.get(1).getKey());
        // update local contact 0 (oldName) with local contact 2 (newName) to the database
        contactHandler.updateContact(uName2, uName0);
        // get the list of saved contacts
        List<Contact> uContacts1 = contactHandler.getAllContacts();
        // compare the first contact object values in the list are updated
        TestCase.assertEquals(uContacts1.get(0).getName(), uLocalContact2.getName());
        // email and key do not zero out they stay the same when the name is updated
        TestCase.assertEquals(uContacts1.get(0).getEmail(), uLocalContact0.getEmail());
        TestCase.assertEquals(uContacts1.get(0).getKey(), uLocalContact0.getKey());
        // compare the second contact object values in the list
        TestCase.assertEquals(uContacts1.get(1).getName(), uLocalContact1.getName());
        TestCase.assertEquals(uContacts1.get(1).getEmail(), uLocalContact1.getEmail());
        TestCase.assertEquals(uContacts1.get(1).getKey(), uLocalContact1.getKey());
        // ensure the two object in the contact list are not equal
        TestCase.assertNotSame(uContacts1.get(0).getName(), uContacts.get(1).getName());
        TestCase.assertNotSame(uContacts1.get(0).getEmail(), uContacts.get(1).getEmail());
        TestCase.assertNotSame(uContacts1.get(0).getKey(), uContacts.get(1).getKey());


    }

    @Test
    public void deleteContact() {
        // Create multiple local contacts for name, email, and key strings
        String uName0 = "SAM0", uEmail0 = "sam0@sam.com", uKey0 = "testKey0";
        String uName1 = "SAM1", uEmail1 = "sam1@sam.com", uKey1 = "testKey1";
        String uName2 = "SAM2", uEmail2 = "sam2@sam.com", uKey2 = "testKey2";
        // Create a local contact objects
        Contact uLocalContact0 = new Contact(uName0, uEmail0 , uKey0);
        Contact uLocalContact1 = new Contact(uName1, uEmail1 , uKey1);
        Contact uLocalContact2 = new Contact(uName2, uEmail2 , uKey2);
        // ensure at least two local contacts can be saved into the database
        contactHandler.saveContact(uName0, uEmail0, uKey0);
        contactHandler.saveContact(uName1, uEmail1, uKey1);
        contactHandler.saveContact(uName2, uEmail2, uKey2);
        // get the list of saved contacts
        List<Contact> uContacts = contactHandler.getAllContacts();
        // compare the first contact object values in the list
        TestCase.assertEquals(uContacts.get(0).getName(), uLocalContact0.getName());
        TestCase.assertEquals(uContacts.get(0).getEmail(), uLocalContact0.getEmail());
        TestCase.assertEquals(uContacts.get(0).getKey(), uLocalContact0.getKey());
        // compare the second contact object values in the list
        TestCase.assertEquals(uContacts.get(1).getName(), uLocalContact1.getName());
        TestCase.assertEquals(uContacts.get(1).getEmail(), uLocalContact1.getEmail());
        TestCase.assertEquals(uContacts.get(1).getKey(), uLocalContact1.getKey());
        // compare the third contact object values in the list
        TestCase.assertEquals(uContacts.get(1).getName(), uLocalContact1.getName());
        TestCase.assertEquals(uContacts.get(1).getEmail(), uLocalContact1.getEmail());
        TestCase.assertEquals(uContacts.get(1).getKey(), uLocalContact1.getKey());

        // delete contact 0 in the the database, ensure list data shifts up
        contactHandler.deleteContact(uName0);
        // get the list of saved contacts
        List<Contact> uContacts1 = contactHandler.getAllContacts();
        // ensure the first contact object values in the list is removed, old second object is now first
        TestCase.assertEquals(uContacts1.get(0).getName(), uLocalContact1.getName());
        TestCase.assertEquals(uContacts1.get(0).getEmail(), uLocalContact1.getEmail());
        TestCase.assertEquals(uContacts1.get(0).getKey(), uLocalContact1.getKey());
        // ensure the old third contact object values in the list is now second
        TestCase.assertEquals(uContacts1.get(1).getName(), uLocalContact2.getName());
        TestCase.assertEquals(uContacts1.get(1).getEmail(), uLocalContact2.getEmail());
        TestCase.assertEquals(uContacts1.get(1).getKey(), uLocalContact2.getKey());

    }

}

