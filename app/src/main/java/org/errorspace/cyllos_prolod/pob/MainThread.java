package org.errorspace.cyllos_prolod.pob;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.util.Log;


public class MainThread extends Thread {

    private static final String TAG = MainThread.class.getSimpleName();

    private SurfaceHolder surfaceHolder;
    private MainGamePanel gamePanel;
    private boolean running;
    public void setRunning(boolean running) {
        this.running = running;
    }

    public MainThread(SurfaceHolder surfaceHolder, MainGamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }


    @Override
    public void run() {
        Canvas canvas;
        Log.d(TAG, "starting game loop");
        while (running){
            canvas = null;
            // try locking the canvas for exclusive pixel editing in the surface
            try {
                canvas = this.surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder){
                    // update game state
                    this.gamePanel.update();
                    //render state to the screen
                    // draws the canvas on the panel
                    this.gamePanel.render(canvas);

                }
            } finally {
                // in case of an exception the surface is  not left in
                // an inconsistent state
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            } // end finally
        }
    }
}
