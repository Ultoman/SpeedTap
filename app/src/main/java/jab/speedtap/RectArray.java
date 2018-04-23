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
    private int numCol;
    private boolean isEmptyRow;

    public RectArray(int nRectHeight, int nRectWidth, int color, int row, int newNumCol, boolean empty)
    {
        rectHeight = nRectHeight;
        rectWidth = nRectWidth;
        numCol = newNumCol;
        isEmptyRow = empty;

        rects = new ColorRect[numCol];

        for (int col = 0; col < numCol; col++)
        {
            rects[col] = new ColorRect(rectWidth*col, rectHeight*row, rectWidth*(col+1), rectHeight*(row+1), color);
        }
        //Activate one random rectangle index to be drawn
        drawCol = random.nextInt(numCol);
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
        if (!isEmptyRow)
        {
            rects[drawCol].draw(canvas);
        }
        else
        {
            drawAll(canvas);
        }
    }

    public void drawAll(Canvas canvas)
    {
        for (int i = 0; i < numCol; i++)
        {
            rects[i].draw(canvas);
        }
    }

    public void moveDown()
    {
        // Moves down coordinates of every rectangle in row
        for (int col = 0; col < numCol; col++)
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
