package sorting.information.bucket;

import sorting.information.SortingInformation;

public class BucketSortInformation extends SortingInformation {
    public static BucketSortDistribution putInBucket( int index, int bucket, int placeValue ) {
        return BucketSortDistribution.make( index, bucket, placeValue );
    }

    public static BucketSortCollection collect( int targetIndex, int value ) {
        return BucketSortCollection.make( targetIndex, value );
    }
}
