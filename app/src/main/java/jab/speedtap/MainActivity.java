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

    private static final int SPEED_LOW = 20;
    private static final int SPEED_MED = 50;
    private static final int SPEED_HIGH = 200;

    private static final int ACC_LOW = 20;
    private static final int ACC_MED = 50;
    private static final int ACC_HIGH = 200;

    private int speedNumRect = SPEED_LOW;
    private int accNumRect = ACC_LOW;

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

        speedRB1.setText(String.valueOf(SPEED_LOW));
        speedRB2.setText(String.valueOf(SPEED_MED));
        speedRB3.setText(String.valueOf(SPEED_HIGH));
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
                        speedNumRect = SPEED_LOW;
                        break;
                    case R.id.speedRadioButton2:
                        speedNumRect = SPEED_MED;
                        break;
                    case R.id.speedRadioButton3:
                        speedNumRect = SPEED_HIGH;
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
    }


    public void playSpeed(View v)
    {
        if (speedRG.getCheckedRadioButtonId() == -1)
        {
            return;
        }
        else {
            Intent speedIntent = new Intent(MainActivity.this, GameActivity.class);
            speedIntent.putExtra("NUM_RECT", speedNumRect);
            startActivity(speedIntent);
        }
    }

    public void playAcc(View v)
    {
        if (accRG.getCheckedRadioButtonId() == -1) {
            return;
        }
        else {
            Intent accIntent = new Intent(MainActivity.this, GameActivity.class);
            accIntent.putExtra("NUM_RECT", accNumRect);
            startActivity(accIntent);
        }
    }

    public void playEndless(View v)
    {
        Intent endlessIntent = new Intent(MainActivity.this, GameActivity.class);
        startActivity(endlessIntent);
    }

}
