package jab.speedtap;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Game3Activity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Layout variables
    private RelativeLayout relativeLayout;
    private TextView tapsText;

    private TextView dialogTitleText, dialogMes1, dialogMes1Result, dialogMes2, dialogMes2Result, dialogMes3, dialogMes3Result;
    private Button dialogRetryButton, dialogMainMenuButton;

    // Game variables
    Game3Screen game3Screen;
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
    private float elapsedTimeSecFloat;
    // End game variables
    private float tps = 0.0f;
    private int score = 0;
    private Handler handler;
    // Timer runnable
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {

            tapsText.setText(String.valueOf(game3Screen.getCorrectTaps()));
            getTime();
            handler.postDelayed(this, 0);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("HighScores", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

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
        total_rect = getIntent().getIntExtra("NUM_RECT", -1);
        numCol = getIntent().getIntExtra("NUM_COL", 4);
        numRow = getIntent().getIntExtra("NUM_ROW", 3);

        // Create GameScreen
        game3Screen = new Game3Screen(Game3Activity.this, this, screenHeight, screenWidth, numCol, numRow, total_rect);
        Log.d("dimen", "Height: " + screenHeight + " Width: " + screenWidth);

        // Create timer textview
        tapsText = new TextView(this);
        tapsText.setText("0");
        tapsText.setTextColor(getResources().getColor(R.color.colorAccent));
        tapsText.setTextSize(30);

        RelativeLayout.LayoutParams timerTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        timerTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        timerTextParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        tapsText.setLayoutParams(timerTextParams);

        // Create relative layout and fill with views
        relativeLayout = new RelativeLayout(this);
        relativeLayout.addView(game3Screen);
        relativeLayout.addView(tapsText);

        setContentView(relativeLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
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
        elapsedTimeSecFloat = (float)elapsedTimeMillis/1000;
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

    public void stopTimer()
    {
        handler.removeCallbacks(timerRunnable);
    }

    public void gameOver(int totalTaps)
    {
        // Game ended after a winning
        stopTimer();
        tapsText.setVisibility(View.INVISIBLE);

        // Divide rectangles tapped by time to get tps - taps per second
        if (!(elapsedTimeSecFloat == 0))
            tps = (totalTaps/elapsedTimeSecFloat);
        score = (int)(tps*totalTaps);

        String scoreOldEncrypted = sharedPreferences.getString("endless_high_score", "0");
        String scoreOldDecrypted = decrypt(scoreOldEncrypted);

        String scoreNewEncrypted = encrypt(String.valueOf(score));

        if (score > Integer.valueOf(scoreOldDecrypted))
        {
            editor.putString("endless_high_score", scoreNewEncrypted);
            editor.commit();
            dialogMes3Result.setBackgroundColor(Color.YELLOW);
        }

        Log.d("gameover", "Total taps: " + totalTaps);

        dialogTitleText.setText("GAME END");
        dialogMes1.setText("Total Taps:");
        dialogMes1Result.setText(tapsText.getText());
        dialogMes2.setText("Taps/Sec:");
        dialogMes2Result.setText(String.format("%.3f", tps));
        dialogMes3.setText("Score:");
        dialogMes3Result.setText(String.valueOf(score));

        dialog.show();
        dialog.getWindow().setLayout(screenWidth/2,screenHeight);


    }

    //Encryption
    public static String encrypt(String input)
    {
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public static String decrypt(String input)
    {
        if (input.equals("0"))
            return input;
        return new String(Base64.decode(input, Base64.DEFAULT));
    }

    // Important to not have multiple background threads running
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(timerRunnable);
    }
}
