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
import utils.ArrayUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Simulator extends EngineFrame {

    private SimulatorStatus simulatorStatus;

    private int[] array;

    Queue<int[]> sortingArrays;
    private Queue<SortingInformation> sortingInformationQueue;
    private List<Element> elementList;

    private RoundRectangle sortingContainer;
    private RoundRectangle controlsContainer;
    private double roundness;
    private Vector2 containersGap;

    private List<GuiComponent> components;
    private int numberComponents;
    private GuiDropdownList sortingAlgorithmList;
    private GuiDropdownList numberElementsList;
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

        int n = 10;
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

        gapBetweenArrayElements = new Vector2(
                -8.0/90 * array.length + 98/9.0,
                20
        );

        int biggerElement = ArrayUtils.getMaxElement(array);
        sizeUnity = new Vector2(
                (sortingContainer.width - (gapBetweenArrayElements.x * array.length + gapBetweenArrayElements.x)) / array.length,
                (sortingContainer.height - 2 * gapBetweenArrayElements.y) / biggerElement
        );

        numberComponents = 4;

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

        numberElementsList = new GuiDropdownList(
                xIni + gapX,
                yIni,
                componentsSize.x,
                componentsSize.y,
                List.of(
                        "10",
                        "20",
                        "50",
                        "100"
                ),
                this
        );

        startSortButton = new GuiButton(
                xIni + 2 * gapX,
                yIni,
                componentsSize.x,
                componentsSize.y,
                "Start",
                this
        );

        shuffleButton = new GuiButton(
                xIni + 3 * gapX,
                yIni,
                componentsSize.x,
                componentsSize.y,
                "Shuffle",
                this
        );



        components = new ArrayList<>();
        components.add(sortingAlgorithmList);
        components.add(numberElementsList);
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
        System.out.printf("counter: %f, delta: %f, counter + delta: %f\n", counter, delta, counter + delta);
        counter += delta;

        switch( simulatorStatus ) {
            case STOPPED -> {
                sortingAlgorithmList.setEnabled(true);
                numberElementsList.setEnabled(true);
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
                        case "CountingSort" -> sortingArrays = SortingAlgorithms.countingSort(arrayCopy);
                    }
                    simulatorStatus = SimulatorStatus.RUNNING_SORTING;
                }

            }
            case RUNNING_SORTING -> {
                sortingAlgorithmList.setEnabled(false);
                numberElementsList.setEnabled(false);
                shuffleButton.setEnabled(false);
                startSortButton.setText("Skip");

                if  ( startSortButton.isMousePressed() ) {
                    // have to update together
                    while ( !sortingArrays.isEmpty() ) {
                        array = sortingArrays.poll();
                    }
                    collectToDraw();
                }

                if ( counter > 0.016 ){
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

        if ( numberElementsList.getSelectedItemText() != null &&
                array.length != Integer.parseInt(numberElementsList.getSelectedItemText()) ) {
            // have to update together
            int n = Integer.parseInt(numberElementsList.getSelectedItemText());
            array = new int[n];
            switch (sortingAlgorithmList.getSelectedItemText()) {
                case "CountingSort" -> {
                    int a = 1;
                    for ( int i = 0; i < n; i++ ) {
                        array[i] = a++;
                        if ( a > 10 ) {
                            a = 1;
                        }
                    }
                }
                default -> {
                    for ( int i = 0; i < n; i++ ) {
                        array[i] = i + 1;
                    }
                }
            }
            ArrayUtils.shuffle( array, 200 );
            calculateSizeUnity();
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

        drawFPS( 20, 20 );
    
    }

    private void calculateSizeUnity() {

        gapBetweenArrayElements = new Vector2(
                -8.0/90 * array.length + 98/9.0,
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
                default -> throw new IllegalStateException("Unexpected value: " + information);
            }
        } else {
            simulatorStatus =  SimulatorStatus.STOPPED;
        }
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
