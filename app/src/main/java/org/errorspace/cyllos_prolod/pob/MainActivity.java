package org.errorspace.cyllos_prolod.pob;


import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
//import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
// android.view.Window;
//import android.view.WindowManager;

import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private static final String TAG = MainActivity.class.getSimpleName();
    int currentApiVersion = Build.VERSION.SDK_INT;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set our MainGamePanel as the View
        setContentView(new MainGamePanel(this));

        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        Log.d(TAG, "View added");

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Destroying...");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Stopping...");
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}