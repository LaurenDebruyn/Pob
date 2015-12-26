package org.errorspace.cyllos_prolod.pob.model;

import java.lang.Math;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.content.Context;
import java.lang.Math;
import org.errorspace.cyllos_prolod.pob.MainGamePanel;
import org.errorspace.cyllos_prolod.pob.MainThread;
import org.errorspace.cyllos_prolod.pob.R;
import org.errorspace.cyllos_prolod.pob.model.components.Speed;
import android.graphics.Matrix;
import java.lang.System;



import android.graphics.Paint;
import android.view.WindowManager;


public class Wall {

    public void garbageCollection(){
        System.gc();
    }

    private static final String TAG = MainGamePanel.class.getSimpleName();

    private Bitmap bitmap;    // the actual bitmap
    private Bitmap rotatedBitmap;
    private int x;            // the X coordinate
    private int y;            // the Y coordinate
    private boolean touched;    // if wall is touched/picked up
    private Speed speed;    // the speed with its directions
    private boolean canMove = true;
    private boolean canTurn = false;


    public static int HEIGHT = 20;
    public static int WIDTH = 100;

    // variables needed to compute angle rotation of wall
    private int _refX;
    private int _refY;

    private int centerX;
    private int centerY;

    private int newX;
    private int newY;

    // returns biggest of two numbers
    public int biggest(int x1, int x2){
        //check if two numbers or equal
        //return 0 if they are equal
        if (x1 == x2){
            return 0;
        }
        if (x1 < x2){
            return x2;
        }
        else{
            return x1;
        }
    }
    // returns smallest of two numbers
    public int smallest(int x1, int x2){
        //check if two numbers or equal
        //return 0 if they are equal
        if (x1 == x2){
            return 0;
        }
        if (x1 < x2){
            return x1;
        }
        else{
            return x2;
        }
    }
    // returns distance between coordinates
    // using pythagoras
    public int calculateDistance(int refX, int refY, int newX, int newY){
        //calculate height of triangle
        int height = biggest(refY,newY) - smallest(refY,newY);
        //calculate width of triangle
        int width = biggest(refX,newX) - smallest(refX,newX);
        //calculate hypotenuse of triangle
        int distance = (int) java.lang.Math.sqrt(height*height + width*width);
        return distance;
    }



    public Wall(Bitmap bitmap, int x, int y) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.speed = new Speed();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setCenterX(int x) {
        this.centerX = x + WIDTH/2;
    }

    public void setCenterY(int y) {
        this.centerY = y - HEIGHT/2;
    }

    public boolean isTouched() {
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
    }


    public boolean onTouchEvent(MotionEvent event) {
        System.gc();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (canMove) {
                // delegating event handling to the wall
                this.setX((int) event.getX());
                this.setY((int) event.getY());
                canMove = false;
                canTurn = true;

                _refX = (int) event.getX();
                _refY = (int) event.getY();
                Log.d(TAG, "refx: " + _refX + " refy: " + _refY);
                Log.d(TAG, "x: " + x + " y: " + y);

                // define the center of the wall
                setCenterX(x);
                setCenterY(y);
            }
            else {
                if (canTurn) {
                    int newX = (int) event.getX();
                    int newY = (int) event.getY();
                    Log.d(TAG, "1. refx: " + _refX + " refy: " + _refY);
                    Log.d(TAG, "x: " + x + " y: " + y);
                    Log.d(TAG, "newx: " + newX + " newy: " + newY);

                    //Log.d( TAG, "x: " + x + " y: " + y);
                    float angle = (float) computeAngle(_refX, _refY, newX, newY);
                    Log.d(TAG, "angle: " + angle);

                    bitmap = rotateBitmap(bitmap, angle, centerX, centerY, _refX, _refY, newX, newY);

                    _refX = newX;
                    _refY = newY;
                    Log.d(TAG, "2. refx: " + _refX + " refy: " + _refY);
                    //canTurn = false;
                }
            }
        }
        return true;
    }

    public double computeAngle(int refX, int refY, int newX, int newY){
        // calculates distance between the center and the left top corner of wall
        // the wall is a rectangle
        //using pythagoras
        int radiusRect = (int) Math.sqrt(50 * 50 + 20 * 20);

        //calculates the distance between the two touches
        int distanceRefNew = calculateDistance(refX, refY, newX, newY);

        //calculates the distance between second touch and the center of the wall
        int distanceCenterNew= calculateDistance(centerX, centerY, newX, newY);

        // calculate angle using rule of cosines
        // using two help variables: dividend and divisor
        double dividend = radiusRect*radiusRect + distanceCenterNew*distanceCenterNew - distanceRefNew*distanceRefNew;
        double divisor = 2*radiusRect*distanceCenterNew;
        double angle = Math.acos(dividend/divisor);
        angle = (angle*180) / Math.PI;

        return angle;
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle, int centX, int centY, int refX, int refY, int newX, int newY){
        Matrix matrix = new Matrix();
        matrix.postTranslate(-refX,-refY);
        matrix.postRotate(angle);
        matrix.postTranslate(newX, newY);
        Log.d(TAG, "width source " + source.getWidth());
        Log.d(TAG, "height source " + source.getHeight());
        return Bitmap.createBitmap(source,0,0,source.getWidth(),source.getHeight(),matrix,true);
    }

}
