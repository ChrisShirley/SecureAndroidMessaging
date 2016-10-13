package com.android.secure.messaging.email;


import android.util.Base64;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ryan on 10/12/16.
 */

public class SignatureGenerator {

    final private String userKey = "1fSvr+73dzN/4pPy/T+g";
    final private String secretKey = "rWAcPx6YAAcgviM7BlouEW2Pgf5hC6uWctdLmFgS";
    final private String userAgent = "Rackspace Management Interface";
    private String formattedDate;
    private String combinedSignature;
    private String hashedSignature;
    private Date date = new Date();
    final private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddkkmmss", Locale.ENGLISH);

    public SignatureGenerator() throws Exception{
        formatDate();
        createSignature();
    }


    /*
   In order to access the API, the timestamp must be formatted in the following format:
   <year><month><day><hour><minute><second>. This method converts the current timestamp
   to the correct format.
   */
    private void formatDate() {
        formattedDate = simpleDateFormat.format(date.getTime());
        System.out.println("This is the date: " + formattedDate);
    }

    /*
    Create signature that will eventually be hashed and passed via API to authenticate
     */
    private void createSignature() throws Exception{
        combinedSignature = userKey + userAgent + formattedDate + secretKey;
        String result = null;

        java.security.MessageDigest messageDigest = null;
        messageDigest = java.security.MessageDigest.getInstance("SHA-1");

        byte[] dataBytes = combinedSignature.getBytes("UTF-8");
        System.out.println("Pre encoding: " + dataBytes);
        result = Base64.encodeToString(messageDigest.digest(dataBytes), Base64.DEFAULT);

        System.out.println("This is the result: " + result);
        result = result.replaceAll("\n", "");
        hashedSignature = userKey + ":" + formattedDate + ":" + result;
    }

    public String getSignature(){
        return hashedSignature;
    }


}
