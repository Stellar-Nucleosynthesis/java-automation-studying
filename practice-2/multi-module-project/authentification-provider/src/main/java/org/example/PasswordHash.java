package org.example;
import java.util.Random;

public class PasswordHash {
    private final Encoder encoder;
    private final String salt;
    private final String hash;

    public PasswordHash(Encoder encoder, String password) {
        this.encoder = encoder;
        Random rand = new Random();
        this.salt = String.valueOf(rand.nextInt());
        this.hash = encoder.encode(password + salt);
    }

    public boolean validatePassword(String password){
        return hash.equals(encoder.encode(password + salt));
    }

    private String generateHash(String password, String salt) {
        return encoder.encode(password + salt);
    }
}
