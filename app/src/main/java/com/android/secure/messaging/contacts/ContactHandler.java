package com.android.secure.messaging.contacts;

import android.os.Bundle;

/**
 * Created by christophershirley on 9/18/16.
 */
public class ContactHandler {

    public ContactHandler()
    {

    }

    public boolean saveContact(Bundle bundle)
    {
        Contact contact = new Contact(bundle);
        return false;
    }

    boolean getContact(String name)
    {
        return false;
    }
}

