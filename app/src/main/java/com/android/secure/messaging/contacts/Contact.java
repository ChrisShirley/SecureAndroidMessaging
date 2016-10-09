package com.android.secure.messaging.contacts;

import android.os.Bundle;

/**
 * Created by christophershirley on 9/18/16.
 */
public class Contact {

    private final String NAME_KEY = "name";
    private final String EMAIL_KEY = "email";
    private final String PUBLIC_KEY = "pkey";

    private String name;
    private String email;
    private String key;

    public Contact()
    {

    }

    /*public Contact(Bundle bundle)
    {
        name = bundle.getString(NAME_KEY);
        email = bundle.getString(EMAIL_KEY);
        key = bundle.getString(PUBLIC_KEY);

    }*/

    public Contact (String n, String e, String k)
    {
        name = n;
        email = e;
        key = k;
    }


    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public String getKey()
    {
        return key;
    }

}

