package jab.speedtap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by JAB on 3/31/2018.
 */

public class GameScreen extends View {
    // Constants
    public static int NUM_COL = 4;
    public static int NUM_ROW = 3;

    private static final int GRID_WIDTH = 5;
    // Drawing variables
    private int screenHeight, screenWidth, rectHeight, rectWidth;
    private Paint gridBrush;
    private ArrayList<RectArray> rowArray;
    private int rectColor = Color.BLACK;
    // onTouch variables
    private int touchX;
    private int columnPressed;
    private boolean correctPress;

    public GameScreen(Context context, int newHeight, int newWidth) {
        super(context);
        // Initialize rows of rectangles
        //rects = new Rect[NUM_COL][NUM_ROW];
        rowArray = new ArrayList<>(NUM_ROW);
        // Initialize flags
        columnPressed = -1;
        correctPress = false;
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

        //Fill rowArray with rectArrays
        for (int row = 0; row < NUM_ROW; row++)
        {
            rowArray.add(row, new RectArray(rectHeight, rectWidth, rectColor, row));
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

                // Check if correct rectangle was pressed
                if (rowArray.get(NUM_ROW - 1).myRectPressed(columnPressed))
                {
                    correctPress = true;
                    Log.d("game", "Correct column pressed!");
                }
                else
                {
                    correctPress = false;
                }
                // redraw canvas with updated flags
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    // Draw method called everytime invalidate() is called
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 3 Drawing scenarios:
        // First initialization
        if (!correctPress && columnPressed == -1) {
            for (int row = 0; row < NUM_ROW; row++) {
                // Every row draws their rectangle in a random place
                rowArray.get(row).drawRandom(canvas);
            }
        }
        // If correct rectangle was pressed
        if (correctPress)
        {
            moveDown();
            for (int row = 0; row < NUM_ROW; row++) {
                // Every row draws their rectangle
                rowArray.get(row).draw(canvas);
            }
        }
        // If incorrect rectangle was pressed
        else if (!(correctPress || columnPressed == -1))
        {
            rowArray.get(NUM_ROW - 1).drawIncorrectPress(canvas, columnPressed);
            for (int row = 0; row < NUM_ROW; row++) {
                // Every row draws their rectangle
                rowArray.get(row).draw(canvas);
            }

        }

        // Draw grid last
        drawGrid(canvas);

    }

    public void drawGrid(Canvas canvas)
    {
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

    public void moveDown()
    {

        for (int row = NUM_ROW - 1; row > 0; row--)
        {
            rowArray.set(row, rowArray.get(row - 1));
            rowArray.get(row).moveDown();
        }
        rowArray.set(0, new RectArray(rectHeight, rectWidth, rectColor, 0));

    }
}
