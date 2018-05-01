package jab.speedtap;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private RadioGroup speedRG, accRG;
    private RadioButton speedRB1, speedRB2, speedRB3, accRB1, accRB2, accRB3;

    private static final int SPD_LOW = 20;
    private static final int SPD_MED = 50;
    private static final int SPD_HIGH = 200;

    private static final int ACC_LOW = 20;
    private static final int ACC_MED = 50;
    private static final int ACC_HIGH = 200;

    private static final int NUM_COL = 4;
    private static final int NUM_ROW = 3;

    // SPEED
    private int speedNumRect = SPD_LOW;
    public static final int NUM_COL_SPD = 4;
    public static final int NUM_ROW_SPD = 3;
    // ACCURACY
    private int accNumRect = ACC_LOW;
    public static final int NUM_COL_ACC = 4;
    public static final int NUM_ROW_ACC = 3;
    // REACTION
    /*
    private int reactNumRect = RCT_LOW;
    public static final int NUM_COL_RCT = 4;
    public static final int NUM_ROW_RCT = 1;
    */

    private Intent gameIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speedRG = findViewById(R.id.speedRadioGroup);
        speedRB1 = findViewById(R.id.speedRadioButton1);
        speedRB2 = findViewById(R.id.speedRadioButton2);
        speedRB3 = findViewById(R.id.speedRadioButton3);

        accRG = findViewById(R.id.accRadioGroup);
        accRB1 = findViewById(R.id.accRadioButton1);
        accRB2 = findViewById(R.id.accRadioButton2);
        accRB3 = findViewById(R.id.accRadioButton3);

        speedRB1.setText(String.valueOf(SPD_LOW));
        speedRB2.setText(String.valueOf(SPD_MED));
        speedRB3.setText(String.valueOf(SPD_HIGH));
        speedRB1.setChecked(true);

        accRB1.setText(String.valueOf(ACC_LOW));
        accRB2.setText(String.valueOf(ACC_MED));
        accRB3.setText(String.valueOf(ACC_HIGH));
        accRB1.setChecked(true);

        speedRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.speedRadioButton1:
                        speedNumRect = SPD_LOW;
                        break;
                    case R.id.speedRadioButton2:
                        speedNumRect = SPD_MED;
                        break;
                    case R.id.speedRadioButton3:
                        speedNumRect = SPD_HIGH;
                        break;
                }
            }
        });

        accRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i)
                {
                    case R.id.accRadioButton1:
                        accNumRect = ACC_LOW;
                        break;
                    case R.id.accRadioButton2:
                        accNumRect = ACC_MED;
                        break;
                    case R.id.accRadioButton3:
                        accNumRect = ACC_HIGH;
                        break;
                }
            }
        });

        gameIntent = new Intent(MainActivity.this, GameActivity.class);
        gameIntent.putExtra("NUM_COL", NUM_COL);
        gameIntent.putExtra("NUM_ROW", NUM_ROW);
    }


    public void playSpeed(View v)
    {
        if (speedRG.getCheckedRadioButtonId() == -1)
        {
            return;
        }
        else {
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
            gameIntent.putExtra("NUM_RECT", accNumRect);
            startActivity(gameIntent);
        }
    }

    public void playEndless(View v)
    {
        gameIntent.putExtra("NUM_RECT", -1);
        startActivity(gameIntent);
    }

}
