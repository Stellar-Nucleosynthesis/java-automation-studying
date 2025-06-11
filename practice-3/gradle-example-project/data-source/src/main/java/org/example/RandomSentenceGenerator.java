package org.example;

import java.util.Random;

public class RandomSentenceGenerator implements DataSource{
    public String[] get(){
        String[] result = new String[20];
        for (int i = 0; i < 20; i++) {
            result[i] = generateRandomString();
        }
        return result;
    }

    private static String generateRandomString() {
        int length = 7;
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random rand = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(rand.nextInt(characters.length())));
        }
        return sb.toString();
    }
}
