package com.android.secure.messaging.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ryan on 10/12/16.
 */

public class Preferences {

    private final String prefName = "samEmail";

    public void setEmailAddress(Context context, String emailAddress){
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;

        sharedPreferences = context.getSharedPreferences(prefName,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putString(prefName,emailAddress);
        editor.commit();

        System.out.println("This is what's in the email address preferneces file: " + sharedPreferences.getString(prefName,null));
    }

    public String getEmailAddress(Context context){
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(prefName,Context.MODE_PRIVATE);
        return sharedPreferences.getString(prefName,null);
    }


}