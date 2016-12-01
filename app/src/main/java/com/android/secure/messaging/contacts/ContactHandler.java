package com.android.secure.messaging.contacts;

import android.content.Context;

import com.android.secure.messaging.database.DAO;
import com.android.secure.messaging.messaging.MThreadHandler;

import android.util.Log;
import java.util.List;

/**
 * Created by christophershirley on 9/18/16.
 */
public class ContactHandler {
    private  DAO dao;
    public static final String TABLE_CONTACTS = "contacts";

    //public static final String CONTACT_ID ="default_id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PKEY = "pkey";


    public static final String DATABASE_NAME = "contacts.db";
    public static final int DATABASE_VERSION = 1;

    private static final String CONTACT_TABLE_CREATE = "create table "
            + TABLE_CONTACTS + "(" +
            NAME + " text not null, "+
            EMAIL +" text not null, "+
            PKEY +" text PRIMARY KEY not null);";

    private MThreadHandler threadHandler;

    public ContactHandler(Context context,MThreadHandler mThreadHandler)
    {
        getDAO(context);
        threadHandler = mThreadHandler;
    }

    public ContactHandler(Context context)
    {
        getDAO(context);
    }

    private void getDAO(Context context)
    {
        dao = new DAO(context,DATABASE_NAME,DATABASE_VERSION,CONTACT_TABLE_CREATE);
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

    public  List<Contact> getAllContacts()
    {

       List<Contact> contacts =  dao.getAllContacts();
        return contacts;
    }

   public void updateContact(String newName, String oldName)
    {
        dao.updateContactDatabase(newName, oldName);
        threadHandler.updateThreadName(newName,oldName);

    }

    public void deleteContact(String deleteName)
    {
        dao.deleteContactDatabase(deleteName);
        threadHandler.deleteThread(deleteName);

    }

}

