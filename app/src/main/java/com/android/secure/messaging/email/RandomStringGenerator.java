package com.android.secure.messaging.email;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by Ryan on 10/12/16.
 */

public class RandomStringGenerator {


    char[] emailCharacterSet = ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789").toCharArray();
    char[] specialCharacterSet = ("?!@#$%^&*()").toCharArray();


    public String generateRandomEmail(int length) {
        Random random = new SecureRandom();
        char[] result = new char[length];
        for (int i = 0; i < result.length; i++) {
            // picks a random index out of character set > random character
            int randomCharIndex = random.nextInt(emailCharacterSet.length);
            result[i] = emailCharacterSet[randomCharIndex];
        }
        //System.out.println(new String(result));
        return new String(result);
    }

    public String generateRandomPassword(int length){
        Random random = new SecureRandom();
        char[] result = new char[length];
        for (int i = 0; i < result.length-1; i++) {
            // picks a random index out of character set > random character
            int randomCharIndex = random.nextInt(emailCharacterSet.length);
            result[i] = emailCharacterSet[randomCharIndex];
        }
            int randomSpecialCharIndex = random.nextInt(specialCharacterSet.length);
            result[result.length-1] = specialCharacterSet[randomSpecialCharIndex];

        //System.out.println(new String(result));
        return new String(result);
    }
}
