package sorting;

import static utils.ArrayUtils.swap;

public class SortingAlgorithms {

    public static void selectionSort( int[] array ) {
        int n = array.length;
        for ( int i = 0; i < n; i++ ) {
            int min = i;
            for ( int j = i + 1; j < n; j++ ) {
                if ( array[j] < array[min] ) {
                    min = j;
                }
            }
            swap( array, i, min );
        }

    }

    public static void insertionSort() {

    }

    public static void ShellSort() {

    }

    public static void MergeSort() {

    }

    public static void bucketSort() {

    }

    public static void quickSort() {

    }
}
