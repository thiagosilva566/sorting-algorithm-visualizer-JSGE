package utils;

import sorting.SortingInformation;
import sorting.TypeComparison;

public class ArrayUtils {

    public static SortingInformation swap(int[] array, int i, int j ) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        return new SortingInformation(
                i,
                j,
                TypeComparison.SWAP
        );
    }
}
