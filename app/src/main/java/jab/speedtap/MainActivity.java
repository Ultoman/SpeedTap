package jab.speedtap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private RadioGroup speedRG, accRG;
    private RadioButton speedRB1, speedRB2, speedRB3, accRB1, accRB2, accRB3;
    private TextView speedHSTV, accHSTV, endlessHSTV;

    public static final int SPD_ESY = 20;
    public static final int SPD_MED = 50;
    public static final int SPD_HRD = 100;

    public static final int ACC_ESY = 200;
    public static final int ACC_MED = 350;
    public static final int ACC_HRD = 500;

    private static final int NUM_COL = 4;
    private static final int NUM_ROW = 3;

    // SPEED
    private int speedNumRect = SPD_ESY;
    private String speedEasyHighScore, speedMedHighScore,speedHardHighScore;
    public static final int NUM_COL_SPD = 4;
    public static final int NUM_ROW_SPD = 3;
    // ACCURACY
    private int accNumRect = ACC_ESY;
    private String accEasyHighScore, accMedHighScore, accHardHighScore;
    public static final int NUM_COL_ACC = 4;
    public static final int NUM_ROW_ACC = 3;
    // Endless
    private String endlessHighScore;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speedHSTV = findViewById(R.id.speedHighScoreTextView);
        accHSTV = findViewById(R.id.accHighScoreTextView);
        endlessHSTV = findViewById(R.id.endlessHighScoreTextView);

        speedRG = findViewById(R.id.speedRadioGroup);
        speedRB1 = findViewById(R.id.speedRadioButton1);
        speedRB2 = findViewById(R.id.speedRadioButton2);
        speedRB3 = findViewById(R.id.speedRadioButton3);

        accRG = findViewById(R.id.accRadioGroup);
        accRB1 = findViewById(R.id.accRadioButton1);
        accRB2 = findViewById(R.id.accRadioButton2);
        accRB3 = findViewById(R.id.accRadioButton3);

        speedRB1.setText(String.valueOf(SPD_ESY));
        speedRB2.setText(String.valueOf(SPD_MED));
        speedRB3.setText(String.valueOf(SPD_HRD));
        speedRB1.setChecked(true);

        accRB1.setText(String.valueOf(ACC_ESY));
        accRB2.setText(String.valueOf(ACC_MED));
        accRB3.setText(String.valueOf(ACC_HRD));
        accRB1.setChecked(true);

        speedRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                sharedPreferences = getSharedPreferences("HighScores", Context.MODE_PRIVATE);
                switch (i) {
                    case R.id.speedRadioButton1:
                        speedNumRect = SPD_ESY;
                        speedEasyHighScore = sharedPreferences.getString("speed_easy_high_score", "0");
                        speedHSTV.setText(decrypt(String.valueOf(speedEasyHighScore)));
                        break;
                    case R.id.speedRadioButton2:
                        speedMedHighScore = sharedPreferences.getString("speed_med_high_score", "0");
                        speedHSTV.setText(decrypt(String.valueOf(speedMedHighScore)));
                        speedNumRect = SPD_MED;
                        break;
                    case R.id.speedRadioButton3:
                        speedHardHighScore = sharedPreferences.getString("speed_hard_high_score", "0");
                        speedHSTV.setText(decrypt(String.valueOf(speedHardHighScore)));
                        speedNumRect = SPD_HRD;
                        break;
                }
            }
        });

        accRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                sharedPreferences = getSharedPreferences("HighScores", Context.MODE_PRIVATE);
                switch (i)
                {
                    case R.id.accRadioButton1:
                        accEasyHighScore = sharedPreferences.getString("acc_easy_high_score", "0");
                        accHSTV.setText(decrypt(String.valueOf(accEasyHighScore)));
                        accNumRect = ACC_ESY;
                        break;
                    case R.id.accRadioButton2:
                        accMedHighScore = sharedPreferences.getString("acc_med_high_score", "0");
                        accHSTV.setText(decrypt(String.valueOf(accMedHighScore)));
                        accNumRect = ACC_MED;
                        break;
                    case R.id.accRadioButton3:
                        accHardHighScore = sharedPreferences.getString("acc_hard_high_score", "0");
                        accHSTV.setText(decrypt(String.valueOf(accHardHighScore)));
                        accNumRect = ACC_HRD;
                        break;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        sharedPreferences = getSharedPreferences("HighScores", Context.MODE_PRIVATE);

        switch(speedRG.getCheckedRadioButtonId())
        {
            case R.id.speedRadioButton1:
                speedEasyHighScore = sharedPreferences.getString("speed_easy_high_score", "0");
                speedHSTV.setText(decrypt(String.valueOf(speedEasyHighScore)));
                break;
            case R.id.speedRadioButton2:
                speedMedHighScore = sharedPreferences.getString("speed_med_high_score", "0");
                speedHSTV.setText(decrypt(String.valueOf(speedMedHighScore)));
                break;
            case R.id.speedRadioButton3:
                speedHardHighScore = sharedPreferences.getString("speed_hard_high_score", "0");
                speedHSTV.setText(decrypt(String.valueOf(speedHardHighScore)));
                break;
        }

        switch(accRG.getCheckedRadioButtonId())
        {
            case R.id.accRadioButton1:
                accEasyHighScore = sharedPreferences.getString("acc_easy_high_score", "0");
                accHSTV.setText(decrypt(String.valueOf(accEasyHighScore)));
                break;
            case R.id.accRadioButton2:
                accMedHighScore = sharedPreferences.getString("acc_med_high_score", "0");
                accHSTV.setText(decrypt(String.valueOf(accMedHighScore)));
                break;
            case R.id.accRadioButton3:
                accHardHighScore = sharedPreferences.getString("acc_hard_high_score", "0");
                accHSTV.setText(decrypt(String.valueOf(accHardHighScore)));
                break;
        }

        endlessHighScore = sharedPreferences.getString("endless_high_score", "0");
        endlessHSTV.setText(decrypt(String.valueOf(endlessHighScore)));
    }

    public void playSpeed(View v)
    {
        if (speedRG.getCheckedRadioButtonId() == -1)
        {
            return;
        }
        else {
            Intent gameIntent = new Intent(MainActivity.this, GameActivity.class);
            gameIntent.putExtra("NUM_COL", NUM_COL);
            gameIntent.putExtra("NUM_ROW", NUM_ROW);
            gameIntent.putExtra("NUM_RECT", speedNumRect);
            startActivity(gameIntent);
        }
    }

    public void playAcc(View v)
    {
        if (accRG.getCheckedRadioButtonId() == -1) {
            return;
        }
        else {
            Intent game2Intent = new Intent(MainActivity.this, Game2Activity.class);
            game2Intent.putExtra("NUM_COL", NUM_COL);
            game2Intent.putExtra("NUM_ROW", NUM_ROW);
            game2Intent.putExtra("NUM_RECT", accNumRect);
            startActivity(game2Intent);
        }
    }

    public void playEndless(View v)
    {
        Intent game3Intent = new Intent(MainActivity.this, Game3Activity.class);
        game3Intent.putExtra("NUM_COL", NUM_COL);
        game3Intent.putExtra("NUM_ROW", NUM_ROW);
        game3Intent.putExtra("NUM_RECT", -1);
        startActivity(game3Intent);
    }

    public void about(View v)
    {
        Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(aboutIntent);
    }

    //Encryption

    public static String decrypt(String input)
    {
        if (input.equals("0"))
            return "0000";
        return new String(Base64.decode(input, Base64.DEFAULT));
    }

}
