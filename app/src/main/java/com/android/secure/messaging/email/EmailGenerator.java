package com.android.secure.messaging.email;

import android.os.AsyncTask;

import com.android.secure.messaging.Preferences.Preferences;
import com.android.secure.messaging.Preferences.PreferencesHandler;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Ryan on 10/12/16.
 */

public class EmailGenerator extends AsyncTask<String, Void, Void> {

    final private String userAgent = "Rackspace Management Interface";
    //Preferences preferences = new PreferencesHandler();

    @Override
    protected Void doInBackground(String... params) {
        String signature = null;
        String name = params[0];
        String password = params[1];

        URL url;

        try {
            SignatureGenerator signatureGenerator = new SignatureGenerator();
            signature = signatureGenerator.getSignature();
            HttpsURLConnection urlConnection = null;

            url = new URL("https://api.emailsrvr.com/v1/customers/1948717/domains/secureandroidmessaging.com/rs/mailboxes/" + name);

            String param = "password=" + password;

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
        return null;
    }
}
