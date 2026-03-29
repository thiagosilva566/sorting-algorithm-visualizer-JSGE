package sorting;

public class SortingInformation {

    private int currentElement;
    private int currentComparator;
    private TypeComparison typeComparison;

    public SortingInformation(int currentElement, int currentComparator, TypeComparison typeComparison) {
        this.currentElement = currentElement;
        this.currentComparator = currentComparator;
        this.typeComparison = typeComparison;
    }

    public SortingInformation(int currentElement, int currentComparator) {
        this.currentElement = currentElement;
        this.currentComparator = currentComparator;
    }

    @Override
    public String toString() {
        return  String.format("CurrentElement: %d, currentComparator: %d, typeComparison: %s",
                currentElement, currentComparator, typeComparison);
    }

    public int getCurrentElement() {
        return currentElement;
    }

    public int getCurrentComparator() {
        return currentComparator;
    }

    public TypeComparison getTypeComparison() {
        return typeComparison;
    }

    public void setTypeComparison(TypeComparison typeComparison) {
        this.typeComparison = typeComparison;
    }
}
