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
        database=null;
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
        if(database==null)
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



    public List<Contact> getAllContacts()
    {

        List<Contact> contacts = new ArrayList<Contact>();
        if(database==null)
            open();
        Cursor cursor = database.rawQuery("SELECT  * FROM " +DatabaseHelper.TABLE_CONTACTS, null);

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


        String name = databaseCursor.getString(databaseCursor.getColumnIndexOrThrow(DatabaseHelper.NAME));
        String email = databaseCursor.getString(databaseCursor.getColumnIndexOrThrow(DatabaseHelper.EMAIL));
        String publicKey = databaseCursor.getString(databaseCursor.getColumnIndexOrThrow(DatabaseHelper.PKEY));

        return new Contact(name,email,publicKey);


    }



}
