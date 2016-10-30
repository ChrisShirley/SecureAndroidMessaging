package com.android.secure.messaging.email;

import android.content.Context;

import com.android.secure.messaging.Preferences.Preferences;
import com.android.secure.messaging.Preferences.PreferencesHandler;
import com.android.secure.messaging.RandomStringGenerator.RandomStringGenerator;
import com.android.secure.messaging.database.DAO;

/**
 * Created by christophershirley on 9/18/16.
 */
public class EmailHandler {

    private  static DAO dao;
    public static final String TABLE_MESSAGES = "messages";

    public static final String ID = "id";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String TIMESTAMP = "timestamp";
    public static final String MESSAGE = "message";
    public static final String DATABASE_NAME = TABLE_MESSAGES+".db";
    public static final int DATABASE_VERSION = 1;

    private static final String MESSAGES_TABLE_CREATE = "create table "
            + TABLE_MESSAGES + "(" +
            ID + " text PRIMARY KEY, "+
            TIMESTAMP +" text not null, "+
            TO + " text not null, " +
            FROM + " text not null, " +
            MESSAGE +" text not null);";

    final private String DOMAIN = "@secureandroidmessaging.com";
    SendEmail sendEmail = new SendEmail();
    ReceiveEmail receiveEmail = new ReceiveEmail();
    RandomStringGenerator rsg = new RandomStringGenerator();
    EmailGenerator emailGenerator = new EmailGenerator();
    Preferences preferences = new PreferencesHandler();
    Context mContext;

    public EmailHandler(Context context)
    {
        mContext = context;
        dao = new DAO(context,DATABASE_NAME,DATABASE_VERSION,MESSAGES_TABLE_CREATE);
    }

    public void send(String to, String from, String password, String message)
    {
        sendEmail.execute(to, from, password, message);
    }

    public void read(String checkEmailAddress, String password){
        receiveEmail.execute(checkEmailAddress, password);
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
}
