package sorting.information.bucket;

public class BucketSortDistribution extends BucketSortInformation {

    private int index;
    private int bucket;
    private int placeValue;

    protected static BucketSortDistribution make( int index, int bucket, int placeValue ) {
        BucketSortDistribution information = new BucketSortDistribution();
        information.index = index;
        information.bucket = bucket;
        information.placeValue = placeValue;
        return information;
    }

    public int getIndex() {
        return index;
    }

    public int getBucket() {
        return bucket;
    }
}
