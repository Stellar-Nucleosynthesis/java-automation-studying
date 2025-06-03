package org.example;

import java.util.HashMap;
import java.util.Map;

public class AuthentificationProvider {
    private final Map<String, PasswordHash> passwordHashMap = new HashMap<>();

    public void addUser(User user) {
        passwordHashMap.put(user.getUsername(), new PasswordHash(new Sha256(), user.getPassword()));
    }

    public boolean authenticated(User user) {
        return passwordHashMap.containsKey(user.getUsername()) &&
                passwordHashMap.get(user.getUsername()).validatePassword(user.getPassword());
    }
}
