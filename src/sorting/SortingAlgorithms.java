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

    public static void insertionSort( int[] array ) {
        int n = array.length;
        for ( int i = 1; i < n; i++ ) {
            int j = i;
            while ( j > 0 && array[j-1] > array[j] ) {
                swap( array, j-1, j );
                j--;
            }
        }
    }

    public static void shellSort( int[] array ) {
        int h = 1;
        int n = array.length;
        while ( h < n / 3 ) {
            h = 3 * h + 1; // 1, 4, 13, 40...
        }
        while ( h >= 1 ) {
            for ( int i = h; i < n; i++ ){
                int j = i;
                while ( j >= h && array[j-h] > array[j] ) {
                    swap( array, j-h, j );
                    j = j - h;
                }
            }
            h = h / 3;
        }
    }

    public static void mergeSort() {

    }

    public static void bucketSort() {

    }

    public static void quickSort() {

    }
}
