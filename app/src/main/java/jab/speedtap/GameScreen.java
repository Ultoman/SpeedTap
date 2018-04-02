package jab.speedtap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * Created by JAB on 3/31/2018.
 */

public class GameScreen extends View {
    // Constants
    private static final int GRID_WIDTH = 5;
    public static final int NUM_COL = 5;
    public static final int NUM_ROW = 5;
    // Drawing variables
    private int screenHeight, screenWidth, rectHeight, rectWidth;
    private Paint gridBrush, rectBrush;
    private Rect [][] rects;
    private Random random;
    private int randomCol;
    // onTouch variables
    private int touchX;
    private int columnPressed;

    public GameScreen(Context context, int newHeight, int newWidth) {
        super(context);
        // Initialize rectangles
        rects = new Rect[NUM_COL][NUM_ROW];
        // Initialize columnPressed to 0
        columnPressed = -1;
        // Set height and width
        screenHeight = newHeight;
        rectHeight = screenHeight/NUM_ROW;
        screenWidth = newWidth;
        rectWidth = screenWidth/NUM_COL;
        // Set paint properties for grid lines
        gridBrush = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridBrush.setColor(Color.GRAY);
        gridBrush.setStyle(Paint.Style.FILL);
        gridBrush.setStrokeWidth(GRID_WIDTH);
        // Set paint properties for rectangles;
        rectBrush = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectBrush.setColor(Color.BLACK);
        rectBrush.setStyle(Paint.Style.FILL);

        // Initialize Random
        random = new Random();

        //Fill rects with rectangles
        for (int i = 0; i < NUM_COL; i++)
        {
            for (int j = 0; j < NUM_ROW; j++)
            {
                rects[i][j] = new Rect();
                rects[i][j].set(rectWidth*i,rectHeight*j,rectWidth*(i+1), rectHeight*(j+1));
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Check for touch on screen
        switch (event.getAction())
        {
            // Focus on ACTION_DOWN since the game should be a "tap"
            case MotionEvent.ACTION_DOWN:
                touchX = (int) event.getX();
                columnPressed = touchX / rectWidth;
                Log.d("col","columnPressed: " + columnPressed);

                invalidate();

                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*
        Rect rect = new Rect();
        rect.set(rectWidth,rectHeight, rectWidth*2,rectHeight*2);
        canvas.drawRect(rect, rectBrush);
        */
        for (int row = 0; row < NUM_ROW; row++)
        {
            randomCol = random.nextInt(NUM_COL);

            canvas.drawRect(rects[randomCol][row], rectBrush);
        }


        // Draw vertical grid lines
        for (int i = 0; i <= NUM_COL; i++)
        {
            canvas.drawLine(rectWidth*i,0,rectWidth*i, screenHeight, gridBrush);
        }
        // Draw horizontal grid lines
        for (int i = 0; i <= NUM_ROW; i++)
        {
            canvas.drawLine(0,rectHeight*i, screenWidth,rectHeight*i, gridBrush);
        }

    }
}
