package com.android.secure.messaging.email;


import android.os.AsyncTask;
import android.widget.Toast;

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
    final private int smtpPort = 587; //465
//    String sendFrom = "testaccount@secureandroidmessaging.com";
//    String sendTo = "ryan.silan@gmail.com";
//    String password = "Sweng501#";
    String finalString="";
    Multipart multipart;



    private void sendEmail(String sendTo, String sendFrom, String password, String message)throws AddressException, MessagingException{

        Properties properties = System.getProperties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", hostAddress);
        properties.put("mail.smtp.user", sendFrom);
        properties.put("mail.smtp.password", password);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.auth", "true");

        System.out.println("This is the username:" + sendFrom);
        System.out.println("This is the password:" + password);

        Session session = Session.getDefaultInstance(properties,null);
        DataHandler dataHandler = new DataHandler(new ByteArrayDataSource(finalString.getBytes(),"test/plain"));
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(sendFrom));
        email.setDataHandler(dataHandler);

        multipart = new MimeMultipart();
        InternetAddress toAddress;
        toAddress = new InternetAddress(sendTo);
        email.addRecipient(Message.RecipientType.TO, toAddress);
        email.setSubject("Test email");
        email.setContent(multipart);
        email.setText(message);

        Transport transport = session.getTransport("smtp");
        transport.connect(hostAddress, smtpPort, sendFrom, password);
        transport.sendMessage(email, email.getAllRecipients());
        transport.close();

    }

    @Override
    protected Void doInBackground(String... params) {
        String sendTo = params[0];
        String sendFrom = params[1];
        String password = params[2];
        String message = params[3];

        try {
            sendEmail(sendTo, sendFrom, password, message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
