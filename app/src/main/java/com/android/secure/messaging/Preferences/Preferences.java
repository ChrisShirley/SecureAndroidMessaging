package com.android.secure.messaging.Preferences;

import android.content.Context;

/**
 * Created by silanr on 10/14/2016.
 */

public interface Preferences {


    void resetPreferences(Context context);

    void setPreference(Context context, String preferenceName, String prefValue);

    void setPreference(Context context, String preferenceName, int prefValue);

    String getPreference(Context context, String preferenceName);

    String getEmailPrefName();

    String getPasswordPrefName();

    String getEmailCountPrefName();

    String getNewEmailPrefName();

}
