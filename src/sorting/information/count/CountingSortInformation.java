package sorting.information.count;

import sorting.information.SortingInformation;

public class CountingSortInformation extends SortingInformation {

    public static CountingSortCount count( int arrayIndex, int countIndex ) {
        return CountingSortCount.makeInstance( arrayIndex, countIndex );
    }

    public static CountingSortAccumulation accumulate( int countIndex, int valueAdded ) {
        return CountingSortAccumulation.makeInstance( countIndex, valueAdded );
    }

    public static CountingSortRepositioning reposition( int countDecrementIndex, int targetArrayIndex, int value ) {
        return CountingSortRepositioning.makeInstance( countDecrementIndex, targetArrayIndex, value );
    }

}
