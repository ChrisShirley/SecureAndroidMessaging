package com.android.secure.messaging.keys;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;


import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by cjs07f on 9/26/16.
 */
public class Keys {

    final private static String SAM_PREFERENCES = "Preferences";
    private static Keys ourInstance = new Keys();
    private static PrivateKey privateKey;
    private static PublicKey publicKey;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;



    public static Keys getInstance() {
        return ourInstance;
    }

    private Keys() {


    }

    public void getKeys(Context context)
    {
        sharedPreferences = context.getSharedPreferences(SAM_PREFERENCES,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if((getPrivateKey()==null)||(getPublicKey()==null))
            generateKeys();
    }

    private void generateKeys(){
        try {

            KeyPairGenerator generator;
            generator = KeyPairGenerator.getInstance("RSA", "BC");
            generator.initialize(256, new SecureRandom());
            KeyPair pair = generator.generateKeyPair();
            publicKey = pair.getPublic();
            privateKey = pair.getPrivate();
            byte[] publicKeyBytes = publicKey.getEncoded();
            String publicKeyString = new String(Base64.encode(publicKeyBytes,Base64.DEFAULT));
            byte[] privateKeyBytes = privateKey.getEncoded();
            String privateKeyString = new String(Base64.encode(privateKeyBytes,Base64.DEFAULT));
            editor.putString("PublicKey", publicKeyString);
            editor.putString("PrivateKey", privateKeyString);
            editor.commit();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public PublicKey getPublicKey(){
        String publicKeyString = sharedPreferences.getString("PublicKey", "");
        byte[] sigBytes = Base64.decode(publicKeyString,Base64.DEFAULT);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(sigBytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA", "BC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        try {
            return  keyFactory.generatePublic(x509KeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getPublicKeyAsString(){
        return sharedPreferences.getString("PublicKey", "");
    }
    public PrivateKey getPrivateKey(){
        String privateKeyString = sharedPreferences.getString("PrivateKey", "");
        byte[] sigBytes = Base64.decode(privateKeyString,Base64.DEFAULT);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(sigBytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA", "BC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        try {
            return  keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getPrivateKeyAsString(){
        return sharedPreferences.getString("PrivateKey", "");
    }
}
