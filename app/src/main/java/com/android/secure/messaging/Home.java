package com.android.secure.messaging;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.secure.messaging.Preferences.Preferences;
import com.android.secure.messaging.Preferences.PreferencesHandler;
import com.android.secure.messaging.contacts.Contact;
import com.android.secure.messaging.contacts.ContactHandler;
import com.android.secure.messaging.contacts.ContactsActivity;
import com.android.secure.messaging.email.EmailHandler;
import com.android.secure.messaging.email.EmailListener;
import com.android.secure.messaging.keys.Encrypt;
import com.android.secure.messaging.keys.Keys;
import com.android.secure.messaging.messaging.MessagingActivity;
import com.android.secure.messaging.messaging.MThreadHandler;
import com.android.secure.messaging.nfc.NFCHandler;
import com.google.gson.Gson;
import android.view.WindowManager;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;
import java.io.*;


@SuppressWarnings("deprecation")
public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NfcAdapter.CreateNdefMessageCallback {

    private static Keys keys;
    private  NFCHandler nfcHandler;
    private Context context;
    private static NfcAdapter mNfcAdapter;
    private static ContactHandler contactHandler;
    private static MThreadHandler mThreadHandler;
    private final Preferences preferencesHandler = new PreferencesHandler();
    private static List<Contact> contacts;
    private static List<String> messagingThreads;
    public static boolean threadChange = false;
    private  ListView myListView;
    private ArrayAdapter<String> threadAdapter;
    private static Encrypt encrypt;
    //private EmailHandler emailHandler;

    private NdefMessage msg;
    private  EditText input;

                                /*Activity Overrides*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_new_msg);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMessageDialog();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        context = getApplicationContext();
        keys = Keys.getInstance();
        keys.getKeys(this.getApplicationContext());

        encrypt = new Encrypt(keys.getPublicKey());


        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcHandler = new NFCHandler(this,mNfcAdapter);
        if(nfcHandler.deviceHasNFC())
            mNfcAdapter.setNdefPushMessageCallback(this, this);


        contactHandler = new ContactHandler(this);
        mThreadHandler = new MThreadHandler(this);
        messagingThreads = mThreadHandler.getAllThreads();
        if(!messagingThreads.isEmpty())
            addThreads();

        //contactHandler.saveContact("Test Account", "testaccount@secureandroidmessaging.com", "1234451243");
//        contactHandler.saveContact("Ryan" , "lkAsZTaJuDBf@secureandroidmessaging.com", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArjE0bq9UkqU+H6wS24+UgzyC/cxwRkcd" +
//                "kfd/LSHbQRhqqkJrwk/XKz5K2GeuTT9veHsc13LisOR0yKcrY9XFZcPZtiGVDRpybL13zjF++WhD" +
//                "cy8AXwUcYc/69JnXeQcHnllNyQ5Bl+sokBiqRUcmdJpwn3rZKwrDl/tG7LbOa4GpnY+jFTAaY9It" +
//                "VOmKaYZub9IdQs+iALekxbSEaBFYrSVChBIYDH7eIenNy3fx070zBfgLTwxZK2HZi0fpsAqXvh6T" +
//                "khQgk3Se7qTnVQXj4NOJBsh5dJrkjMcrMEePdE7Yhpkf/0pxW0bWnxEGkOveuYVzCoo5yjNCyCBC" +
//                "Op0ThwIDAQAB");

        /*emailHandler = new EmailHandler(context);
       /* ArrayList<Email> emailArrayList = null;
        try {
            emailArrayList = emailHandler.readEmailsFrom("testaccount@secureandroidmessaging.com", "Sweng501#", "PkeIBWWWFm8Q@secureandroidmessaging.com");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
//        emailHandler.read("testaccount@secureandroidmessaging.com", "Sweng501#");
//        while(emailArrayList == null){
//            emailArrayList = emailHandler.getReadEmails();
//        }
        /*if(emailArrayList!=null) {
            for (Email e : emailArrayList) {
                System.out.println("Message To: " + e.getTo());
                System.out.println("Message From: " + e.getFrom());
                System.out.println("Message Message: " + e.getMessage());
                System.out.println("Message Timestamp : " + e.getTimestamp());
            }
        }*/

    }


    @Override
    public void onResume() {
        super.onResume();
        /**/PendingIntent pi = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                0);
        if(mNfcAdapter!=null)
            mNfcAdapter.enableForegroundDispatch(this, pi, null, null);
        if(threadChange)
        {
            threadChange = false;
            threadAdapter = null;
            updateThreads(null);
        }
        // Check to see that the Activity started due to an Android Beam

    }

                                /*Activity Navigation Overrides*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.transfer) {

            startNFCHandler();
        } else if (id == R.id.contacts) {
            Intent intent = new Intent(this, ContactsActivity.class);
            startActivity(intent);
            //Toast.makeText(this,"Contacts Activity",Toast.LENGTH_LONG).show();

        } /*else if (id == R.id.Settings) {

        } else if (id == R.id.nav_help) {


        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

                                /*NFC Overrides and helpers*/

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        //String text = ("Public Key: "+keys.getPublicKeyAsString());
        NdefRecord [] records = {NdefRecord.createApplicationRecord(keys.getPublicKeyAsString())
                ,NdefRecord.createApplicationRecord(preferencesHandler.getPreference(getApplicationContext(),preferencesHandler.getEmailPrefName()))};
        return new NdefMessage( records);

    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            processIntent(intent);
        }
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    void processIntent(Intent intent) {

        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        //Toast.makeText(this,new String(msg.getRecords()[0].getPayload()),Toast.LENGTH_LONG).show();


        createContactDialog();

    }

    public void startNFCHandler()
    {
        if(nfcHandler.deviceHasNFC())
            nfcHandler.isNFCEnabled();
        else {
            nfcHandler.noNFC(this);
            //finish();
            //System.exit(0);
        }
    }


                                    /* Alert Dialog Methods for notifying the user of errors or to give instructions*/
