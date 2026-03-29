package utils;

import br.com.davidbuzatto.jsge.math.MathUtils;

public class ArrayUtils {

    public static void swap(int[] array, int i, int j ) {
        int temp = array[i];
        array[i] = array[j];
    }

    public static int[] copy( int[] array ) {
        int[] copy = new int[array.length];
        System.arraycopy( array, 0, copy, 0, array.length );
        return copy;
    }

    public static int getMaxElement( int[] array ) {
        int max = Integer.MIN_VALUE;
        for ( int i = 0; i < array.length; i++ ) {
            if ( array[i] > max ) {
                max = array[i];
            }
        }
        return max;
    }

    public static void shuffle( int[] array, int count ) {
        for  ( int i = 0; i < count; i++ ) {
            int a = MathUtils.getRandomValue( 0, array.length - 1 );
            int b = MathUtils.getRandomValue( 0, array.length - 1 );
            swap( array, a, b );
        }
    }
}
