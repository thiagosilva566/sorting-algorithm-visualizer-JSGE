package sorting;

import utils.ArrayUtils;

import java.util.LinkedList;
import java.util.Queue;

import static utils.ArrayUtils.swap;

public class SortingAlgorithms {

    private static Queue<int[]> sortingArrays;

    public static Queue<SortingInformation> selectionSort( int[] array ) {

        Queue<SortingInformation> sortingInformation = new LinkedList<>();

        int n = array.length;
        for ( int i = 0; i < n; i++ ) {

            int min = i;

            for ( int j = i + 1; j < n; j++ ) {

                SortingInformation currentSortingInformation = new SortingInformation( min, j );

                if ( array[j] < array[min] ) {
                    min = j;
                    currentSortingInformation.setTypeComparison(TypeComparison.SUCCESS);
                } else {
                    currentSortingInformation.setTypeComparison(TypeComparison.FAILURE);
                }

                sortingInformation.add(currentSortingInformation);
            }

            SortingInformation swapInformation = swap( array, i, min );
            sortingInformation.add( swapInformation );
        }

        return sortingInformation;

    }

    public static Queue<SortingInformation> insertionSort( int[] array ) {

        Queue<SortingInformation> sortingInformation = new LinkedList<>();

        int n = array.length;
        for ( int i = 1; i < n; i++ ) {
            int j = i;
            while ( j > 0 && array[j-1] > array[j] ) {
                SortingInformation swapInformation = swap( array, j-1, j );
                sortingInformation.add( swapInformation );
                j--;
            }
            // this may return a negative number index (most likely -1)
            sortingInformation.add(new SortingInformation(
                    j,
                    j - 1,
                    TypeComparison.FAILURE
            ));
        }

        return sortingInformation;
    }

    public static Queue<SortingInformation> shellSort( int[] array ) {

        Queue<SortingInformation> sortingInformation = new LinkedList<>();

        int h = 1;
        int n = array.length;
        while ( h < n / 3 ) {
            h = 3 * h + 1; // 1, 4, 13, 40...
        }
        while ( h >= 1 ) {
            for ( int i = h; i < n; i++ ){
                int j = i;
                while ( j >= h && array[j-h] > array[j] ) {
                    SortingInformation swapInformation = swap( array, j-h, j );
                    sortingInformation.add( swapInformation );
                    j = j - h;
                }
                sortingInformation.add(new SortingInformation( j,
                        j - h, TypeComparison.FAILURE ) );
            }
            h = h / 3;
        }

        return sortingInformation;
    }

    public static Queue<int[]> mergeSort( int[] array ) {
        int n = array.length;
        int[] tempMS = new int[n];

        sortingArrays = new LinkedList<>();
        sortingArrays.add(ArrayUtils.copy(array));

        mergeSort( array, 0, n - 1, tempMS );

        sortingArrays.add(ArrayUtils.copy(array));

        return sortingArrays;
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

            sortingArrays.add(ArrayUtils.copy(array));

        }
    }

    public static Queue<int[]> bucketSort( int[] array ) {

        sortingArrays =  new LinkedList<>();
        sortingArrays.add(ArrayUtils.copy(array));

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
                    sortingArrays.add(ArrayUtils.copy(array));
                }
                c[i] = 0;
            }
            t2 = t1;
            t1 *= 10;
        }

        return  sortingArrays;
    }

    public static Queue<int[]> countingSort( int[] array ) {
        return countingSort( array, ArrayUtils.getMaxElement(array) );
    }

    /*
    * this method should receive the value of the first element in the array.
    * The way the information about the sorting is returned does not accurately
    * reflect this method.
    */

    private static Queue<int[]> countingSort( int[] array, int maxValue ) {
        int n = array.length;
        int[] c = new int[maxValue+1];
        int[] b = new int[n];

        sortingArrays =  new LinkedList<>();
        sortingArrays.add(ArrayUtils.copy(array));
        int[] copyArray = ArrayUtils.copy(array);

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

            copyArray[c[array[i]]] = array[i];
            sortingArrays.add(ArrayUtils.copy(copyArray));
        }

        System.arraycopy( b, 0, array, 0, n );

        return sortingArrays;
    }
}
