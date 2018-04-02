package jab.speedtap;

import android.graphics.Rect;

/**
 * Created by JAB on 4/2/2018.
 */

public class RectArray {

    private ColorRect[] rects;

    public RectArray(int rectHeight, int rectWidth, int numCol, int row)
    {
        rects = new ColorRect[GameScreen.NUM_COL];

        for (int i = 0; i < GameScreen.NUM_COL; i++)
        {
            rects[i].set(rectWidth*i, rectHeight*row, rectWidth*(i+1), rectHeight*(row+1));
        }
    }
}
