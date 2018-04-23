package jab.speedtap;

import android.app.ActionBar;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    private RelativeLayout relativeLayout;
    private TextView timerText;

    GameScreen gameScreen;
    private int screenHeight, screenWidth, navBarHeight, navBarId, statusBarHeight, statusBarId;

    public static final int NUM_COL = 4;
    public static final int NUM_ROW = 3;
    private int TOTAL_RECT;

    // Timer variables
    // For display
    private int minutes, seconds, millis;
    private float secMillis;
    // For calculations
    private long startTime;
    private long elapsedTimeMillis = 0;
    private int elapsedTimeSec;
    private Handler handler;
    // Timer runnable
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            elapsedTimeMillis = (SystemClock.uptimeMillis() - startTime);
            elapsedTimeSec = (int)elapsedTimeMillis/1000;
            minutes = (elapsedTimeSec/60);
            seconds = (elapsedTimeSec%60);
            millis = (int)(elapsedTimeMillis%1000);
            secMillis = (float)seconds + (float)millis/1000;
            Log.d("timer", "Seconds: " + seconds + " Millis: " + millis + " SecMillis: " + secMillis);
            if (minutes == 0) {
                timerText.setText(String.format("%.3f", secMillis));
            }
            else {
                timerText.setText(minutes + ":" + String.format("%.3f", secMillis));
            }
            handler.postDelayed(this, 0);
        }
    };

    // End game variables
    private float rectPerSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();

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

        // Get total rects from intent
        TOTAL_RECT = getIntent().getIntExtra("NUM_RECT", 20);

        // Create GameScreen
        gameScreen = new GameScreen(GameActivity.this, this, screenHeight, screenWidth, NUM_COL, NUM_ROW, TOTAL_RECT);
        Log.d("dimen", "Height: " + screenHeight + " Width: " + screenWidth);

        // Create timer textview
        timerText = new TextView(this);
        timerText.setText("0.000");
        timerText.setTextColor(getResources().getColor(R.color.colorAccent));
        timerText.setTextSize(30);

        RelativeLayout.LayoutParams timerTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        timerTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        timerTextParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        timerText.setLayoutParams(timerTextParams);

        // Create relative layout and fill with views
        relativeLayout = new RelativeLayout(this);
        relativeLayout.addView(gameScreen);
        relativeLayout.addView(timerText);

        setContentView(relativeLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void startTimer()
    {
        startTime = SystemClock.uptimeMillis();
        Toast.makeText(this, "Game was started!", Toast.LENGTH_SHORT).show();
        handler.postDelayed(timerRunnable, 0);
    }

    public void stopTimer()
    {
        handler.removeCallbacks(timerRunnable);
    }

    public void gameOver(boolean completed)
    {
        stopTimer();
        if (completed)
        {
            // Divide rectangles tapped by time to get tps - taps per second
            rectPerSecond = (TOTAL_RECT/secMillis);
            Toast.makeText(this, "Rectangles per second: " + rectPerSecond, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "You failed!", Toast.LENGTH_SHORT).show();
        }
    }

    // Important to not have multiple background threads running
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(timerRunnable);
    }
}
