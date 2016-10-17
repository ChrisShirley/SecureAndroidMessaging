package com.android.secure.messaging.email;

import android.content.Context;

import com.android.secure.messaging.Preferences.Preferences;
import com.android.secure.messaging.Preferences.PreferencesHandler;
import com.android.secure.messaging.RandomStringGenerator.RandomStringGenerator;

/**
 * Created by christophershirley on 9/18/16.
 */
public class EmailHandler {

    final private String domain = "@secureandroidmessaging.com";
    SendEmail sendEmail = new SendEmail();
    ReceiveEmail receiveEmail = new ReceiveEmail();
    RandomStringGenerator rsg = new RandomStringGenerator();
    EmailGenerator emailGenerator = new EmailGenerator();
    Preferences preferences = new PreferencesHandler();
    Context mContext;

    public EmailHandler(Context context)
    {
        mContext = context;
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
        preferences.setPreference(mContext, preferences.getEmailPrefName(), generatedEmail + domain);
        preferences.setPreference(mContext, preferences.getPasswordPrefName(), generatedPassword);
        return null;
    }

    public String getUniqueEmail()
    {
        return preferences.getPreference(mContext, preferences.getEmailPrefName());
    }
}
