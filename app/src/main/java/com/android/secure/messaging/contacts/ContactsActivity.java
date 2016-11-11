package com.android.secure.messaging.contacts;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.nfc.NdefMessage;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.android.secure.messaging.R;
import com.android.secure.messaging.database.DAO;
import com.android.secure.messaging.keys.Encrypt;
import android.widget.TextView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ImageView;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import com.android.secure.messaging.contacts.ContactHandler;

public class ContactsActivity extends AppCompatActivity{

    private List<Contact> contactList;
    private ArrayAdapter<String> adapter;
    private  EditText inputUpdate;
    private String nameToBeUpdated;
    private ContactHandler contactUpdateHandler1;
    private AdapterView adapterView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addContacts();


    }

    private void addContacts()
    {
        contactList =  new ContactHandler(this).getAllContacts();
     final ListView myListView = (ListView) this.findViewById(R.id.display_contacts);
        ArrayList<String> displayContactList = new ArrayList<>();

        if(contactList.isEmpty())
            Toast.makeText(this,"No contacts",Toast.LENGTH_LONG).show();

        for(int contactInList = 0; contactInList<contactList.size(); contactInList++)
            displayContactList.add(contactList.get(contactInList).getName());
        adapter =   new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,android.R.id.text1, displayContactList);

  /* Parameters
    parent:     The AdapterView where the click happened.
    view:       The view within the AdapterView that was clicked (this will be a view provided by the adapter)
    position:   The position of the view in the adapter.
    id:         The row id of the item that was clicked. */
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myListView.getChildAt(position);
                TextView textView = (TextView) myListView.getAdapter().getView(position, null, myListView);
                Log.i("item", textView.getText().toString());
                nameToBeUpdated = textView.getText().toString();
                        updateContactDialog(textView.getText().toString());
            }
        });

        myListView.setAdapter(adapter);

    }


    DialogInterface.OnClickListener updateClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    if(inputUpdate.getText().length()==0) {
                        Toast.makeText(getApplicationContext(), "Contact must have a name in order to be saved. Please name your contact or cancel.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Name: " + inputUpdate.getText(), Toast.LENGTH_LONG).show();

                    }
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };


    public void updateContactDialog(String oldNameContact)
    {
        AlertDialog.Builder updateBuilder = new AlertDialog.Builder(this);
        updateBuilder.setTitle("Update Contact");

        // Set an EditText view to get user input
        inputUpdate = new EditText(this);
        updateBuilder.setView(inputUpdate);

        updateBuilder.setMessage("Please update the common name of the contact").setPositiveButton("Finished", updateClickListener);
        updateBuilder.setNeutralButton("Delete Contact", updateClickListener);
        updateBuilder.setNegativeButton("Cancel", updateClickListener);

        final AlertDialog contactDialog = updateBuilder.create();
        contactDialog.show();

        contactDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = (inputUpdate.getText().toString().trim().isEmpty());
                // if EditText is empty disable closing on possitive button
                if (!wantToCloseDialog) {
                    ContactHandler testupdate = new ContactHandler(getApplicationContext());
                    testupdate.updateContact(inputUpdate.getText().toString(), nameToBeUpdated);
                    contactDialog.dismiss();
                    addContacts();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Contact must have a name in order to be saved. Please name your contact or cancel.", Toast.LENGTH_LONG).show();
                }
            }
        });
        contactDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactHandler testupdate = new ContactHandler(getApplicationContext());
                testupdate.deleteContact(nameToBeUpdated);
                contactDialog.dismiss();
                addContacts();
            }
        });
        contactDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactDialog.dismiss();
            }
        });

    }

}
