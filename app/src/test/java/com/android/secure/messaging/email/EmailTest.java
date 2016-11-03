package com.android.secure.messaging.email;


import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by christophershirley on 9/17/16.
 */
public class EmailTest {

    Email email;

    @Before
    public void setUp() throws Exception {



    }

    @Test
    public void getMessage() throws Exception {

        TestCase.assertTrue(email.getMessage()!=null);
    }






}