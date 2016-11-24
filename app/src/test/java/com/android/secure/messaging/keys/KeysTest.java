package com.android.secure.messaging.keys;

import android.app.Activity;

import com.android.secure.messaging.BuildConfig;
import com.android.secure.messaging.CustomRobolectricTestRunner;
import com.android.secure.messaging.Home;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Any;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static org.junit.Assert.*;

/**
 * Created by cjs07f on 11/3/16.
 */
@RunWith(CustomRobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class KeysTest {

    private Activity homeActivity;
    private Keys uKeys;
    private PrivateKey uPrivateKey;
    private PublicKey uPublicKey;


    @Before
    public void setUp() throws Exception {
        homeActivity = Robolectric.setupActivity(Home.class);
        uKeys = uKeys.getInstance();
    }

    @Test
    public void getInstance() throws Exception {
        // ensure an instance is returned
        Assert.assertNotNull(uKeys);
    }


    @Test
    public void getPublicKey() throws Exception {
        // retrieve a public key, auto type check
        uPublicKey = uKeys.getPublicKey();
        // ensure public key is not a null value
        Assert.assertNotNull(uPublicKey);
    }


    @Test
    public void getPublicKeyAsString() throws Exception {
        // get the public key as a string
        String uPublicKeyStr = uKeys.getPublicKeyAsString();
        // ensure public key is not an empty string
        Assert.assertFalse(uPublicKeyStr.isEmpty());

        // create a public key comparison variable
        PublicKey uPublicKeyComp = null;

        // generate a public key from public key string for comparison
        byte[] sigBytes = Base64.decode(uPublicKeyStr,Base64.DEFAULT);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(sigBytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }/* catch (NoSuchProviderException e) {
            e.printStackTrace();
        }*/
        try {
            uPublicKeyComp = keyFactory.generatePublic(x509KeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        // ensure generated public key from string Equals public key
        Assert.assertEquals(uPublicKeyComp, uKeys.getPublicKey());
    }

    @Test
    public void getPrivateKey() throws Exception {
        // retrieve a private key, auto type check
        uPrivateKey = uKeys.getPrivateKey();
        // ensure private key is not a null value
        Assert.assertNotNull(uPrivateKey);
    }

    @Test
    public void getPrivateKeyAsString() throws Exception {
        // get the private key as a string
        String uPrivateKeyStr = uKeys.getPrivateKeyAsString();
        // ensure private key is not an empty string
        Assert.assertFalse(uPrivateKeyStr.isEmpty());

        // create a public key comparison variable
        PrivateKey uPrivateKeyComp = null;

        // generate a private key from private key string for comparison
        byte[] sigBytes = Base64.decode(uPrivateKeyStr,Base64.DEFAULT);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(sigBytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } /*catch (NoSuchProviderException e) {
            e.printStackTrace();
        }*/
        try {
            uPrivateKeyComp =  keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        // ensure generated private key from string Equals private key
        Assert.assertEquals(uPrivateKeyComp, uKeys.getPrivateKey());
    }

}