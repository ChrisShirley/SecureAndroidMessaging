package com.android.secure.messaging.messaging;

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
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class MessagingActivity extends AppCompatActivity {

    private Contact contact;
    private EmailHandler emailHandler;
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
        List<String> messages = getMessages();


        TextView textView = (TextView) findViewById(R.id.messaging_text_view);

        for(String message : messages)
            textView.append(message +"\n");



    }

    private Contact getContact(Bundle contactBundle)
    {
        String jsonMyObject = null;
        if (contactBundle != null) {
            jsonMyObject = contactBundle.getString(CONTACT_EXTRA_NAME);
        }
       return new Gson().fromJson(jsonMyObject, Contact.class);
    }

    private List<String> getMessages()
    {
        List<String> allMessages = new ArrayList<>();
        List<Email> emails = getEmailsFromServer();
        if(emails!=null)
            for(Email email : emails)
                allMessages.add(email.getFrom()+" "+email.getTimestamp()+": "+ email.getMessage());
        return allMessages;
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


}
