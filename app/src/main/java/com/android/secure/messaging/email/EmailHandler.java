package com.android.secure.messaging.email;

import android.content.Context;

/**
 * Created by christophershirley on 9/18/16.
 */
public class EmailHandler {

    EmailCommService ecs = new EmailCommService();
    Context mContext;

    EmailHandler(Context context)
    {
        mContext = context;
    }

    void send(String to, String from, String password, String message)
    {
        ecs.execute(to, from, password, message);
    }

    boolean saveUniqueEmail(String uniqueEmailAddress)
    {
        return false;
    }

    String requestUniqueEmailAddress()
    {
        return null;
    }

    String getUniqueEmail()
    {
        return null;
    }
}
