package sorting.information.count;

public class CountingSortAccumulation extends CountingSortInformation {

    private int countIndex;
    private int valueAdded;

    protected static CountingSortAccumulation makeInstance( int countIndex, int valueAdded ) {
        CountingSortAccumulation instance = new CountingSortAccumulation();
        instance.countIndex = countIndex;
        instance.valueAdded = valueAdded;
        return instance;
    }

    public int getCountIndex() {
        return countIndex;
    }

    public int getValueAdded() {
        return valueAdded;
    }
}
