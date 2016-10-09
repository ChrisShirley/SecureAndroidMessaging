package com.android.secure.messaging.database;

/**
 * Created by cjs07f on 10/2/16.
 */
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.android.secure.messaging.contacts.Contact;

/**
 * @author Chris J. Shirley
 *
 */
public class DAO {

    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private static Context context;
    //private String[] allColumns = { DatabaseHelper.ID,DatabaseHelper.MOB };

    public DAO(Context c) {
        dbHelper = new DatabaseHelper(c);
        context = c;

    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }



    public void deleteContact(String email,Contact contact)
    {

        Cursor cursor = database.rawQuery("SELECT "+ DatabaseHelper.EMAIL +" FROM "+ DatabaseHelper.TABLE_CONTACTS + " WHERE email= '"+email+"'", null);
        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            String id = cursor.getString(0);
            database.delete(DatabaseHelper.TABLE_CONTACTS, DatabaseHelper.CONTACT_ID +" = '"+contact.getKey()+"'" , null);
        }
    }

    public boolean saveContact(Contact contact)
    {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.NAME, contact.getName());
        values.put(DatabaseHelper.EMAIL, contact.getEmail());
        values.put(DatabaseHelper.PKEY, contact.getKey());

        long id = database.insert(DatabaseHelper.TABLE_CONTACTS, null,
                values);
        close();
        if(id==-1)
            return false;
        else
            return true;


    }



    public List<Contact> getAllContacts(String id)
    {
        List<Contact> contacts = new ArrayList<Contact>();
        if(database==null)
            database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT  * FROM " +DatabaseHelper.TABLE_CONTACTS, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Contact contact = cursorToContact(cursor);
            contacts.add(contact);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return contacts;
    }

    private Contact cursorToContact(Cursor databaseCursor) {


        String id =databaseCursor.getString(1);
        Contact contact = new Contact();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor phoneCursor = contentResolver.query(Data.CONTENT_URI,
                new String[] { Data._ID, Data.DISPLAY_NAME, Phone.NUMBER,
                        Data.CONTACT_ID, Phone.TYPE, Phone.LABEL },
                Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'", null,
                Data._ID);

        phoneCursor.moveToFirst();

        String[] columnNames = phoneCursor.getColumnNames();
        int displayNameColIndex = phoneCursor.getColumnIndex("display_name");
        int idColIndex = phoneCursor.getColumnIndex("_id");
        int col2Index = phoneCursor.getColumnIndex(columnNames[2]);
        int col3Index = phoneCursor.getColumnIndex(columnNames[3]);
        int count = phoneCursor.getCount();
        for(int cursorPosition=0;cursorPosition<count;cursorPosition++)
        {
            if(phoneCursor.getInt(col3Index)==Integer.parseInt(id))
            {
                String displayName = phoneCursor.getString(displayNameColIndex);
                String phoneNumber = phoneCursor.getString(col2Index);
                int contactId = phoneCursor.getInt(col3Index);


                break;
            }
            phoneCursor.moveToNext();
        }

        return contact;
    }



}
