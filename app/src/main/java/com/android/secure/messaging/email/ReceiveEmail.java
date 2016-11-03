package com.android.secure.messaging.email;

import android.os.AsyncTask;

import com.android.secure.messaging.keys.Decrypt;
import com.android.secure.messaging.keys.Keys;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

/**
 * Created by Ryan on 10/16/16.
 */

public class ReceiveEmail extends AsyncTask<String, Void, Void> {


    final private String HOSTADDRESS = "secure.emailsrvr.com";
    final private String DOMAIN = "@secureandroidmessaging.com";
    //final private int smtpPort = 587; //465
    private static Decrypt decrypt;
    private static Keys keys;
    private static ArrayList<Email> emailArrayList;

    private ArrayList readEmailFromAddress(String checkEmailAddress, String password, String fromEmail) throws Exception {

        ArrayList<Email> emailArray = new ArrayList<Email>();

        keys = keys.getInstance();
        decrypt = new Decrypt(keys.getPrivateKey());

        Properties properties = System.getProperties();
        properties.put("mail.store.protocol", "imaps");
        Session session = Session.getInstance(properties, null);
        Store store = session.getStore();
        store.connect(HOSTADDRESS, checkEmailAddress, password);
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        for (int i = 1; i <= inbox.getMessageCount(); i++) {
            Message msg = inbox.getMessage(i);
            String to = null;
            String from = null;
            String message = null;
            String timestamp = msg.getSentDate().toString();
            String subject = msg.getSubject().toString();
            from = subject.substring(subject.indexOf("[") + 1, subject.indexOf("]"));
            to = subject.substring(subject.indexOf("(") + 1, subject.indexOf(")"));

            if (from.contains(fromEmail) | to.contains(fromEmail)) {

                decrypt = new Decrypt(keys.getPrivateKey());

                byte[] msgBytes = msg.getContent().toString().getBytes("ISO-8859-1");
                byte[] decryptedMessage = decrypt.decrypt(msgBytes);
                message = new String(decryptedMessage);

                emailArray.add(new Email(to, from, message, timestamp));

            }
        }

        setEmailArray(emailArray);
        return emailArray;

    }


    private ArrayList readAllEmails(String checkEmailAddress, String password) throws Exception {
        ArrayList<Email> emailArray = new ArrayList<Email>();

        keys = keys.getInstance();
        decrypt = new Decrypt(keys.getPrivateKey());

        Properties properties = System.getProperties();
        properties.put("mail.store.protocol", "imaps");
        Session session = Session.getInstance(properties, null);
        Store store = session.getStore();
        store.connect(HOSTADDRESS, checkEmailAddress, password);
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        for (int i = 1; i <= inbox.getMessageCount(); i++) {
            Message msg = inbox.getMessage(i);
            String to = null;
            String from = null;
            String message = null;
            String timestamp = msg.getSentDate().toString();
            String subject = msg.getSubject().toString();
            from = subject.substring(subject.indexOf("[") + 1, subject.indexOf("]"));
            to = subject.substring(subject.indexOf("(") + 1, subject.indexOf(")"));

            decrypt = new Decrypt(keys.getPrivateKey());

            byte[] msgBytes = msg.getContent().toString().getBytes("ISO-8859-1");
            byte[] decryptedMessage = decrypt.decrypt(msgBytes);
            message = new String(decryptedMessage);

            emailArray.add(new Email(to, from, message, timestamp));
        }

        setEmailArray(emailArray);
        return emailArray;

    }

    void setEmailArray(ArrayList<Email> emailArray) {

        this.emailArrayList = emailArray;

    }

    ArrayList<Email> getEmailArray() {
        return this.emailArrayList;
    }


    @Override
    protected Void doInBackground(String... params) {
        String checkEmailAddress = params[0];
        String password = params[1];

        if (params.length == 3) {
            String fromEmail = params[2];
            try {
                readEmailFromAddress(checkEmailAddress, password, fromEmail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                readAllEmails(checkEmailAddress, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
