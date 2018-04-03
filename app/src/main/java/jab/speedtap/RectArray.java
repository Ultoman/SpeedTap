package jab.speedtap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by JAB on 4/2/2018.
 */

public class RectArray {

    private ColorRect[] rects;

    private int rectHeight, rectWidth;
    private Random random = new Random();
    private int drawCol;

    public RectArray(int nRectHeight, int nRectWidth, int color, int row)
    {
        rectHeight = nRectHeight;
        rectWidth = nRectWidth;

        rects = new ColorRect[GameScreen.NUM_COL];

        for (int col = 0; col < GameScreen.NUM_COL; col++)
        {
            rects[col] = new ColorRect(rectWidth*col, rectHeight*row, rectWidth*(col+1), rectHeight*(row+1), color);
        }
        //Activate one random rectangle index to be drawn
        drawCol = random.nextInt(GameScreen.NUM_COL);
    }

    // Draws rectangles in random places
    public void drawRandom(Canvas canvas)
    {
        // Draw a rectangle in that row
        rects[drawCol].draw(canvas);
    }

    //Draws rectangles without changes
    public void draw(Canvas canvas)
    {
        rects[drawCol].draw(canvas);
    }

    public void moveDown()
    {
        for (int col = 0; col < GameScreen.NUM_COL; col++)
        {
            rects[col].set(rects[col].getLeft(), rects[col].getTop() + rectHeight, rects[col].getRight(), rects[col].getBottom() + rectHeight);
        }
    }

    public boolean myRectPressed(int columnPressed)
    {
        if (columnPressed == drawCol) {
            return true;
        }
        else return false;
    }

    public void drawIncorrectPress(Canvas canvas, int columnPressed)
    {
        rects[columnPressed].setRectColor(Color.RED);
        rects[columnPressed].draw(canvas);
    }

}
