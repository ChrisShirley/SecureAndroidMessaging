package com.android.secure.messaging.email;

import com.android.secure.messaging.contacts.Contact;

/**
 * Created by christophershirley on 9/18/16.
 */
public class Email {

    String to;
    String from;
    String message;
    String timestamp;

    Email(String to, String from, String message, String timestamp)
    {
        this.to = to;
        this.from = from;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getTo(){ return to; }
    public String getFrom(){ return from; }
    public String getMessage() { return message; }
    public String getTimestamp() { return timestamp; }

}
