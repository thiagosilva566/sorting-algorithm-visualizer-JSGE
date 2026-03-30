package core;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.RoundRectangle;
import br.com.davidbuzatto.jsge.imgui.GuiButton;
import br.com.davidbuzatto.jsge.imgui.GuiComponent;
import br.com.davidbuzatto.jsge.imgui.GuiDropdownList;
import br.com.davidbuzatto.jsge.math.Vector2;
import sorting.SortingAlgorithms;
import utils.ArrayUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Simulator extends EngineFrame {

    private SimulatorStatus simulatorStatus;

    private int[] array;

    Queue<int[]> sortingArrays;

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

    public Simulator() {
        
        super(
            1600,                 // largura                      / width
            900,                 // algura ,                      / height
            "Sorting Visualizer",      // título                       / title
            60,                  // quadros por segundo desejado / target FPS
            true,                // suavização                   / antialiasing
            false,               // redimensionável              / resizable
            false,               // tela cheia                   / full screen
            false,               // sem decoração                / undecorated
            false,               // sempre no topo               / always on top
            false                // fundo invisível              / invisible background
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

        for ( GuiComponent component : components ) {
            component.setBackgroundColor(backgroundComponentColor);
        }

    }

    @Override
    public void update( double delta ) {

        for ( GuiComponent component : components ) {
            component.update( delta );
        }

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
                        case "BucketSort" -> sortingArrays = SortingAlgorithms.bucketSort(arrayCopy);
                        case "CountingSort" -> sortingArrays = SortingAlgorithms.countingSort(arrayCopy);
                    }
                    simulatorStatus = SimulatorStatus.RUNNING_SORTING;
                }

                if ( shuffleButton.isMousePressed() ) {
                    ArrayUtils.shuffle(array, 100);
                }

            }
            case RUNNING_SORTING -> {
                sortingAlgorithmList.setEnabled(false);
                numberElementsList.setEnabled(false);
                shuffleButton.setEnabled(false);
                startSortButton.setText("Skip");

                if  ( startSortButton.isMousePressed() ) {
                    while ( !sortingArrays.isEmpty() ) {
                        array = sortingArrays.poll();
                    }
                }
            }
        }



        if ( numberElementsList.getSelectedItemText() != null &&
                array.length != Integer.parseInt(numberElementsList.getSelectedItemText()) ) {
            int n = Integer.parseInt(numberElementsList.getSelectedItemText());
            array = new int[n];
            for ( int i = 0; i < n; i++ ) {
                array[i] = i + 1;
            }
            ArrayUtils.shuffle( array, 200 );
            calculateSizeUnity();
        }

        if ( sortingArrays != null && !sortingArrays.isEmpty() ) {
            array = sortingArrays.poll();
        } else {
            simulatorStatus =  SimulatorStatus.STOPPED;
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

        for ( int i = 0; i < array.length; i++ ) {
            int n = array[i];
            double xIni = sortingContainer.x + gapBetweenArrayElements.x;
            double yIni = sortingContainer.y + sortingContainer.height - gapBetweenArrayElements.y;
            fillRectangle(
                    xIni + i * (sizeUnity.x + gapBetweenArrayElements.x),
                    yIni - n * sizeUnity.y,
                    sizeUnity.x,
                    sizeUnity.y * n,
                    LIGHTGRAY
            );
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


    public static void main( String[] args ) {
        new Simulator();
    }
    
}
