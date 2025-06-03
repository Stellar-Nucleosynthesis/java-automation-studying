package org.example;

import java.util.Scanner;

public class Main {
    private final static AuthentificationProvider provider = new AuthentificationProvider();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("Enter 1 to add user, 2 to log in:");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    addUser();
                    break;
                case "2":
                    authenticateUser();
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }

    private static void addUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        provider.addUser(new User(username, password));
        System.out.println("User " + username + " added");
    }

    private static void authenticateUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        boolean valid = provider.authenticated(new User(username, password));
        if (valid) {
            System.out.println("User authenticated!");
        } else {
            System.out.println("User not authenticated!");
        }
    }
}