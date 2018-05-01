package jab.speedtap;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.zip.Inflater;

public class GameActivity extends AppCompatActivity {

    // Layout variables
    private RelativeLayout relativeLayout;
    private TextView timerText;

    private TextView dialogTitleText, dialogMes1, dialogMes1Result, dialogMes2, dialogMes2Result, dialogMes3, dialogMes3Result;
    private Button dialogRetryButton, dialogMainMenuButton;

    // Game variables
    GameScreen gameScreen;
    private int screenHeight, screenWidth, navBarHeight, navBarId, statusBarHeight, statusBarId;

    // Number of columns and rows for game modes
    private int numCol, numRow;

    private int total_rect;

    // Dialog variables
    private Dialog dialog;
    // Timer variables
    // For display
    private int minutes, seconds, millis;
    private float secMillis;
    // For calculations
    private long startTime;
    private long elapsedTimeMillis = 0;
    private int elapsedTimeSec;
    // End game variables
    private float tps;
    private float acc;
    private Handler handler;
    // Timer runnable
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {

            timerText.setText(getTime());
            handler.postDelayed(this, 0);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Prepare dialog
        dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.end_game_dialog);
        handler = new Handler();

        dialogTitleText = dialog.findViewById(R.id.endGameTitleTextView);
        dialogMes1 = dialog.findViewById(R.id.message1TextView);
        dialogMes1Result = dialog.findViewById(R.id.message1ResultTextView);
        dialogMes2 = dialog.findViewById(R.id.message2TextView);
        dialogMes2Result = dialog.findViewById(R.id.message2ResultTextView);
        dialogMes3 = dialog.findViewById(R.id.message3TextView);
        dialogMes3Result = dialog.findViewById(R.id.message3ResultTextView);
        dialogRetryButton = dialog.findViewById(R.id.retryButton);
        dialogMainMenuButton = dialog.findViewById(R.id.mainMenuButton);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                finish();
            }
        });

        dialogRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
                dialog.dismiss();
            }
        });

        dialogMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });

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

        // Get extras from intent
        total_rect = getIntent().getIntExtra("NUM_RECT", 20);
        numCol = getIntent().getIntExtra("NUM_COL", 4);
        numRow = getIntent().getIntExtra("NUM_ROW", 3);

        // Create GameScreen
        gameScreen = new GameScreen(GameActivity.this, this, screenHeight, screenWidth, numCol, numRow, total_rect);
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

        Log.d("timer", "Game/Timer was started!");
        handler.postDelayed(timerRunnable, 0);
    }

    public String getTime()
    {
        elapsedTimeMillis = (SystemClock.uptimeMillis() - startTime);
        elapsedTimeSec = (int)elapsedTimeMillis/1000;
        minutes = (elapsedTimeSec/60);
        seconds = (elapsedTimeSec%60);
        millis = (int)(elapsedTimeMillis%1000);
        secMillis = (float)seconds + (float)millis/1000;
        Log.d("timer", "Seconds: " + seconds + " Millis: " + millis + " SecMillis: " + secMillis);
        if (minutes == 0) {
            return String.format("%.3f", secMillis);
        }
        else {
            return minutes + ":" + String.format("%.3f", secMillis);
        }
    }

    public String estimatedTime(int tapsLeft)
    {
        float timeLeft = tapsLeft/tps;
        Log.d("estimate", "timeLeft: " + timeLeft);
        float estimatedElapsedSec = timeLeft + secMillis;
        Log.d("estimate", "estimatedTime: " + estimatedElapsedSec);
        int estimatedSeconds = (int)estimatedElapsedSec % 60;
        int estimatedMinutes = (int)estimatedElapsedSec / 60;
        int estimatedMillis = (int)((estimatedElapsedSec - estimatedSeconds) * 1000);
        float estimatedSecMillis = (float)estimatedSeconds + (float)estimatedMillis/1000;
        if (estimatedMinutes == 0)
            return String.format("%.3f", estimatedSecMillis);
        else
            return estimatedMinutes + ":" + String.format("%.3f", estimatedSecMillis);

    }

    public void stopTimer()
    {
        handler.removeCallbacks(timerRunnable);
    }

    public void gameOver(int tapsLeft, int totalTaps)
    {
        // Game ended after a winning
        stopTimer();
        timerText.setVisibility(View.INVISIBLE);

        // Divide rectangles tapped by time to get tps - taps per second
        tps = (totalTaps/secMillis);
        Log.d("gameover", "Total taps: " + totalTaps);
        acc = (total_rect/(float)totalTaps) * 100;
        if (tapsLeft == 0)
        {
            //Format of winning
            // FINISHED
            // Time:  ----
            // Taps/Sec:  ----
            //
            dialogTitleText.setText("FINISHED");
            dialogMes1.setText("Time:");
            dialogMes1Result.setText(timerText.getText());
            dialogMes2.setText("Taps/Sec:");
            dialogMes2Result.setText(String.valueOf(tps));
            dialogMes3.setVisibility(View.INVISIBLE);
            dialogMes3Result.setVisibility(View.INVISIBLE);
        }
        // Game ended after losing
        else
        {
            //Format for losing
            // GAME OVER
            // Time:  ----
            // Taps Left:  ----
            // Estimated Time: ----
            dialogTitleText.setText("GAME OVER");
            dialogMes1.setText("Time:");
            dialogMes1Result.setText(timerText.getText());
            dialogMes2.setText("Taps Left:");
            dialogMes2Result.setText(String.valueOf(tapsLeft));
            dialogMes3.setText("Estimated Time:");
            dialogMes3Result.setText(estimatedTime(tapsLeft));
        }
        dialog.show();
        dialog.getWindow().setLayout(screenWidth/2,screenHeight);
    }

    // Important to not have multiple background threads running
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(timerRunnable);
    }
}
