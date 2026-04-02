package core;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.geom.RoundRectangle;
import br.com.davidbuzatto.jsge.imgui.GuiButton;
import br.com.davidbuzatto.jsge.imgui.GuiComponent;
import br.com.davidbuzatto.jsge.imgui.GuiDropdownList;
import br.com.davidbuzatto.jsge.math.Vector2;
import sorting.SortingAlgorithms;
import sorting.information.SortingInformation;
import sorting.information.bucket.BucketSortCollection;
import sorting.information.bucket.BucketSortDistribution;
import sorting.information.bucket.BucketSortInformation;
import sorting.information.count.CountingSortAccumulation;
import sorting.information.count.CountingSortCount;
import sorting.information.count.CountingSortInformation;
import sorting.information.count.CountingSortRepositioning;
import utils.ArrayUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class Simulator extends EngineFrame {

    private static final int SHUFFLE_COUNT = 200;

    private SimulatorStatus simulatorStatus;

    private int[] array;

    Queue<int[]> sortingArrays;
    private int[] cumulationArray;
    private Queue<SortingInformation> sortingInformationQueue;
    private List<Element> elementList;

    private RoundRectangle sortingContainer;
    private RoundRectangle controlsContainer;
    private RoundRectangle countSortContainer;
    private double roundness;
    private Vector2 containersGap;

    private List<GuiComponent> components;
    private int numberComponents;
    private GuiDropdownList sortingAlgorithmList;
    private GuiButton startSortButton;
    private GuiButton shuffleButton;

    private Vector2 gapBetweenArrayElements;
    private Vector2 sizeUnity;
    private Vector2 gapBetweenComponents;
    private Vector2 componentsSize;

    private Color backgroundColor;
    private Color containerColor;
    private Color borderContainerColor;
    private Color backgroundComponentColor;
    private Color borderComponentColor;
    private Color defaultElementColor;

    private double counter;
    private double timeToAdvance;

    public Simulator() {
        
        super(
            1600,
            900,
            "Sorting Visualizer",
            60,
            true,
            false,
            false,
            false,
            false,
            false
        );
        
    }

    @Override
    public void create() {

        simulatorStatus = SimulatorStatus.STOPPED;

        int n = 100;
        array = new int[n];
        for ( int i = 1; i <= n; i++ ) {
            array[i - 1] = i;
        }
        ArrayUtils.shuffle( array, 200 );

        containersGap = new Vector2(
                200,
                50
        );

        roundness = 20;

        controlsContainer = new RoundRectangle(
                containersGap.x,
                containersGap.y,
                getScreenWidth() - 2 * containersGap.x,
                (getScreenHeight() - 3 * containersGap.y) * 0.3,
                roundness
        );

        sortingContainer = new RoundRectangle(
                containersGap.x,
                controlsContainer.y + controlsContainer.height + containersGap.y,
                getScreenWidth() - 2 *  containersGap.x,
                (getScreenHeight() - 3 * containersGap.y) * 0.7,
                roundness
        );

        countSortContainer = new RoundRectangle(
                containersGap.x + sortingContainer.width + 20,
                sortingContainer.y,
                160,
                (getScreenHeight() - 3 * containersGap.y) * 0.7,
                roundness
        );

        gapBetweenArrayElements = new Vector2(
                -8.0/90 * array.length + 98/9.0,
                20
        );

        int biggerElement = ArrayUtils.getMaxElement(array);
        sizeUnity = new Vector2(
                (sortingContainer.width - (gapBetweenArrayElements.x * array.length + gapBetweenArrayElements.x)) / array.length,
                (sortingContainer.height - 2 * gapBetweenArrayElements.y) / biggerElement
        );

        numberComponents = 3;

        gapBetweenComponents = new Vector2(
                50,
                70
        );

        componentsSize = new Vector2(
                (controlsContainer.width - (1 + numberComponents) * gapBetweenComponents.x) / numberComponents,
                controlsContainer.height - 2 * gapBetweenComponents.y
        );

        double xIni = controlsContainer.x + gapBetweenComponents.x;
        double yIni =  controlsContainer.y + gapBetweenComponents.y;
        double gapX = componentsSize.x + gapBetweenComponents.x;

        sortingAlgorithmList = new GuiDropdownList(
                xIni,
                yIni,
                componentsSize.x,
                componentsSize.y,
                List.of(
                        "SelectionSort",
                        "InsertionSort",
                        "ShellSort",
                        "MergeSort",
                        "BucketSort",
                        "CountingSort"
                ),
                this
        );

        startSortButton = new GuiButton(
                xIni + 1 * gapX,
                yIni,
                componentsSize.x,
                componentsSize.y,
                "Start",
                this
        );

        shuffleButton = new GuiButton(
                xIni + 2 * gapX,
                yIni,
                componentsSize.x,
                componentsSize.y,
                "Shuffle",
                this
        );

        components = new ArrayList<>();
        components.add(sortingAlgorithmList);
        components.add(startSortButton);
        components.add(shuffleButton);

        backgroundColor = Color.decode("#282A36" );
        containerColor = Color.decode("#6272A4" );
        borderContainerColor = Color.decode("#FF79C6" );
        backgroundComponentColor = LIGHTGRAY;
        defaultElementColor = LIGHTGRAY;

        for ( GuiComponent component : components ) {
            component.setBackgroundColor(backgroundComponentColor);
        }

        /*
        * That's there because collectToDraw() needs calculateSizeUnity() and
        * calculateSizeUnity() needs the container's dimensions initialized
        */

        calculateSizeUnity();
        collectToDraw();

    }

    @Override
    public void update( double delta ) {

        for ( GuiComponent component : components ) {
            component.update( delta );
        }
        counter += delta;

        switch( simulatorStatus ) {
            case STOPPED -> {
                sortingAlgorithmList.setEnabled(true);
                shuffleButton.setEnabled(true);
                startSortButton.setText("Start");

                if ( startSortButton.isMousePressed() ) {
                    int[] arrayCopy = ArrayUtils.copy( array );
                    switch (sortingAlgorithmList.getSelectedItemText()) {
                        case "SelectionSort" -> sortingArrays = SortingAlgorithms.selectionSort(arrayCopy);
                        case "InsertionSort" -> sortingArrays = SortingAlgorithms.insertionSort(arrayCopy);
                        case "ShellSort" -> sortingArrays = SortingAlgorithms.shellSort(arrayCopy);
                        case "MergeSort" -> sortingArrays = SortingAlgorithms.mergeSort(arrayCopy);
                        case "BucketSort" -> sortingInformationQueue = SortingAlgorithms.bucketSort(arrayCopy);
                        case "CountingSort" -> {
                            sortingInformationQueue = SortingAlgorithms.countingSort(arrayCopy);
                        }
                    }
                    simulatorStatus = SimulatorStatus.RUNNING_SORTING;
                }

                if ( sortingAlgorithmList.isDropdownListVisible() ) {
                    simulatorStatus = SimulatorStatus.SELECTING_SORT_TYPE;
                }

            }
            case SELECTING_SORT_TYPE -> {
                if ( !sortingAlgorithmList.isDropdownListVisible() ) {
                    switch (sortingAlgorithmList.getSelectedItemText()) {
                        case "MergeSort" -> generateArray( 256, 256 );
                        case "CountingSort" -> {
                            generateArray(100, 5);
                            cumulationArray = new int[ArrayUtils.getMaxElement(array) + 1];
                        }
                        default -> generateArray( 100, 100 );
                    }
                    simulatorStatus = SimulatorStatus.STOPPED;
                }
            }
            case RUNNING_SORTING -> {
                sortingAlgorithmList.setEnabled(false);
                shuffleButton.setEnabled(false);
                startSortButton.setText("Skip");

                switch (sortingAlgorithmList.getSelectedItemText()) {
                    case "MergeSort", "SelectionSort", "InsertionSort" -> timeToAdvance = 0.00001;
                    case "BucketSort", "CountingSort" -> timeToAdvance = 0.2;
                    default -> timeToAdvance = 0.1; // shellSort
                }

                if  ( startSortButton.isMousePressed() ) {
                    // have to update together
                    while ( !sortingArrays.isEmpty() ) {
                        array = sortingArrays.poll();
                    }
                    collectToDraw();
                }

                if ( counter > timeToAdvance ){
                    if (sortingArrays != null && !sortingArrays.isEmpty()) {
                        updateSortingArrays();
                    } else if (sortingInformationQueue != null && !sortingInformationQueue.isEmpty()) {
                        updateSortingInformationQueue();
                    } else {
                        simulatorStatus = SimulatorStatus.STOPPED;
                    }
                    counter = 0;
                }
            }
        }

        if ( shuffleButton.isMousePressed() ) {
            // have to update together
            ArrayUtils.shuffle(array, 100);
            collectToDraw();
        }

    }

    @Override
    public void draw() {
        
        clearBackground( backgroundColor );


        fillRoundRectangle( controlsContainer, containerColor );
        fillRoundRectangle( sortingContainer, containerColor );

        setStrokeLineWidth(4);
        drawRoundRectangle( controlsContainer, borderContainerColor );
        drawRoundRectangle( sortingContainer, borderContainerColor );
        setStrokeLineWidth(WIDTH);

        for ( Element element : elementList ) {
            fillRectangle( element.rectangle, element.color );
        }

        for ( GuiComponent component : components ) {
            component.draw();
        }

        if (Objects.equals(sortingAlgorithmList.getSelectedItemText(), "CountingSort") && cumulationArray != null) {
            fillRoundRectangle( countSortContainer, containerColor );
            setStrokeLineWidth(4);
            drawRoundRectangle( countSortContainer, borderContainerColor );
            Vector2 gap = new  Vector2(
                    10, 10
            );
            Vector2 size = new Vector2(
                    countSortContainer.width - 2 * gap.x,
                    (countSortContainer.height - (( cumulationArray.length ) * gap.y)) /
                            (cumulationArray.length - 1)
            );
            double xIni = countSortContainer.x + gap.x;
            double yIni = (countSortContainer.y + gap.y);
            for ( int i = 1; i < cumulationArray.length; i++ ) {
                float hue = (float) i / ArrayUtils.getMaxElement(array);
                // Saturation e Brightness em 1.0f (cores vivas)
                Color color = Color.getHSBColor(hue, 1.0f, 1.0f);
                drawRectangle(
                        xIni,
                        yIni + ( i - 1) * (gap.y + size.y),
                        size.x,
                        size.y,
                        color
                );
                int fontSize = 20;
                String text = String.valueOf(cumulationArray[i]);
                Rectangle textBounds = measureTextBounds( text, fontSize );
                double x = xIni + size.x / 2 - textBounds.width / 2;
                double y = (yIni + ( i - 1) * (gap.y + size.y)) + size.y / 2 - textBounds.height / 4;
                drawText( text, x, y, fontSize, WHITE);
            }
            setStrokeLineWidth(1);
        }

        drawFPS( 20, 20 );
    
    }

    private void calculateSizeUnity() {

        gapBetweenArrayElements = new Vector2(
                -1.0/52 * array.length + 77/13.0,
                20
        );

        int biggerElement =  ArrayUtils.getMaxElement(array);

        sizeUnity = new Vector2(
                (sortingContainer.width -
                        (gapBetweenArrayElements.x * array.length + gapBetweenArrayElements.x)) / array.length,
                (sortingContainer.height - 2 * gapBetweenArrayElements.y) / biggerElement
        );
    }

    private void collectToDraw() {
        elementList = new ArrayList<>();
        for ( int i = 0; i < array.length; i++ ) {
            int n = array[i];
            double xIni = sortingContainer.x + gapBetweenArrayElements.x;
            double yIni = sortingContainer.y + sortingContainer.height - gapBetweenArrayElements.y;
            Rectangle rectangle = new Rectangle(
                    xIni + i * (sizeUnity.x + gapBetweenArrayElements.x),
                    yIni - n * sizeUnity.y,
                    sizeUnity.x,
                    sizeUnity.y * n
            );
            Element element = new  Element();
            element.rectangle = rectangle;
            element.color = defaultElementColor;
            elementList.add(element);
        }
    }

    private void updateSortingInformationQueue() {
        if ( sortingInformationQueue != null && !sortingInformationQueue.isEmpty() ) {
            SortingInformation information = sortingInformationQueue.poll();
            switch (information) {
                case BucketSortInformation bsi -> {
                    switch (bsi) {
                        case BucketSortCollection bsc -> {
                            int index = bsc.getTargetIndex();
                            int value = bsc.getValue();
                            array[index] = value;
                            collectToDraw();
                        }
                        case BucketSortDistribution bsd -> {
                            int index = bsd.getIndex();
                            int bucket = bsd.getBucket();
                            // Hue varia de 0.0 a 1.0
                            float hue = (float) bucket / 10;
                            // Saturation e Brightness em 1.0f (cores vivas)
                            Color cor = Color.getHSBColor(hue, 1.0f, 1.0f);
                            elementList.get(index).color = cor;
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + bsi);
                    }
                }
                case CountingSortInformation csi -> {
                    switch (csi) {
                        case CountingSortCount csc -> {
                            int arrayIndex = csc.getArrayIndex();
                            int countIndex= csc.getCountIndex();

                            cumulationArray[countIndex]++;

                            float hue = (float) countIndex / ArrayUtils.getMaxElement(array);
                            // Saturation e Brightness em 1.0f (cores vivas)
                            Color cor = Color.getHSBColor(hue, 1.0f, 1.0f);
                            elementList.get(arrayIndex).color = cor;
                        }
                        case CountingSortAccumulation csa -> {
                            int countIndex = csa.getCountIndex();
                            int valueAdded = csa.getValueAdded();

                            cumulationArray[countIndex] += valueAdded;
                        }
                        case CountingSortRepositioning csr -> {

                            int countDecrementIndex = csr.getCountDecrementIndex();
                            int targetArrayIndex = csr.getTargetArrayIndex();
                            int value = csr.getValue();

                            for ( int i = countDecrementIndex; i < cumulationArray.length; i++ ) {
                                cumulationArray[i]--;
                            }

                            array[targetArrayIndex] = value;

                            collectToDraw();

                        }
                        default -> throw new IllegalStateException("Unexpected value: " + csi);
                    }
                }
                default -> throw new IllegalStateException("Unexpected value: " + information);
            }
        } else {
            simulatorStatus =  SimulatorStatus.STOPPED;
        }
    }

    private void generateArray( int numberElements, int maxElement ) {
        array = new int[numberElements];
        int a = 1;
        for ( int i = 0; i < numberElements; i++ ) {
            array[i] = a++;
            if ( a > maxElement) {
                a = 1;
            }
        }
        ArrayUtils.shuffle(array, SHUFFLE_COUNT);
        calculateSizeUnity();
        collectToDraw();
    }

    private void updateSortingArrays() {
        // have to update together
        array = sortingArrays.poll();
        collectToDraw();
    }


    public static void main( String[] args ) {
        new Simulator();
    }
    
}
