package sorting;

import java.util.ArrayList;
import java.util.List;

import static utils.ArrayUtils.swap;

public class SortingAlgorithms {

    public static List<SortingInformation> selectionSort( int[] array ) {

        List<SortingInformation> sortingInformations = new ArrayList<>();

        int n = array.length;
        for ( int i = 0; i < n; i++ ) {

            int min = i;

            for ( int j = i + 1; j < n; j++ ) {

                SortingInformation sortingInformation = new SortingInformation( min, j );

                if ( array[j] < array[min] ) {
                    min = j;
                    sortingInformation.setTypeComparison(TypeComparison.SUCCESS);
                } else {
                    sortingInformation.setTypeComparison(TypeComparison.FAILURE);
                }

                sortingInformations.add(sortingInformation);
            }

            SortingInformation swapInformation = swap( array, i, min );
            sortingInformations.add( swapInformation );
        }

        return sortingInformations;

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

    public static void mergeSort( int[] array ) {
        int n = array.length;
        int[] tempMS = new int[n];
        mergeSort( array, 0, n - 1, tempMS );
    }

    private static void mergeSort( int[] array, int start, int end, int[] tempMS ) {
        int middle;
        if ( start < end ) {
            middle = ( start + end ) / 2;
            mergeSort( array, start, middle, tempMS ); // esquerda
            mergeSort( array, middle + 1, end, tempMS ); // direita
            merge( array, start, middle, end, tempMS ); // intercalação
        }
    }

    private static void merge( int[] array, int start, int middle, int end, int[] tempMS ) {
        int i = start;
        int j = middle + 1;
        for ( int k = start; k <= end; k++ ) {
            tempMS[k] = array[k];
        }
        for ( int k = start; k <= end; k++ ) {
            if ( i > middle ) {
                array[k] = tempMS[j++];
            } else if ( j > end ) {
                array[k] = tempMS[i++];
            } else if ( tempMS[j] < tempMS[i] ) {
                array[k] = tempMS[j++];
            } else {
                array[k] = tempMS[i++];
            }
        }
    }

    public static void bucketSort( int[] array ) {
        int n = array.length;
        final int K = 10;
        int[][] buckets = new int[K][n];
        int[] c = new int[K];
        int t1 = 10;
        int t2 = 1;
        int max = -1;
        boolean first = true;
        while ( max < 0 || max / t2 != 0 ) {
            // distribuição
            for ( int i = 0; i < n; i++ ) {
                int p = array[i] % t1 / t2;
                buckets[p][c[p]++] = array[i];
                if ( first ) {
                    max = max < array[i] ? array[i] : max;
                }
            }
            first = false;
            // coleta
            int k = 0;
            for ( int i = 0; i < K; i++ ) {
                for ( int j = 0; j < c[i]; j++ ) {
                    array[k++] = buckets[i][j];
                }
                c[i] = 0;
            }
            t2 = t1;
            t1 *= 10;
        }
    }
    // this method should receive the value of the first element in the array
    public static void countingSort( int[] array, int maxValue ) {
        int n = array.length;
        int[] c = new int[maxValue+1];
        int[] b = new int[n];

        // contagem
        for ( int i = 0; i < n; i++ ) {
            c[array[i]]++;
        }
        // acumulação
        for ( int i = 1; i <= maxValue; i++ ) {
            c[i] += c[i-1];
        }
        // reposicionamento
        for ( int i = n-1; i >= 0; i-- ) {
            c[array[i]]--;
            b[c[array[i]]] = array[i];
        }
        System.arraycopy( b, 0, array, 0, n );
    }
}
