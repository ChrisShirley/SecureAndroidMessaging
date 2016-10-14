package com.android.secure.messaging.email;


import android.os.AsyncTask;
import android.util.Log;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

/**
 * Created by silanr on 10/13/2016.
 */

//Class will be used to send/receive email messages

public class EmailCommService extends AsyncTask<String, Void, Void> {

    final private String hostAddress = "secure.emailsrvr.com";
    final private String port = "587";
    String sendFrom = "ryansilan@secureandroidmessaging.com";
    String sendTo = "ryan.silan@gmail.com";
    String password = "Sweng500#";
    String finalString="";
    Multipart multipart;

    public void sendEmail()throws AddressException, MessagingException{

        Properties properties = System.getProperties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", hostAddress);
        properties.put("mail.smtp.user", sendFrom);
        properties.put("mail.smtp.password", password);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smpt.auth", "true");

        Session session = Session.getDefaultInstance(properties,null);
        DataHandler dataHandler = new DataHandler(new ByteArrayDataSource(finalString.getBytes(),"test/plain"));
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sendFrom));
        message.setDataHandler(dataHandler);

        multipart = new MimeMultipart();
        InternetAddress toAddress;
        toAddress = new InternetAddress(sendTo);
        message.addRecipient(Message.RecipientType.TO, toAddress);
        message.setSubject("Test email");
        message.setContent(multipart);
        message.setText("Someone is testing the app");

        Transport transport = session.getTransport("smtp");
        transport.connect(hostAddress,587,sendFrom,password);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();

    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            sendEmail();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
