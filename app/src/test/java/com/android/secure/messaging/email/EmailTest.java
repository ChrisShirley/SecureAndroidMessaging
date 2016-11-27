package com.android.secure.messaging.email;


import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by christophershirley on 9/17/16.
 */
public class EmailTest {

    Email email;
    final String TEST_TO = "testTo";
    final String TEST_FROM = "testFrom";
    final String TEST_MESSAGE = "testMessage";
    final String TEST_TIMESTAMP = "testTimestamp";


    @Before
    public void setUp() throws Exception {

        email = new Email("testTo","testFrom","testMessage","testTimestamp");

    }

    @Test
    public void getMessage() throws Exception {

        TestCase.assertEquals(email.getMessage(),TEST_MESSAGE);
    }

    @Test
    public void getTo() throws Exception {
        TestCase.assertEquals(email.getTo(),TEST_TO);
    }

    @Test
    public void getFrom() throws Exception {
        TestCase.assertEquals(email.getFrom(),TEST_FROM);
    }

    @Test
    public void getTimestamp() throws Exception {
        TestCase.assertEquals(email.getTimestamp(),TEST_TIMESTAMP);
    }

}