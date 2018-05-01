package jab.speedtap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.TimerTask;


/**
 * Created by JAB on 3/31/2018.
 */

public class GameScreen extends View {
    // Game data
    private int numCol;
    private int numRow;
    private int totalRect;
    private int maxRect;
    private int totalTaps = 0;

    private GameActivity gameActivity;

    // Drawing variables
    private static final int GRID_WIDTH = 5;
    private int screenHeight, screenWidth, rectHeight, rectWidth;
    private Paint gridBrush;
    private ArrayList<RectArray> rowArray;
    private int rectColor = Color.BLACK;
    private int emptyColor = Color.DKGRAY;

    // onTouch variables
    private int touchX;
    private int columnPressed;
    private boolean correctPress, wrongPress;

    // Constructor
    public GameScreen(GameActivity gameAct, Context context, int newHeight, int newWidth, int newNumCol, int newNumRow, int newTotalRect) {
        super(context);
        gameActivity = gameAct;
        // Initialize rows of rectangles
        rowArray = new ArrayList<>(numRow);
        // Initialize flags
        columnPressed = -1;
        correctPress = false;
        // Set number of rows, columns, and total rectangles
        numCol = newNumCol;
        numRow = newNumRow;
        totalRect = newTotalRect;
        maxRect = totalRect;
        // Set height and width
        screenHeight = newHeight;
        rectHeight = screenHeight/numRow;
        screenWidth = newWidth;
        rectWidth = screenWidth/numCol;
        // Set paint properties for grid lines
        gridBrush = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridBrush.setColor(Color.GRAY);
        gridBrush.setStyle(Paint.Style.FILL);
        gridBrush.setStrokeWidth(GRID_WIDTH);

        //Fill rowArray with rectArrays
        for (int row = 0; row < numRow; row++)
        {
            rowArray.add(row, new RectArray(rectHeight, rectWidth, rectColor, row, numCol, false));
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (totalRect == -(numRow) || wrongPress)
        {
            return super.onTouchEvent(event);
        }
        // Check for touch on screen
        switch (event.getAction())
        {
            // Focus on ACTION_DOWN since the game should be a "tap"
            case MotionEvent.ACTION_DOWN:
                // Get column pressed
                Log.d("game", "Taps: " + totalTaps);
                touchX = (int) event.getX();
                columnPressed = touchX / rectWidth;
                Log.d("col","columnPressed: " + columnPressed);

                // Check if correct column was pressed
                if (rowArray.get(numRow - 1).myRectPressed(columnPressed))
                {
                    //Check if correct press was first press
                    if (totalRect == maxRect - numRow)
                    {
                        gameActivity.startTimer();
                    }
                    correctPress = true;
                    totalTaps++;
                    Log.d("game", "Correct column pressed!");
                }
                else
                {
                    correctPress = false;
                    wrongPress = true;
                }
                // redraw canvas with updated flags
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    // Draw method called every time invalidate() is called
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 3 Drawing scenarios:
        // First initialization -- nothing pressed
        if (!correctPress && columnPressed == -1) {
            for (int row = 0; row < numRow; row++) {
                // Every row draws their rectangle in a random place
                rowArray.get(row).drawRandom(canvas);
                totalRect--;
            }
        }
        // If correct rectangle was pressed
        if (correctPress)
        {
            moveDown();
            for (int row = 0; row < numRow; row++) {
                // Every row draws their rectangle
                rowArray.get(row).draw(canvas);
            }
        }
        // If incorrect rectangle was pressed
        else if (!(correctPress || columnPressed == -1))
        {
            rowArray.get(numRow - 1).drawIncorrectPress(canvas, columnPressed);
            for (int row = 0; row < numRow; row++) {
                // Every row draws their rectangle
                rowArray.get(row).draw(canvas);
            }
            wrongPress = true;
        }
        // Draw grid
        drawGrid(canvas);

        //Check if game has ended
        if (totalRect == -(numRow) || wrongPress) {
            gameActivity.gameOver(totalRect + numRow, totalTaps);
        }
    }

    public void drawGrid(Canvas canvas)
    {
        // Draw vertical grid lines
        for (int i = 0; i <= numCol; i++)
        {
            canvas.drawLine(rectWidth*i,0,rectWidth*i, screenHeight, gridBrush);
        }
        // Draw horizontal grid lines
        for (int i = 0; i <= numRow; i++)
        {
            canvas.drawLine(0,rectHeight*i, screenWidth,rectHeight*i, gridBrush);
        }
    }

    public void moveDown()
    {

        for (int row = numRow - 1; row > 0; row--)
        {
            // Move down in array
            rowArray.set(row, rowArray.get(row - 1));
            // Move down drawn rectangle
            rowArray.get(row).moveDown();
        }

        if (totalRect > 0) {
            rowArray.set(0, new RectArray(rectHeight, rectWidth, rectColor, 0, numCol, false));
        }
        else
        {
            rowArray.set(0, new RectArray(rectHeight, rectWidth, emptyColor, 0, numCol, true));
        }
        // Subtract from total
        totalRect--;
    }

}
