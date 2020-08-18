package com.vectorfrontlines.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;



public class LogoSplash {

    SpriteBatch batch;
    OrthographicCamera camera;
    Viewport viewport;

    float elapsed;
    private TextureRegion frameToRender;

    Animation<TextureRegion> animation;
    int logoWidth=0;
    int logoHeight=0;

    public LogoSplash() {
        LogoSpreadSheetLoader( "splashlogo.png" );

        //Allocate a batch Object and Set An ortogonal camera
        batch = new SpriteBatch();
        camera = new OrthographicCamera();

        //set a screen's fitting viewport
        viewport = new FitViewport( logoWidth , logoHeight , camera );
        viewport.apply();

        //Center the camera position relatively to viewport position
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);

    }

    public void render() {
        //take count of elapsed time
        elapsed += Gdx.graphics.getDeltaTime();

        //Recalculates the projection and view matrix of this camera
        camera.update();

        //"Clean" the "canvas" where draw the frame
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //coordinate the drawing coordinates within the camera
        batch.setProjectionMatrix(camera.combined);

        //draw the frame
        batch.begin();
        frameToRender = animation.getKeyFrame(elapsed);
        batch.draw( frameToRender, 0, 0 );
        batch.end();
    }

    public void update(int width, int height) {
        //make the content of the window react to the window's size changes
        viewport.update(width,height);
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
    }

    public void dispose() {
        //Release the resources used by the batch
        batch.dispose();
    }

    private boolean  LogoSpreadSheetLoader( String logoFileName ) {
        Texture logoSpreadSheet;
        //logo intake I/O operation
        try
        { logoSpreadSheet = new Texture( logoFileName ); }
        catch
        ( Exception e )
        { return false; }

        //hardcoded values fitting only within actual logo
        //logo don't change every day,so generalisation not needed
        logoWidth= logoSpreadSheet.getWidth()/8; // 8 are frames per row
        logoHeight=logoSpreadSheet.getHeight()/6; // 6 are frames per column


        //conversion from texture to TextureRegion
        TextureRegion logoTxReg=new TextureRegion( logoSpreadSheet );

        //split the whole image into a bidimensional array where each cell is a frame
        TextureRegion[][] tmp=logoTxReg.split(logoWidth,logoHeight);

        //move the frames from bidimensional to a linear array
        int frames=44;
        TextureRegion[] logoFrames = new TextureRegion[frames] ;
        int index = 0;
        for (int i = 0; i < tmp.length; i++) {
            for ( int j = 0; j < tmp[i].length; j++) {
                try
                { logoFrames[index++] = tmp[i][j]; }
                catch
                ( ArrayIndexOutOfBoundsException e )
                { break; }
            }
        }

        //Setup animation private variable with 1 frame every 30 fps
        //and make the animation looping
        animation=new Animation(1.0f/30, logoFrames );
        animation.setPlayMode( Animation.PlayMode.LOOP );
        return true;
    }
}



