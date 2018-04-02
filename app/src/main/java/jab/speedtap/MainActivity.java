package jab.speedtap;

import android.content.res.Resources;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    GameScreen gameScreen;
    private int screenHeight, screenWidth, navBarHeight, navBarId, statusBarHeight, statusBarId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        // Fix Height and Width issues with status bar and navigation bars
        navBarHeight = 0;
        navBarId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (navBarId > 0) {
            navBarHeight = getResources().getDimensionPixelSize(navBarId);
        }
        //Log.d("dimen","navHeight: " + navBarHeight + " navID: " + navBarId);
        statusBarHeight = 0;
        statusBarId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (statusBarId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(statusBarId);
        }
        //Log.d("dimen","statusHeight: " + statusBarHeight + " statusID: " + statusBarId);

        //Incorrect screen sizes when reloading app????
        //screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels - statusBarHeight;
        //screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;// + navBarHeight;

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        screenHeight = size.y - statusBarHeight;
        screenWidth = size.x;
        // Check for orientation of screen
        if (screenHeight > screenWidth) {
            screenHeight += navBarHeight;
        } else {
            screenWidth += navBarHeight;
        }

        gameScreen = new GameScreen(this, screenHeight, screenWidth);
        Log.d("dimen", "Height: " + screenHeight + " Width: " + screenWidth);

        setContentView(gameScreen);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
