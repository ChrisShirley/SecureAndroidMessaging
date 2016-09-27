package com.android.secure.messaging.Biometrics;

import java.security.KeyStore;

import javax.crypto.KeyGenerator;

/**
 * Created by silanr on 9/27/2016.
 */
public class BiometricCipher {

    private KeyStore keyStore;
    private KeyGenerator keyGenerator;

    public void generateKey() {

        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
