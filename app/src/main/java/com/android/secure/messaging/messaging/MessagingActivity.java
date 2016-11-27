package com.android.secure.messaging.messaging;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.secure.messaging.Preferences.Preferences;
import com.android.secure.messaging.Preferences.PreferencesHandler;
import com.android.secure.messaging.R;
import com.android.secure.messaging.contacts.Contact;
import com.android.secure.messaging.email.Email;
import com.android.secure.messaging.email.EmailHandler;
import com.android.secure.messaging.email.EmailListener;
import com.android.secure.messaging.keys.Encrypt;
import com.android.secure.messaging.keys.Keys;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;

public class MessagingActivity extends AppCompatActivity {

    private Contact contact;
    private EmailHandler emailHandler;
    private TextView textView;
    private NestedScrollView nestedScrollView;
    private ProgressDialog dialog;
    private List<String> messages = new ArrayList<>();
    private List<String> newMessages = new ArrayList<>();
    private static boolean downloading = false;

    private final Preferences preferencesHandler = new PreferencesHandler();
    private final String CONTACT_EXTRA_NAME = "Contact";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_messaging);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        contact = getContact(getIntent().getExtras());

        toolbar.setTitle(contact.getName());
        setSupportActionBar(toolbar);

        //emailHandler = new EmailHandler(this);
        emailHandler = EmailHandler.getInstance(this);


        textView = (TextView) findViewById(R.id.messaging_text_view);
        nestedScrollView = (NestedScrollView) findViewById(R.id.messaging_scroll_view);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_send);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sendMessage();
            }
        });

        Download(true);
        preferencesHandler.setPreference(this, preferencesHandler.getNewEmailPrefName(), "false");
        /**/ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        emailHandler.checkForMessages(preferencesHandler.getPreference(getApplicationContext(),preferencesHandler.getEmailPrefName())
                                ,preferencesHandler.getPreference(getApplicationContext(),preferencesHandler.getPasswordPrefName()),messages.size());
                       if(preferencesHandler.getPreference(getApplicationContext(),preferencesHandler.getNewEmailPrefName()).contains("true")) {

                           preferencesHandler.setPreference(getApplicationContext(), preferencesHandler.getNewEmailPrefName(), "false");

                           Download(false);
                       }

                    }
                }, 0, 1, TimeUnit.SECONDS);


    }

    private void sendMessage()
    {
        String message = null;
        Encrypt sendMessage = null;
        sendMessage = new Encrypt(Keys.getInstance().getPublicKey());
        EditText input = (EditText) findViewById(R.id.messaging_edit_text);
        if(input.getText().length()==0) {
           // Toast.makeText(getApplicationContext(), "Contact must have a name in order to be saved. Please name your contact or cancel.", Toast.LENGTH_LONG).show();
        }
        else {
            //Toast.makeText(getApplicationContext(), "Name: " + input.getText() + " Key: " + new String(msg.getRecords()[0].getPayload()), Toast.LENGTH_LONG).show();
            message = input.getText().toString();
            byte[] encryptedMsgForSelf = sendMessage.encrypt(message.getBytes());
            String messageForSelf = null;
            try {
                messageForSelf = new String(encryptedMsgForSelf, "ISO-8859-1");
                System.out.println("Encrypted using ISO standard: " + messageForSelf);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Encrypt encryptForContact = null;
            try {
                encryptForContact = new Encrypt(Keys.getInstance().convertStringToPublicKey(contact.getKey()));
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

            emailHandler.send(contact.getEmail(), preferencesHandler.getPreference(getApplicationContext(),
                    preferencesHandler.getEmailPrefName()), preferencesHandler.getPreference(getApplicationContext(),
                    preferencesHandler.getPasswordPrefName()), messageForContact, messageForSelf);

        }
        input.setText("");
    }



    private void Download(boolean load)
    {
        if(!downloading) {
            downloading = true;
            DownloadMessages downloadMessages = new DownloadMessages(load);
            downloadMessages.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private void createLoader()
    {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.show();

    }

    private void updateView()
    {
        String newText = "";
        for(String message : newMessages)
            newText += message +"\n";



        this.textView.append(newText);

        nestedScrollView.post(new Runnable() {
        @Override
        public void run() {
            nestedScrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
        }
        });

        newMessages.clear();


    }

    private void clearLoader()
    {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    protected Contact getContact(Bundle contactBundle)
    {
        String jsonMyObject = null;
        if (contactBundle != null) {
            jsonMyObject = contactBundle.getString(CONTACT_EXTRA_NAME);
        }
       return new Gson().fromJson(jsonMyObject, Contact.class);
    }

    private void getMessages()
    {

        List<Email> emails = getEmailsFromServer();
        if(emails!=null)
            for(int i = messages.size(); i< emails.size();i++) {

                if(emails.get(i).getFrom().contains(contact.getEmail()))
                     newMessages.add(emails.get(i).getTimestamp()+" "+contact.getName() + ": " + emails.get(i).getMessage());
                else
                    newMessages.add(emails.get(i).getTimestamp()+" you: " + emails.get(i).getMessage());
            }

        messages.addAll(newMessages);

    }

    private List<Email> getEmailsFromServer()
    {
        List<Email> serverMessages = new ArrayList<>();

        try {
           serverMessages = emailHandler.readEmailsFrom(preferencesHandler.getPreference(getApplicationContext(),preferencesHandler.getEmailPrefName())
                   ,preferencesHandler.getPreference(getApplicationContext(),preferencesHandler.getPasswordPrefName()),contact.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return serverMessages;

    }

    private class DownloadMessages extends AsyncTask<String, Void, String> {

        boolean loadScreen = false;

        public DownloadMessages(boolean load)
        {
            loadScreen = load;
        }

       @Override
       protected void onPreExecute(){
           if(loadScreen)
               createLoader();
       }

        @Override
        protected String doInBackground(String... urls) {
            getMessages();
        if(messages!=null)
            return "Messages Received";
        else
            return "Download Failed";

    }

    @Override
    protected void onPostExecute(String result) {
        downloading=false;
        if(loadScreen)
            clearLoader();
        updateView();
        //emailHandler.newMessages();
    }
}



}
