package com.android.secure.messaging.contacts;

import android.content.Context;
import android.os.Bundle;

import com.android.secure.messaging.database.DAO;

import java.util.List;

/**
 * Created by christophershirley on 9/18/16.
 */
public class ContactHandler {
    private  static DAO dao;
    public ContactHandler(Context context)
    {
        dao = new DAO(context);
    }

    public boolean saveContact(String name, String email, String key)
    {
        Contact contact = new Contact(name,email,key);
        return dao.saveContact(contact);

    }

    boolean getContact(String name)
    {
        return false;
    }

    public List<Contact> getAllContacts()
    {

       List<Contact> contacts =  dao.getAllContacts();
        return contacts;
    }

    public static DAO getDAO(){return dao;}
}

