package sorting.information.count;

public class CountingSortRepositioning extends CountingSortInformation {
    private int countDecrementIndex;
    private int targetArrayIndex;
    private int value;

    protected static CountingSortRepositioning makeInstance( int countDecrementIndex, int targetArrayIndex, int value ) {
        CountingSortRepositioning instance = new CountingSortRepositioning();
        instance.countDecrementIndex = countDecrementIndex;
        instance.targetArrayIndex = targetArrayIndex;
        instance.value = value;
        return instance;
    }

    public int getCountDecrementIndex() {
        return countDecrementIndex;
    }

    public int getTargetArrayIndex() {
        return targetArrayIndex;
    }

    public int getValue() {
        return value;
    }
}
