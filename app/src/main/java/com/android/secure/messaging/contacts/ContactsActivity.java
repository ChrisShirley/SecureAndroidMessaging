package com.android.secure.messaging.contacts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.android.secure.messaging.R;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private List<Contact> contactList;
    private ArrayAdapter<String> adapter;
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
        contactList = ContactHandler.getDAO().getAllContacts();
        ListView myListView = (ListView) this.findViewById(R.id.display_contacts);
        ArrayList<String> displayContactList = new ArrayList<>();

        if(contactList.isEmpty())
            Toast.makeText(this,"No contacts",Toast.LENGTH_LONG).show();

        for(int contactInList = 0; contactInList<contactList.size(); contactInList++)
            displayContactList.add(contactList.get(contactInList).getName());
        adapter =   new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,android.R.id.text1, displayContactList);
        myListView.setAdapter(adapter);

    }
}
