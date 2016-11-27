package com.android.secure.messaging.email;

import android.content.Context;
import android.os.Build;

import com.android.secure.messaging.Preferences.Preferences;
import com.android.secure.messaging.Preferences.PreferencesHandler;
import com.android.secure.messaging.RandomStringGenerator.RandomStringGenerator;
import com.android.secure.messaging.database.DAO;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;

/**
 * Created by christophershirley on 9/18/16.
 */
public class EmailHandler {

    //private  static DAO dao;
    //private static ArrayList<Email> emailArrayList;
    //public static final String TABLE_MESSAGES = "messages";

//    public static final String ID = "id";
//    public static final String FROM = "from";
//    public static final String TO = "to";
//    public static final String TIMESTAMP = "timestamp";
//    public static final String MESSAGE = "message";
//    public static final String DATABASE_NAME = TABLE_MESSAGES+".db";
//    public static final int DATABASE_VERSION = 1;
//
//    private static final String MESSAGES_TABLE_CREATE = "create table "
//            + TABLE_MESSAGES + "(" +
//            ID + " text PRIMARY KEY, "+
//            TIMESTAMP +" text not null, "+
//            TO + " text not null, " +
//            FROM + " text not null, " +
//            MESSAGE +" text not null);";

    final private String DOMAIN = "@secureandroidmessaging.com";
    SendEmail sendEmail;
    private static EmailHandler emailHandler;
    ReceiveEmail receiveEmail;
    RandomStringGenerator rsg = new RandomStringGenerator();
    EmailGenerator emailGenerator = new EmailGenerator();
    final Preferences preferences = new PreferencesHandler();
    Context mContext;
    private static EmailListener el;
    private static boolean listenerAlive = false;
    private static boolean checkingMessages = false;

    public static EmailHandler getInstance(Context context)
    {
        if(emailHandler==null)
            emailHandler = new EmailHandler(context);
        return emailHandler;
    }
    private EmailHandler(Context context)
    {
        mContext = context;
        //dao = new DAO(context,DATABASE_NAME,DATABASE_VERSION,MESSAGES_TABLE_CREATE);
    }

    public void send(String to, String from, String password, String encryptedMessageForContact, String encryptedMessageForSelf)
    {
        sendEmail = new SendEmail();
        sendEmail.execute(to, from, password, encryptedMessageForContact, encryptedMessageForSelf);
    }

    public ArrayList<Email> readAllEmails (String checkEmailAddress, String password) throws InterruptedException, ExecutionException{
        receiveEmail = new ReceiveEmail();
        receiveEmail.execute(checkEmailAddress, password);
        receiveEmail.get();
        return receiveEmail.getEmailArray();
    }

    public ArrayList<Email> readEmailsFrom(String checkEmailAddress, String password, String fromAddress) throws Exception{
        receiveEmail = new ReceiveEmail();
        receiveEmail.execute(checkEmailAddress, password, fromAddress);
        receiveEmail.get();
        return receiveEmail.getEmailArray();

    }

    public String requestUniqueEmailAddress()
    {
        String generatedEmail = rsg.generateRandomEmail(12);
        String generatedPassword = rsg.generateRandomPassword(10);
        emailGenerator.execute(generatedEmail,generatedPassword);
        preferences.setPreference(mContext, preferences.getEmailPrefName(), generatedEmail + DOMAIN);
        preferences.setPreference(mContext, preferences.getPasswordPrefName(), generatedPassword);
        return null;
    }

    public String getUniqueEmail()
    {
        return preferences.getPreference(mContext, preferences.getEmailPrefName());
    }

    public void newMessages(){
        if(listenerAlive==false) {
            el = new EmailListener(mContext);
            listenerAlive = true;
            el.execute(preferences.getPreference(mContext, preferences.getEmailPrefName()),
                    preferences.getPreference(mContext, preferences.getPasswordPrefName()));
        }




//        if(preferences.getPreference(mContext, preferences.getEmailCountPrefName()) == null) {
//
//            preferences.setPreference(mContext, preferences.getEmailCountPrefName(), 0);
//
//        }
//
//        int oldMessageCount = Integer.parseInt(preferences.getPreference(mContext, preferences.getEmailCountPrefName()));
//        int newMessageCount = emailCount.countMessages(checkEmailAddress, password, mContext);
//
//        if(newMessageCount > oldMessageCount){
//            return true;
//        }else {
//            return false;
//        }
    }final private String HOSTADDRESS = "secure.emailsrvr.com";
    public void checkForMessages(String checkEmailAddress, String password, int count)
    {
        if(checkingMessages)
            return;
        else
            checkingMessages = true;

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



             if(inbox.getMessageCount()>count)
                preferences.setPreference(mContext, preferences.getNewEmailPrefName(), "true");






            store.close();


        } catch (FolderClosedException e) {
            System.out.println("In folder closed exception");
            checkForMessages(checkEmailAddress, password, count);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        checkingMessages = false;

    }


}