// Nick Altered


    public void createMessageDialog()
    {

        final String  selectContact = "Select Contact";
        //Make new Dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("New Message");
        dialog.setIcon(R.drawable.ic_mail);
        dialog.setMessage("\n").setPositiveButton("Send", null)



                .setNegativeButton("Cancel", null);

        List<String> contactNames = new ArrayList<>();
        contactNames.add(selectContact);
        getContacts();

        if(contacts.isEmpty()) {
            Toast.makeText(context, "No Friends Detected", Toast.LENGTH_LONG).show();
            return;
        }

        for(Contact c : contacts)
            contactNames.add(c.getName());
        Context context = this;
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, contactNames);


        final Spinner sp = new Spinner(this);
        sp.setLayoutParams(new LinearLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.MATCH_PARENT));
        sp.setAdapter(adp);

        final EditText messageBox = new EditText(context);
        messageBox.setHint("Enter Message");

        layout.addView(sp);
        layout.addView(messageBox);

        dialog.setView(layout);


        final AlertDialog messageDialog = dialog.create();
        messageDialog.show();

        messageDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmailHandler emailHandler =  EmailHandler.getInstance(getApplicationContext());
                Contact messagingContact = null;
                String message = messageBox.getText().toString();


                if(sp.getSelectedItem().toString().equals(selectContact)) {
                    Toast.makeText(getApplicationContext(), "Please select a contact", Toast.LENGTH_LONG).show();
                    return;
                }

                else if(messageBox.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter a message", Toast.LENGTH_LONG).show();
                    return;
                }
                else {


                    //Toast.makeText(getApplicationContext(), "Send Message to encryption!", Toast.LENGTH_LONG).show();
                        boolean threadMatch = false;
                        for (Contact c : contacts) {
                            if (sp.getSelectedItem().toString().equals(c.getName())) {
                                messagingContact = c;
                                for(String messagingThread : messagingThreads)
                                    if(messagingContact.getName().equals(messagingThread))
                                        threadMatch = true;
                            }
                        }
                    if((!threadMatch) && (messagingContact!=null))
                        addNewThread(messagingContact.getName());
                    else
                        Toast.makeText(getApplicationContext(), "Thread exists! Open and append thread", Toast.LENGTH_LONG).show();

                    byte[] encryptedMsgForSelf = encrypt.encrypt(message.getBytes());
                    String messageForSelf = null;
                    try {
                        messageForSelf = new String(encryptedMsgForSelf, "ISO-8859-1");
                        System.out.println("Encrypted using ISO standard: " + messageForSelf);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    Encrypt encryptForContact = null;
                    try {
                        encryptForContact = new Encrypt(keys.convertStringToPublicKey(messagingContact.getKey()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    byte[] encryptedMsg = encryptForContact.encrypt(message.getBytes());
                    String messageForContact = null;
                    try {
                        messageForContact = new String(encryptedMsg, "ISO-8859-1");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    emailHandler.send(messagingContact.getEmail(), preferencesHandler.getPreference(getApplicationContext(),
                            preferencesHandler.getEmailPrefName()), preferencesHandler.getPreference(getApplicationContext(),
                            preferencesHandler.getPasswordPrefName()), messageForContact, messageForSelf);
                    startMessagingActivity(messagingContact.getName());
                }
                messageDialog.dismiss();
            }
        });
    }

    public void createContactDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Contact");

        // Set an EditText view to get user input
        input = new EditText(this);
        builder.setView(input);

        builder.setMessage("New Contact Received! Please name your contact.").setPositiveButton("Continue", null)

                .setNegativeButton("Cancel", null);

        final AlertDialog contactDialog = builder.create();
        contactDialog.show();

        contactDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = (input.getText().toString().trim().isEmpty());
                // if EditText is empty disable closing on possitive button
                if (!wantToCloseDialog) {
                    Toast.makeText(getApplicationContext(), "Name: " + input.getText() + " Key: " + new String(msg.getRecords()[0].getPayload()), Toast.LENGTH_LONG).show();
                    contactHandler.saveContact(input.getText().toString(),new String(msg.getRecords()[1].getPayload()), new String(msg.getRecords()[0].getPayload()));
                    contactDialog.dismiss();
                }
                else {

                    Toast.makeText(getApplicationContext(), "Contact must have a name in order to be saved. Please name your contact or cancel.", Toast.LENGTH_LONG).show();
                }
            }
        });

        contactDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    contactDialog.dismiss();
            }
        });

    }















                                   /*Messaging Thread Display Helpers*/
    public void addNewThread(String name)
    {
        mThreadHandler.saveThread(name);
        updateThreads(name);

    }

    public void updateThreads(String name)
    {
        if(threadAdapter==null) {
            messagingThreads = mThreadHandler.getAllThreads();
            addThreads();
        }
        else
            threadAdapter.add(name);
        myListView.invalidateViews();
    }

    public void addThreads() {
        myListView = (ListView) this.findViewById(R.id.display_message_threads);
        threadAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, messagingThreads);
        myListView.setAdapter(threadAdapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View view, int i, long l) {
                startMessagingActivity(threadAdapter.getItem(i));
            }
        });
    }

    protected void startMessagingActivity(String contactName)
    {
        Intent intent = new Intent(context, MessagingActivity.class);
        getContacts();
        intent.putExtra("Contact", new Gson().toJson(mThreadHandler.getContact(contactName,contacts)));
        startActivity(intent);
    }

                                    /*Contacts Getter*/

    protected void getContacts()
    {
        if(contacts!=null)
            contacts.clear();
        contacts = contactHandler.getAllContacts();

    }




}
