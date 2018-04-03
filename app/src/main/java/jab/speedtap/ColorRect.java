package jab.speedtap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by JAB on 4/2/2018.
 */

public class ColorRect {

    private Rect rect;
    private Paint paint;

    public ColorRect()
    {
        rect = new Rect();
    }

    public ColorRect(int nleft, int ntop, int nright, int nbottom, int ncolor){
        rect = new Rect();
        rect.set(nleft, ntop, nright, nbottom);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ncolor);
        paint.setStyle(Paint.Style.FILL);

    }

    public void draw(Canvas canvas)
    {
        canvas.drawRect(rect, paint);
    }

    public void setRectColor(int color)
    {
        paint.setColor(color);
    }

    // Getter and set methods for coordinates
    public void set(int left, int top, int right, int bottom)
    {
        rect.set(left, top, right, bottom);
    }

    public int getLeft()
    {
        return rect.left;
    }

    public int getTop()
    {
        return rect.top;
    }

    public int getRight()
    {
        return rect.right;
    }

    public int getBottom()
    {
        return rect.bottom;
    }

}
