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

    //private int left, top, right, bottom;
    //private Color color;

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

    public void set(int left, int top, int right, int bottom)
    {
        rect.set(left, top, right, bottom);
    }

}
