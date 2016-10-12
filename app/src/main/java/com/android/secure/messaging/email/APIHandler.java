package com.android.secure.messaging.email;


import android.os.AsyncTask;
import android.util.Base64;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Ryan on 10/10/16.
 */

public class APIHandler extends AsyncTask<String, Void, String> {

    final private String userKey = "1fSvr+73dzN/4pPy/T+g";
    final private String secretKey = "rWAcPx6YAAcgviM7BlouEW2Pgf5hC6uWctdLmFgS";
    final private String userAgent = "Rackspace Management Interface";
    final private String apiURL = "https://api.emailsrvr.com/v1/";
    String formattedDate;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddkkmmss", Locale.ENGLISH);
    Date date = new Date();
    String combinedSignature;
    String hashedSignature;

    public APIHandler() {

    }

    private void formatDate() {
        formattedDate = simpleDateFormat.format(date.getTime());
        System.out.println("This is the date: " + formattedDate);
    }

    private String createSignature() {
        combinedSignature = userKey + userAgent + formattedDate + secretKey;
        return combinedSignature;
    }

    private String createHashedSignature(String toEncrypt) throws Exception {
        String result = null;
        java.security.MessageDigest messageDigest = null;
        messageDigest = java.security.MessageDigest.getInstance("SHA-1");

        byte[] dataBytes = toEncrypt.getBytes("UTF-8");
        System.out.println("Pre encoding: " + dataBytes);
        result = Base64.encodeToString(messageDigest.digest(dataBytes), Base64.DEFAULT);

        System.out.println("This is the result: " + result);
        result = result.replaceAll("\n", "");
        hashedSignature = userKey + ":" + formattedDate + ":" + result;
        return hashedSignature;
    }


    public String getTimeStamp() throws Exception {
        return formattedDate;

    }

    @Override
    protected String doInBackground(String... params) {

        String signature = null;
        URL url;
        try {
            formatDate();
            createSignature();
            signature = createHashedSignature(combinedSignature);

            HttpsURLConnection urlConnection = null;

            // url = new URL("https://api.emailsrvr.com/v1/customers/me/domains");
            url = new URL("https://api.emailsrvr.com/v1/customers/1948717/domains/secureandroidmessaging.com/rs/mailboxes/testact");
            String param = "password=Today123";
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Accept", "text/xml");
            urlConnection.setRequestProperty("User-Agent", userAgent);
            urlConnection.setRequestProperty("X-Api-Signature", signature);
            urlConnection.setDoOutput(true);
            OutputStream out = urlConnection.getOutputStream();
            out.write(param.getBytes("UTF-8"));

            System.out.println("Error code: " + urlConnection.getResponseCode());
            System.out.println("Error status: " + urlConnection.getResponseMessage());
            System.out.println(urlConnection.getHeaderField("x-error-message"));


            InputStream in = urlConnection.getInputStream();
            InputStreamReader isw = new InputStreamReader(in);
            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                System.out.print(current);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }











/*
        String signature = null;
        URL url;
        try {
            signature = buildSignature(combinedSignature);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpsURLConnection urlConnection = null;

        try {
           // url = new URL("https://api.emailsrvr.com/v1/customers/me/domains");
            url = new URL("https://api.emailsrvr.com/v1/customers/1948717/domains/secureandroidmessaging.com/rs/mailboxes/ryansilan");
            urlConnection = (HttpsURLConnection) url.openConnection();
           // urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Accept", "text/xml");
            urlConnection.setRequestProperty("User-Agent", userAgent);
            urlConnection.setRequestProperty("X-Api-Signature", signature);
            InputStream in = urlConnection.getInputStream();
            InputStreamReader isw = new InputStreamReader(in);
            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                System.out.print(current);
            }


        } catch (Exception e){
            e.printStackTrace();
        }
        */
        return null;
    }
}
