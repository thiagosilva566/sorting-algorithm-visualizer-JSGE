package core;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.math.Vector2;
import sorting.SortingAlgorithms;
import sorting.SortingInformation;
import utils.ArrayUtils;

import java.awt.*;
import java.util.List;

public class Simulator extends EngineFrame {

    private enum AnimationStatus {
        RUNNING,
        STOPPED,
    }

    private static final int GRID_SIZE = 40;

    private int[] array;

    private List<SortingInformation> sortingInformations;
    private int sortingInformationsCurrentIndex;

    private double xIni;
    private double yIni;
    private double gap;

    private int currentElementIndex;
    private int currentComparatorIndex;

    private double counter;
    private AnimationStatus animationStatus;
    private Rectangle currentElement;
    private Rectangle targetElement;
    private Rectangle currentComparator;
    private Rectangle targetComparator;

    public Simulator() {
        
        super(
            1600,                 // largura                      / width
            900,                 // algura                       / height
            "Window Title",      // título                       / title
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
        // 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        array = new int[]{ 10, 7, 9, 8, 4, 1, 2, 5, 3, 6 };
        int[] copyArray = ArrayUtils.copy( array );
        sortingInformations = SortingAlgorithms.selectionSort( copyArray );

        gap = 10;
        xIni = getScreenWidth() / 2.0 - ((array.length ) * GRID_SIZE) / 2;
        yIni = getScreenHeight() / 2.0 + (GRID_SIZE * ArrayUtils.getMaxElement(array)) / 2;

        animationStatus = AnimationStatus.STOPPED;

    }

    @Override
    public void update( double delta ) {

        counter += delta;

        if ( animationStatus == AnimationStatus.RUNNING ) {

        } else if ( animationStatus == AnimationStatus.STOPPED ) {

            if ( counter >= 1 && sortingInformationsCurrentIndex < sortingInformations.size() ) {

                SortingInformation sortingInformation = sortingInformations.get( sortingInformationsCurrentIndex );

                switch (sortingInformation.getTypeComparison()) {
                    case SWAP -> {
                        ArrayUtils.swap(
                                array,
                                sortingInformation.getCurrentElement(),
                                sortingInformation.getCurrentComparator()
                        );
                    }
                    case FAILURE -> {
                        currentElementIndex = sortingInformation.getCurrentElement();
                        currentComparatorIndex = sortingInformation.getCurrentComparator();
                        if ( currentComparator == null ) {
                            currentComparator = getElementRectangle( currentComparatorIndex );
                        } else {
                            targetComparator = getElementRectangle( currentComparatorIndex );
                        }
                    }
                    case SUCCESS -> {
                        currentElementIndex = sortingInformation.getCurrentElement();
                        currentComparatorIndex = sortingInformation.getCurrentComparator();
                        if ( currentElement == null ) {
                            currentElement = getElementRectangle( currentComparatorIndex );
                        } else {
                            targetElement = getElementRectangle( currentComparatorIndex );
                        }
                    }
                }

                animationStatus = AnimationStatus.RUNNING;
                sortingInformationsCurrentIndex++;
                counter = 0;
            }

        }


    }

    @Override
    public void draw() {
        
        clearBackground( WHITE );

        for ( int i = 0; i < array.length; i++ ) {
            int n = array[i];
            Color color = Color.LIGHT_GRAY;

            if ( i == currentElementIndex ) {
                color = Color.GREEN;
            } else if ( i == currentComparatorIndex ) {
                color = Color.RED;
            }

            Vector2 coordinate = getElementCoordinates(i);

            double width = GRID_SIZE;
            double height = GRID_SIZE * n;

            fillRectangle(
                    coordinate,
                    width,
                    height,
                    LIGHTGRAY
            );

            if ( i == currentElementIndex || i ==  currentComparatorIndex ) {

                setStrokeLineWidth(3);

                drawRectangle(
                        coordinate,
                        width,
                        height,
                        color
                );

                setStrokeLineWidth(1);
            }
        }
        
        drawFPS( 20, 20 );
    
    }

    private Vector2 getElementCoordinates( int index ) {
        return new Vector2(
                xIni + ( GRID_SIZE + 10 ) * index,
                yIni - GRID_SIZE * array[index]
        );
    }

    private Rectangle getElementRectangle( int index ) {
        Vector2 coordinate = getElementCoordinates( index );
        return new Rectangle(
                coordinate.x,
                coordinate.y,
                GRID_SIZE,
                GRID_SIZE * array[index]
        );
    }

    public static void main( String[] args ) {
        new Simulator();
    }
    
}
