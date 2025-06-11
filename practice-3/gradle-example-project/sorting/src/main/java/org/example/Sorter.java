package org.example;

import java.util.Collection;

public interface Sorter {
    <T extends Comparable<T>> void sort(T[] arr);
}
