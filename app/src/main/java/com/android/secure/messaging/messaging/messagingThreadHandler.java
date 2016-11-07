package com.android.secure.messaging.messaging;

import android.content.Context;

import com.android.secure.messaging.contacts.Contact;
import com.android.secure.messaging.database.DAO;

import java.util.List;

/**
 * Created by cjs07f on 10/27/16.
 */

public class MessagingThreadHandler {
    private  static DAO dao;
    public static final String TABLE_THREADS = "threads";

    //public static final String THREAD_ID ="default_id";
    public static final String NAME = "name";



    public static final String DATABASE_NAME = TABLE_THREADS+".db";
    public static final int DATABASE_VERSION = 1;

    private static final String THREAD_TABLE_CREATE = "create table "
            + TABLE_THREADS + "(" +
            NAME + " text PRIMARY KEY);";

    public MessagingThreadHandler(Context context)
    {
        dao = new DAO(context,DATABASE_NAME,DATABASE_VERSION,THREAD_TABLE_CREATE);
    }



    public Contact getContact(String contactName, List<Contact> contacts)
    {
        for (Contact c : contacts) {
            if (contactName.equals(c.getName())) {
                return  c;
            }
        }
        return null;
    }

    public List<String> getAllThreads()
    {

        List<String> contacts =  dao.getAllThreads();
        return contacts;
    }

    public void saveThread(String name){
        dao.saveThread(name);
    }
}
