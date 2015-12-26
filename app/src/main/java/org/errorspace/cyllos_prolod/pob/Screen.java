package org.errorspace.cyllos_prolod.pob;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Screen {

    private int screenHeight;
    private int screenWidth;
    private Bitmap bitmap;


    public Screen(Bitmap bitmap, int screenWidth, int screenHeight){
        this.bitmap = bitmap.createScaledBitmap(bitmap,screenWidth,screenHeight,true);
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    public void drawScreen(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, null);
    }
}
