package org.example;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("Random strings:");
        String[] randomStr = new RandomSentenceGenerator().get();
        System.out.println(Arrays.toString(randomStr));
        new InsertionSorter().sort(randomStr);
        System.out.println("After sorting:");
        System.out.println(Arrays.toString(randomStr));
    }
}