package com.android.secure.messaging.messaging;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.TextView;
import com.android.secure.messaging.Preferences.Preferences;
import com.android.secure.messaging.Preferences.PreferencesHandler;
import com.android.secure.messaging.R;
import com.android.secure.messaging.contacts.Contact;
import com.android.secure.messaging.email.Email;
import com.android.secure.messaging.email.EmailHandler;
import com.android.secure.messaging.email.EmailListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

public class MessagingActivity extends AppCompatActivity {

    private Contact contact;
    private EmailHandler emailHandler;
    private TextView textView;
    private ProgressDialog dialog;
    private List<String> messages;

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

        emailHandler = new EmailHandler(this);



        textView = (TextView) findViewById(R.id.messaging_text_view);
        DownloadMessages downloadMessages = new DownloadMessages();
        downloadMessages.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);




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
        for(String message : messages)
            newText += message +"\n";

        MessagingActivity.this.textView.setText(textView.getText() + newText);

    }

    private void clearLoader()
    {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private Contact getContact(Bundle contactBundle)
    {
        String jsonMyObject = null;
        if (contactBundle != null) {
            jsonMyObject = contactBundle.getString(CONTACT_EXTRA_NAME);
        }
       return new Gson().fromJson(jsonMyObject, Contact.class);
    }

    private void getMessages()
    {
        List<String> allMessages = new ArrayList<>();
        List<Email> emails = getEmailsFromServer();
        if(emails!=null)
            for(Email email : emails) {

                if(email.getFrom().equals(contact.getEmail()))
                     allMessages.add(email.getTimestamp()+" "+contact.getName() + ": " + email.getMessage());
                else
                    allMessages.add(email.getTimestamp()+" you: " + email.getMessage());
            }

        messages = allMessages;

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
       @Override
       protected void onPreExecute(){
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
        clearLoader();
        updateView();
        emailHandler.newMessages(preferencesHandler.getPreference(getApplicationContext(),preferencesHandler.getEmailPrefName())
                ,preferencesHandler.getPreference(getApplicationContext(),preferencesHandler.getPasswordPrefName()));
    }
}



}
