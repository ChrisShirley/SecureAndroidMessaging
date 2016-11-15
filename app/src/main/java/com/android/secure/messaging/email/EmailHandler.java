package com.android.secure.messaging.email;

import android.content.Context;
import android.os.Build;

import com.android.secure.messaging.Preferences.Preferences;
import com.android.secure.messaging.Preferences.PreferencesHandler;
import com.android.secure.messaging.RandomStringGenerator.RandomStringGenerator;
import com.android.secure.messaging.database.DAO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    SendEmail sendEmail = new SendEmail();
    ReceiveEmail receiveEmail = new ReceiveEmail();
    RandomStringGenerator rsg = new RandomStringGenerator();
    EmailGenerator emailGenerator = new EmailGenerator();
    Preferences preferences = new PreferencesHandler();
    Context mContext;
    EmailListener el;
    private static boolean listenerAlive = false;

    public EmailHandler(Context context)
    {
        mContext = context;
        //dao = new DAO(context,DATABASE_NAME,DATABASE_VERSION,MESSAGES_TABLE_CREATE);
    }

    public void send(String to, String from, String password, String encryptedMessageForContact, String encryptedMessageForSelf)
    {
        sendEmail.execute(to, from, password, encryptedMessageForContact, encryptedMessageForSelf);
    }

    public ArrayList<Email> readAllEmails (String checkEmailAddress, String password) throws InterruptedException, ExecutionException{
        receiveEmail.execute(checkEmailAddress, password);
        receiveEmail.get();
        return receiveEmail.getEmailArray();
    }

    public ArrayList<Email> readEmailsFrom(String checkEmailAddress, String password, String fromAddress) throws Exception{
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

        el = new EmailListener(mContext);
        listenerAlive = true;
        el.execute(preferences.getPreference(mContext, preferences.getEmailPrefName()),
                preferences.getPreference(mContext, preferences.getPasswordPrefName()));


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
    }

    public static void setListenerAlive(boolean alive)
    {
        listenerAlive = alive;
    }

    public static boolean getListenerLifeStatus()
    {
        return listenerAlive;
    }

    public void stopListenerTask()
    {
        el.cancel(true);
    }
}
