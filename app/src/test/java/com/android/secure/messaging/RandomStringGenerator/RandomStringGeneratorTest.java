package com.android.secure.messaging.RandomStringGenerator;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by christophershirley on 11/25/16.
 */
public class RandomStringGeneratorTest {

    RandomStringGenerator randomStringGenerator = new RandomStringGenerator();

    @Test
    public void generateRandomEmail() throws Exception {
        String email = randomStringGenerator.generateRandomEmail(12);
        Assert.assertNotNull(email);
        Assert.assertTrue(email.length()==12);
    }

    @Test
    public void generateRandomPassword() throws Exception {

        String password = randomStringGenerator.generateRandomPassword(10);
        Assert.assertNotNull(password);
        Assert.assertTrue(password.length()==10);
    }

}