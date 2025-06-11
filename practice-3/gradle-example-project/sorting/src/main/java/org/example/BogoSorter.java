package org.example;

import java.util.Random;

public class BogoSorter implements Sorter {
    public <T extends Comparable<T>> void sort(T[] arr) {
        Random rand = new Random();
        while (!isSorted(arr)) {
            shuffle(arr, rand);
        }
    }

    private <T> void shuffle(T[] arr, Random rand) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            T temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    private <T extends Comparable<T>> boolean isSorted(T[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i].compareTo(arr[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }
}
