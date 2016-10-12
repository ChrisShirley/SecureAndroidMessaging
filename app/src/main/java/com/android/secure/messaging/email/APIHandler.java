package com.android.secure.messaging.email;


import android.os.AsyncTask;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Ryan on 10/10/16.
 */

public class APIHandler extends AsyncTask<String, Void, String>{

    final private String userKey = "1fSvr+73dzN/4pPy/T+g";
    final private String secretKey = "rWAcPx6YAAcgviM7BlouEW2Pgf5hC6uWctdLmFgS";
    final private String userAgent = "Rackspace Management Interface";
    String formattedDate;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddkkmmss", Locale.ENGLISH);
    Date date = new Date();
    String combinedSignature;
    String finalSignature;
    final private String apiURL = "https://api.emailsrvr.com/v1/";


    public String buildSignature(String toEncrypt) throws Exception {
        String result = null;
        java.security.MessageDigest messageDigest = null;
        messageDigest = java.security.MessageDigest.getInstance("SHA-1");

        byte[] dataBytes = toEncrypt.getBytes("UTF-8");
        System.out.println("Pre encoding: " + dataBytes);
        result = Base64.encodeToString(messageDigest.digest(dataBytes),Base64.DEFAULT);

        System.out.println("This is the result: " + result);
        result = result.replaceAll("\n", "");
        finalSignature = userKey + ":" + formattedDate + ":" + result;
        return finalSignature;
    }

    public void getTimeStamp() throws Exception{
        formattedDate = simpleDateFormat.format(date.getTime());
        System.out.println("This is the date: " + formattedDate);
        combinedSignature = userKey + userAgent + formattedDate + secretKey;
        System.out.println("This is the combined signature: " + combinedSignature);
        //buildSignature("eGbq9/2hcZsRlr1JV1PiRackspace Management Interface20010308143725QHOvchm/40czXhJ1OxfxK7jDHr3t");
        //buildSignature(combinedSignature);
        //accessAPI(buildSignature(combinedSignature));

    }


    public void accessAPI(String signature) throws Exception{
        String newAPIURL = apiURL + "customers/1948717/domains/secureandroidmessaging/rs/mailboxes";
        String response;

        System.out.println("This is the new API URL: " + newAPIURL);

        URL url = new URL(newAPIURL);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setDoOutput(false);



        // / connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Accept", "text/xml");
        connection.setRequestProperty("User-Agent",userAgent);
        connection.setRequestProperty("X-Api-Signature",signature);
        connection.setRequestMethod("GET");
        //OutputStreamWriter request = new OutputStreamWriter(connection.getOutputStream());

        System.out.println("before request.flush");
        //request.flush();
        //request.close();
        String line = "";

        //create your inputsream
        InputStreamReader isr = new InputStreamReader(
                connection.getInputStream());
        //read in the data from input stream, this can be done a variety of ways
        System.out.println("After isr");
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
            System.out.println("in the while loop");
        }
        //get the string version of the response data
        response = sb.toString();
        System.out.println("This is the data: " + response);
        //do what you want with the data now

        //always remember to close your input and output streams
        isr.close();
        reader.close();


    }


    @Override
    protected String doInBackground(String... params) {

        String signature = null;
        try {
            signature = buildSignature(combinedSignature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String newAPIURL = apiURL + "customers/1948717/domains/secureandroidmessaging.com/rs/mailboxes/ryansilan";
        String response;

        System.out.println("This is the new API URL: " + newAPIURL);

        URL url = null;
        try {
            url = new URL(newAPIURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setDoOutput(true);



        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Accept", "text/xml");
        connection.setRequestProperty("User-Agent", userAgent);
        connection.setRequestProperty("X-Api-Signature", signature);
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        OutputStreamWriter request = null;
        try {
            request = new OutputStreamWriter(connection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            System.out.println("Error code: " + connection.getResponseCode());

            System.out.println("Error status: " + connection.getResponseMessage());
            System.out.println(connection.getHeaderField("x-error-message"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("before request.flush");
        try {
            request.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            request.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line = "";

        //create your inputsream
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(
                    connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //read in the data from input stream, this can be done a variety of ways
        System.out.println("After isr");
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
                System.out.println("in the while loop");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get the string version of the response data
        response = sb.toString();
        System.out.println("This is the data: " + response);
        //do what you want with the data now

        //always remember to close your input and output streams
        try {
            isr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
