package org.errorspace.cyllos_prolod.pob;


import android.view.SurfaceView;
import org.errorspace.cyllos_prolod.pob.model.Ball;
import org.errorspace.cyllos_prolod.pob.model.Wall;
import org.errorspace.cyllos_prolod.pob.Screen;
//import android.app.Activity;
//import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * This is the main surface that handles the ontouch events and draws
 * the image to the screen.
 */
public class MainGamePanel extends SurfaceView implements
        SurfaceHolder.Callback {

    //Verklaar Variabele - Declare variables
    private static final String TAG = MainGamePanel.class.getSimpleName();
    private MainThread thread;
    private Ball ball;
    private Wall wall;
    public int WallDraw = 0;
    public boolean WallCanBeDrawed = false;
    public boolean screenTouched = false;
    public boolean startScreen = true;

    //Laad Achtergrond - Load background
    private Bitmap game = BitmapFactory.decodeResource(getResources(), R.drawable.muur1);
    //Bitmap resizedBackground = Bitmap.createScaledBitmap(background, 720, 1280, true);
    Screen screenGame = new Screen(game, 720, 1280);
    //Laad Startscherm - Load Startscreen
    private Bitmap start = BitmapFactory.decodeResource(getResources(), R.drawable.start);
    //Bitmap resizedStart = Bitmap.createScaledBitmap(start, 720, 1280, true);
    Screen screenStart = new Screen(start, 720, 1280);
    //Laad poort - Load gate
    private Bitmap poort = BitmapFactory.decodeResource(getResources(), R.drawable.poort1);
    Bitmap resizedPoort = Bitmap.createScaledBitmap(poort, 200, 50, true);


    public MainGamePanel(Context context) {
        super(context);
        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // create ball and load bitmap
        ball = new Ball(BitmapFactory.decodeResource(getResources(), R.drawable.bal1), 49, 50);

        //create wall and load bitmap
        wall = new Wall(BitmapFactory.decodeResource(getResources(), R.drawable.muur2),100,20);
        // create the game loop thread
        thread = new MainThread(getHolder(), this);

        // make the GamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    //Check voor Touch Events
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
           screenTouched = true; //Startscherm -> Spel - Startscreen -> Game

        }
        WallDraw = WallDraw + 1;
        if(WallDraw > 2) {
            wall.onTouchEvent(event);
            WallCanBeDrawed = true;

        }
        return true;
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // at this point the surface is created and
        // we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Surface is being destroyed");
        // tell the thread to shut down and wait for it to finish
        // this is a clean shutdown
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // try again shutting down the thread
            }
        }
        Log.d(TAG, "Thread was shut down cleanly");
    }


    public void render(Canvas canvas) {
        if(startScreen){
            screenStart.drawScreen(canvas);
            if (screenTouched){
                startScreen = false;
            }
        }
        else {
            screenGame.drawScreen(canvas);
            canvas.drawBitmap(resizedPoort, 260, 0, null);
            ball.draw(canvas);
            startScreen = false;
            if(WallCanBeDrawed){
                wall.draw(canvas);
            }
        }
        //Check of we het startscherm of het spel nodig hebben
        //Check to see if we need the startscreen or the game
        /*if(startScreen) {
            canvas.drawBitmap(resizedStart, 0, 0, null);

            if (screenTouched) {
                startScreen = false;
            }
        }
        //Als we het startscherm niet nodig hebben, teken het spel
        //If we don't need the startscreen, draw the game
        if(!startScreen){
            canvas.drawBitmap(resizedBackground, 0, 0, null);
            canvas.drawBitmap(resizedPoort, 260, 0, null);
            ball.draw(canvas);
            startScreen = false;
            if(WallCanBeDrawed){
                wall.draw(canvas);
            }
        }*/
    }



    /**
     * This is the game update method. It iterates through all the objects
     * and calls their update method if they have one or calls specific
     * engine's update method.
     */
    public void update() {
        // check collision with right wall if heading right
        if (ball.getSpeed().getxDirection() == org.errorspace.cyllos_prolod.pob.model.components.Speed.DIRECTION_RIGHT
                && ball.getX()+33 + ball.getBitmap().getWidth() / 2 >= getWidth()) {
            ball.getSpeed().toggleXDirection();
        }
        // check collision with left wall if heading left
        if (ball.getSpeed().getxDirection() == org.errorspace.cyllos_prolod.pob.model.components.Speed.DIRECTION_LEFT
                && ball.getX()-33 - ball.getBitmap().getWidth() / 2 <= 0) {
            ball.getSpeed().toggleXDirection();
        }
        // check collision with bottom wall if heading down
        if (ball.getSpeed().getyDirection() == org.errorspace.cyllos_prolod.pob.model.components.Speed.DIRECTION_DOWN
                && ball.getY()+33 + ball.getBitmap().getHeight() / 2 >= getHeight()) {
            ball.getSpeed().toggleYDirection();
        }
        // check collision with top wall if heading up
        if (ball.getSpeed().getyDirection() == org.errorspace.cyllos_prolod.pob.model.components.Speed.DIRECTION_UP
                && ball.getY()-33 - ball.getBitmap().getHeight() / 2 <= 0) {
            ball.getSpeed().toggleYDirection();
        }
        // Update the lone ball
        ball.update();
    }

}
