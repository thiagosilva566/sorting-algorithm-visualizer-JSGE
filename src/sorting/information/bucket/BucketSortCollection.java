package sorting.information.bucket;

public class BucketSortCollection extends BucketSortDistribution {

    private int targetIndex;
    private int value;

    protected static BucketSortCollection make( int targetIndex, int value ) {
        BucketSortCollection information = new BucketSortCollection();
        information.targetIndex = targetIndex;
        information.value = value;
        return information;
    }

    public int getTargetIndex() {
        return targetIndex;
    }

    public int getValue() {
        return value;
    }
}
