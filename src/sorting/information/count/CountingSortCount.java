package sorting.information.count;

public class CountingSortCount extends CountingSortInformation {

    private int arrayIndex;
    private int countIndex;

    protected static CountingSortCount makeInstance( int arrayIndex, int countIndex ) {
        CountingSortCount instance = new CountingSortCount();
        instance.arrayIndex = arrayIndex;
        instance.countIndex = countIndex;
        return instance;
    }

    public int getArrayIndex() {
        return arrayIndex;
    }

    public int getCountIndex() {
        return countIndex;
    }
}
