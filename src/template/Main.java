package template;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.core.utils.CoreUtils;
import br.com.davidbuzatto.jsge.core.utils.DrawingUtils;
import br.com.davidbuzatto.jsge.geom.Rectangle;
import br.com.davidbuzatto.jsge.image.Image;

public class Main extends EngineFrame {

    public Main() {
        
        super(
            800,                 // largura                      / width
            450,                 // algura                       / height
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

    }

    @Override
    public void update( double delta ) {

    }

    @Override
    public void draw() {
        
        clearBackground( WHITE );
        
        drawFPS( 20, 20 );
    
    }

    public static void main( String[] args ) {
        new Main();
    }
    
}
