package com.android.secure.messaging.database;

/**
 * Created by cjs07f on 10/2/16.
 */
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import android.content.ContentValues;

import com.android.secure.messaging.R;
import com.android.secure.messaging.contacts.Contact;
import com.android.secure.messaging.contacts.ContactHandler;
import com.android.secure.messaging.contacts.ContactsActivity;
import com.android.secure.messaging.messaging.MThreadHandler;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog.Builder;
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
    public DAO(){};
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
        values.put(MThreadHandler.NAME, name);
        long id = database.insert(MThreadHandler.TABLE_THREADS, null,
                values);
        close();
        if(id==-1)
            return false;
        else
            return true;
    }

    public boolean deleteThread(String name)
    {
        if(database==null)
            open();
        ContentValues values = new ContentValues();
        values.put(MThreadHandler.NAME, name);
        int id = database.delete(MThreadHandler.TABLE_THREADS, MThreadHandler.NAME+ " = ?", new String[] {name});
        close();
        if(id==-1)
            return false;
        else
            return true;

    }

    public List<String> getAllThreads() {
        if(database==null)
            open();
        Cursor cursor = database.rawQuery("SELECT  * FROM " + MThreadHandler.TABLE_THREADS, null);
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
        return databaseCursor.getString(databaseCursor.getColumnIndexOrThrow(MThreadHandler.NAME));
    }

 /*   private void alertView( String message ) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setTitle( "Warning" )
              //  .setIcon(R.drawable.ic_launcher)
                .setMessage(message)
//  .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//      public void onClick(DialogInterface dialoginterface, int i) {
//          dialoginterface.cancel();
//          }})
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                    }
                }).show();}     */


    public void updateThreadsDatabase(String newCon, String oldCon)
    {
        if(database==null)
            open();
        ContentValues values = new ContentValues();
        values.put(MThreadHandler.NAME, newCon);

        Cursor cursorExist = null;

        cursorExist = database.rawQuery("SELECT  * FROM " + MThreadHandler.TABLE_THREADS + " where NAME = ?",new String[]{newCon});

        //getActivity()

        if(cursorExist.getCount()  > 0){

            /// Toast.makeText(getApplicationContext(), "The Name is already in use!!!", Toast.LENGTH_LONG).show();
            close();
        }
        else {
            database.update(MThreadHandler.TABLE_THREADS, values, MThreadHandler.NAME + " = ?", new String[] {oldCon});


            close();

        }
    }


    public void updateContactDatabase(String newCon, String oldCon)
    {
        if(database==null)
           open();
        ContentValues values = new ContentValues();
        values.put(ContactHandler.NAME, newCon);

        Cursor cursorExist = null;

        cursorExist = database.rawQuery("SELECT  * FROM " + ContactHandler.TABLE_CONTACTS + " where NAME = ?",new String[]{newCon});

        //getActivity()

        if(cursorExist.getCount()  > 0){

           /// Toast.makeText(getApplicationContext(), "The Name is already in use!!!", Toast.LENGTH_LONG).show();
            close();
        }
        else {
            database.update(ContactHandler.TABLE_CONTACTS, values, ContactHandler.NAME + " = ?", new String[] {oldCon});


            close();

        }
    }

    public void deleteContactDatabase(String deleteName)
    {
        if(database==null)
            open();
        ContentValues values = new ContentValues();
        values.put(ContactHandler.NAME, deleteName);

        database.delete(ContactHandler.TABLE_CONTACTS, ContactHandler.NAME + " = ?", new String[] {deleteName});

        close();
    }
}
