package com.android.secure.messaging.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ryan on 10/12/16.
 */

public class PreferencesHandler implements Preferences {

    private static final String emailPrefName = "SAMEmail";
    private static final String passwordPrefName = "SAMPassword";
    private static final String EMAILCOUNTPREFNAME = "EmailCount";

    //private final String prefName = "samEmail";

    /*
    public void setEmailAddress(Context context, String emailAddress){
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;

        sharedPreferences = context.getSharedPreferences(prefName,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putString(prefName,emailAddress);
        editor.commit();

        System.out.println("This is what's in the email address preferneces file: " + sharedPreferences.getString(prefName,null));
    }
    */

    /*
    public String getEmailAddress(Context context){
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(prefName,Context.MODE_PRIVATE);
        return sharedPreferences.getString(prefName,null);
    }
    */


    @Override
    public void setPreference(Context context, String preferenceName, String prefValue) {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;

        sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putString(preferenceName, prefValue);
        editor.commit();
        System.out.println("Just inserted: " + prefValue + " into: " + preferenceName);
    }

    public void setPreference(Context context, String preferenceName, int prefValue) {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;

        sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putInt(preferenceName, prefValue);
        editor.commit();

        System.out.println("Just inserted: " + prefValue + " into: " + preferenceName);
    }

    @Override
    public String getPreference(Context context, String preferenceName) {
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, null);
    }

    @Override
    public String getEmailPrefName() {
        return emailPrefName;
    }

    @Override
    public String getPasswordPrefName() {
        return passwordPrefName;
    }

    @Override
    public void resetPreferences(Context context){
        setPreference(context, getEmailPrefName(), null);
        setPreference(context, getPasswordPrefName(), null);
    }

    @Override
    public String getEmailCountPrefName(){
        return EMAILCOUNTPREFNAME;

    }

}
