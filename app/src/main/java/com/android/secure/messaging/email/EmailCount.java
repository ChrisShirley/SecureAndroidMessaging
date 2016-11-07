package com.android.secure.messaging.email;

import android.content.Context;
import android.os.AsyncTask;

import com.android.secure.messaging.Preferences.Preferences;
import com.android.secure.messaging.Preferences.PreferencesHandler;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;

/**
 * Created by silanr on 11/7/2016.
 */

public class EmailCount extends AsyncTask<String, Void, Void> {

    Preferences preferencesHandler = new PreferencesHandler();
    final private String HOSTADDRESS = "secure.emailsrvr.com";
    private int messageCount = 0;
    Context mContext = null;

    private int getMessageCount(String checkEmailAddress, String password) throws Exception {

        Properties properties = System.getProperties();
        properties.put("mail.store.protocol", "imaps");
        Session session = Session.getInstance(properties, null);
        Store store = session.getStore();
        store.connect(HOSTADDRESS, checkEmailAddress, password);
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        messageCount = inbox.getMessageCount();
        preferencesHandler.setPreference(mContext, preferencesHandler.getEmailCountPrefName(), messageCount);
        return messageCount;
    }

    public int countMessages(String checkEmailAddress, String password, Context context){
        mContext = context;
        execute(checkEmailAddress, password);
        return messageCount;
    }

    @Override
    protected Void doInBackground(String... params) {

        try {
            getMessageCount(params[0], params[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
