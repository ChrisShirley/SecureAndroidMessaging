package com.android.secure.messaging.messaging;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

        emailHandler = new EmailHandler(this);



        textView = (TextView) findViewById(R.id.messaging_text_view);
        nestedScrollView = (NestedScrollView) findViewById(R.id.messaging_scroll_view);

        Download(true);
        preferencesHandler.setPreference(this, preferencesHandler.getNewEmailPrefName(), "false");
        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                       if(preferencesHandler.getPreference(getApplicationContext(),preferencesHandler.getNewEmailPrefName()).contains("true")) {

                           preferencesHandler.setPreference(getApplicationContext(), preferencesHandler.getNewEmailPrefName(), "false");
                           emailHandler.stopListenerTask();
                           emailHandler = null;
                           emailHandler = new EmailHandler(getApplicationContext());
                           Download(false);
                       }
                        else if(!EmailHandler.getListenerLifeStatus())
                           emailHandler.newMessages();
                    }
                }, 0, 5, TimeUnit.SECONDS);


    }

    private void Download(boolean load)
    {
        if(!downloading) {
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
        emailHandler.newMessages();
    }
}



}
