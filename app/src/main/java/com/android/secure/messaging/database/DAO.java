package com.android.secure.messaging.database;

/**
 * Created by cjs07f on 10/2/16.
 */
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


import com.android.secure.messaging.contacts.Contact;
import com.android.secure.messaging.contacts.ContactHandler;
import com.android.secure.messaging.messaging.MessagingThreadHandler;

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

    public DAO(Context c,String databaseName,int databaseVersion,String databaseSetupString) {
        dbHelper = new DatabaseHelper(c,databaseName,databaseVersion);
        context = c;
        dbHelper.setupDatabase(databaseSetupString);


    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
        database=null;
    }



    public void deleteContact(String email,Contact contact)
    {
        /*Cursor cursor = database.rawQuery("SELECT "+ ContactHandler.EMAIL +" FROM "+ ContactHandler.TABLE_CONTACTS + " WHERE email= '"+email+"'", null);
        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            String id = cursor.getString(0);
            database.delete(ContactHandler.TABLE_CONTACTS, ContactHandler.CONTACT_ID +" = '"+contact.getKey()+"'" , null);
        }*/
    }

    public boolean saveContact(Contact contact)
    {
        if(database==null)
            open();
        ContentValues values = new ContentValues();
        values.put(ContactHandler.NAME, contact.getName());
        values.put(ContactHandler.EMAIL, contact.getEmail());
        values.put(ContactHandler.PKEY, contact.getKey());

        long id = database.insert(ContactHandler.TABLE_CONTACTS, null,
                values);
        close();

        if(id==-1)
            return false;
        else
            return true;


    }

    public boolean saveThread(String name)
    {
        if(database==null)
            open();
        ContentValues values = new ContentValues();
        values.put(MessagingThreadHandler.NAME, name);
        long id = database.insert(MessagingThreadHandler.TABLE_THREADS, null,
                values);
        if(id==-1)
            return false;
        else
            return true;
    }

    public List<String> getAllThreads() {
        if(database==null)
            open();
        Cursor cursor = database.rawQuery("SELECT  * FROM " + MessagingThreadHandler.TABLE_THREADS, null);
        List<String> threads = new ArrayList<>();;
        String threadName;
        if(cursor.moveToFirst()) {

            do {
                threadName = cursorToName(cursor);
                threads.add(threadName);
            }
            while(cursor.moveToNext());
        }
        // Make sure to close the cursor
        cursor.close();
        close();
        return threads;
    }


    public List<Contact> getAllContacts()
    {

        List<Contact> contacts = new ArrayList<Contact>();
        if(database==null)
            open();
        Cursor cursor = database.rawQuery("SELECT  * FROM " + ContactHandler.TABLE_CONTACTS, null);

        if(cursor.moveToFirst()) {
            Contact contact =null;
            do {
                contact = cursorToContact(cursor);
                contacts.add(contact);
            }
            while(cursor.moveToNext());
        }
        // Make sure to close the cursor
        cursor.close();
        close();

        return contacts;
    }

    private Contact cursorToContact(Cursor databaseCursor) {


        String name = databaseCursor.getString(databaseCursor.getColumnIndexOrThrow(ContactHandler.NAME));
        String email = databaseCursor.getString(databaseCursor.getColumnIndexOrThrow(ContactHandler.EMAIL));
        String publicKey = databaseCursor.getString(databaseCursor.getColumnIndexOrThrow(ContactHandler.PKEY));

        return new Contact(name,email,publicKey);


    }

    private String cursorToName(Cursor databaseCursor)
    {
        return databaseCursor.getString(databaseCursor.getColumnIndexOrThrow(MessagingThreadHandler.NAME));
    }



}
