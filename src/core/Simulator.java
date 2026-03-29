package core;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.geom.RoundRectangle;
import br.com.davidbuzatto.jsge.math.Vector2;
import sorting.SortingAlgorithms;
import utils.ArrayUtils;

import java.util.Queue;

public class Simulator extends EngineFrame {

    private int[] array;
    private int[] currentArray;

    Queue<int[]> sortingArrays;

    private RoundRectangle sortingContainer;
    private Vector2 containerGap;

    private Vector2 gapBetweenElements;
    private Vector2 sizeUnity;

    public Simulator() {
        
        super(
            1600,                 // largura                      / width
            900,                 // algura ,                      / height
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

        int n = 100;
        array = new int[n];
        for ( int i = 1; i <= n; i++ ) {
            array[i - 1] = i;
        }
        ArrayUtils.shuffle( array, 200 );
        currentArray = ArrayUtils.copy( array );

        containerGap = new Vector2(
                200,
                200
        );

        gapBetweenElements = new Vector2(
                -8.0/90 * array.length + 98/9.0,
                20
        );

        int biggerElement = ArrayUtils.getMaxElement(array);
        sizeUnity = new Vector2(
                (getScreenWidth() -
                        ((((containerGap.x + gapBetweenElements.x) * 2) +
                                gapBetweenElements.x * array.length) - gapBetweenElements.x)) / array.length,
                (getScreenHeight() - (containerGap.y + gapBetweenElements.y) * 2) / biggerElement
        );

        sortingContainer = new RoundRectangle(
                containerGap.x,
                containerGap.y,
                getScreenWidth() - 2 *  containerGap.x,
                getScreenHeight() - 2 * containerGap.y,
                20
        );

    }

    @Override
    public void update( double delta ) {

        if ( isKeyPressed(KEY_S) )  {
            sortingArrays = SortingAlgorithms.mergeSort(ArrayUtils.copy(array));
        }

        if ( sortingArrays != null && !sortingArrays.isEmpty() ) {
            currentArray = sortingArrays.poll();
        }

    }

    @Override
    public void draw() {
        
        clearBackground( WHITE );

        setStrokeLineWidth(2);

        drawRoundRectangle( sortingContainer, BLACK );

        for ( int i = 0; i < currentArray.length; i++ ) {
            int n = currentArray[i];
            double yIni = containerGap.y + sortingContainer.height - gapBetweenElements.y;
            fillRectangle(
                    containerGap.x + gapBetweenElements.x + i * (sizeUnity.x + gapBetweenElements.x),
                    yIni - n * sizeUnity.y,
                    sizeUnity.x,
                    sizeUnity.y * n,
                    LIGHTGRAY
            );
        }

        drawFPS( 20, 20 );
    
    }


    public static void main( String[] args ) {
        new Simulator();
    }
    
}
