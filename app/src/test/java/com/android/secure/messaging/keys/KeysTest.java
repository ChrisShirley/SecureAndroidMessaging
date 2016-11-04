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

import java.security.PublicKey;

import static org.junit.Assert.*;

/**
 * Created by cjs07f on 11/3/16.
 */
@RunWith(CustomRobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class KeysTest {

    Activity homeActivity;
    Keys keys;
    @Before
    public void setUp() throws Exception {
        homeActivity = Robolectric.setupActivity(Home.class);


    }

    @Test
    public void getInstance() throws Exception {
        keys = keys.getInstance();
        Assert.assertNotNull(keys);
    }

    @Test
    public void getPublicKey() throws Exception {
    }

    @Test
    public void getPublicKeyAsString() throws Exception {

    }

    @Test
    public void getPrivateKey() throws Exception {

    }

    @Test
    public void getPrivateKeyAsString() throws Exception {

    }

}