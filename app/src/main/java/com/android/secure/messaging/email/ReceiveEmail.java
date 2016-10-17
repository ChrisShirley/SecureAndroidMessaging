package com.android.secure.messaging.email;

import android.os.AsyncTask;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

/**
 * Created by Ryan on 10/16/16.
 */

public class ReceiveEmail extends AsyncTask<String, Void, Void> {


    final private String hostAddress = "secure.emailsrvr.com";
    final private int smtpPort = 587; //465

    private void readEmail(String checkEmailAddress, String password) throws Exception {
        Properties properties = System.getProperties();
        properties.put("mail.store.protocol", "imaps");

        Session session = Session.getInstance(properties, null);

        Store store = session.getStore();
        store.connect(hostAddress, checkEmailAddress, password);

        Folder inbox = store.getFolder("INBOX");

        inbox.open(Folder.READ_ONLY);

        Message msg = inbox.getMessage(inbox.getMessageCount());

        System.out.println("This msg is in the inbox: " + msg);

        Address[] in = msg.getFrom();

        for (Address address : in) {
            System.out.println("FROM:" + address.toString());
        }

        System.out.println("Message Content: " + msg.getContent());
        System.out.println("SENT DATE:" + msg.getSentDate());
        System.out.println("SUBJECT:" + msg.getSubject());

    }

    @Override
    protected Void doInBackground(String... params) {
        String checkEmailAddress = params[0];
        String password = params[1];

        try {
            readEmail(checkEmailAddress, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
