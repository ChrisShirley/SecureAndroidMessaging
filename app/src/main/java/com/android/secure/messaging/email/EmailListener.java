package com.android.secure.messaging.email;

import android.content.Context;
import android.os.AsyncTask;


import com.android.secure.messaging.Preferences.Preferences;
import com.android.secure.messaging.Preferences.PreferencesHandler;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;

/**
 * Created by Ryan on 11/7/16.
 */

public class EmailListener extends AsyncTask<String, Void, Void> {

    final private String HOSTADDRESS = "secure.emailsrvr.com";
    Preferences preferences = new PreferencesHandler();
    Context mContext = null;

    public EmailListener (Context context){
        mContext = context;
    }

    private void connect(final String checkEmailAddress, final String password) throws FolderClosedException{

        Properties properties = System.getProperties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", HOSTADDRESS);
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.timeout", "600000");


        Session session = Session.getInstance(properties);

        try {
            final IMAPStore store  = (IMAPStore) session.getStore("imaps");
            store.connect(checkEmailAddress, password);

            if (!store.hasCapability("IDLE")) {
                throw new RuntimeException("IDLE not supported");
            }

            final Folder inbox = (IMAPFolder) store.getFolder("INBOX");
            inbox.addMessageCountListener(new MessageCountAdapter() {

                @Override
                public void messagesAdded(MessageCountEvent event) {
                    Message[] messages = event.getMessages();

                    for (Message message : messages) {
                        try {
                            System.out.println("Mail Subject:- " + message.getSubject());
                            preferences.setPreference(mContext, preferences.getNewEmailPrefName(), "true");
                            inbox.close(true);
                            store.close();
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            inbox.open(Folder.READ_ONLY);
            ((IMAPFolder) inbox).idle();
            EmailHandler.setListenerAlive(true);
        } catch (FolderClosedException e) {
            System.out.println("In folder closed exception");
            connect(checkEmailAddress, password);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void listen(final String checkEmailAddress, String password) throws MessagingException, IOException {

        connect(checkEmailAddress, password);
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            listen(params[0], params[1]);
        }
        catch (Exception e) {
            e.printStackTrace();
            EmailHandler.setListenerAlive(false);
        }

        return null;
    }

}

