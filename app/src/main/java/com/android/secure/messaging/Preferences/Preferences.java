package com.android.secure.messaging.Preferences;

import android.content.Context;

/**
 * Created by silanr on 10/14/2016.
 */

public interface Preferences {

    public static final String emailPrefName = "SAMEmail";
    public static final String passwordPrefName = "SAMPassword";

    void setPreference(Context context, String preferenceName, String prefValue);

    String getPreference(Context context, String preferenceName);
}
