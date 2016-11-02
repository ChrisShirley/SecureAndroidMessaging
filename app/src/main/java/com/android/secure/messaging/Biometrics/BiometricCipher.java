package com.android.secure.messaging.Biometrics;

import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Created by silanr on 9/27/2016.
 */

public class BiometricCipher {

    private Cipher cipher;
    private String mKeyName;
    private KeyStore mKeyStore;

    //Constructor for BiometricCipher class, sets references for mKeyStore and mKeyName
    public BiometricCipher(KeyStore keyStore, String keyName){
        mKeyStore = keyStore;
        mKeyName = keyName;
    }


    public boolean initCipher() {
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to get cipher", e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get cipher", e);
        }


        try {
            mKeyStore.load(null);
            SecretKey secretKey = (SecretKey) mKeyStore.getKey(mKeyName, null);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init cipher", e);
        }
    }

    public Cipher getCipher(){
        return cipher;
    }
}
