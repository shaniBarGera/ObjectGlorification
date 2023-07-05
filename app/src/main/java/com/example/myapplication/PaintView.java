package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PaintView extends View{
    public LayoutParams params;
    private Path path = new Path();
    private Paint brush = new Paint();
    Paint paint = new Paint();
    private float minx=10000, maxx, miny=10000, maxy;
    private float minx2=10000, maxx2, miny2=10000, maxy2;
    boolean firstObjectMarked = false;
    boolean secondObjectMarked = false;
    Bitmap bmt;
    float relation;
    int x,y;
    boolean clean_flag = false;
    RectF firstObjectBounds;
    RectF secondObjectBounds;



    public PaintView(Context context){
        super(context);
        brush.setAntiAlias(true);
        brush.setColor(Color.parseColor("#808080"));
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeWidth(8f);
        firstObjectMarked = false;
        secondObjectMarked = false;
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    public int getMinx() {
        return (int)minx;
    }

    public int getMaxx() {
        return (int)maxx;
    }

    public int getMiny() {
        return (int)miny;
    }

    public int getMaxy() {
        return (int)maxy;
    }

    public int getMinx2() {
        return (int)minx2;
    }

    public int getMaxx2() {
        return (int)maxx2;
    }

    public int getMiny2() {
        return (int)miny2;
    }

    public int getMaxy2() {
        return (int)maxy2;
    }

    public RectF getObjectBounds1() {
        return firstObjectBounds;
    }

    public RectF getObjectBounds2() {
        return secondObjectBounds;
    }

    public void setBitmap(Bitmap bmp){
        bmt = bmp;
        invalidate();
        clean_flag = true;
        path = new Path();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        float pointX = event.getX();
        float pointY = event.getY();

        if (!firstObjectMarked) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(pointX, pointY);
                    return true;
                case MotionEvent.ACTION_UP:
                    maxx = (int) ((maxx - x) * relation);
                    maxy = (int) ((maxy - y) * relation);
                    minx = (int) ((minx - x) * relation);
                    miny = (int) ((miny - y) * relation);

                    firstObjectBounds=new RectF(minx,miny,maxx,maxy);
                    Log.d("get bounds", "*" + maxx);
                    firstObjectMarked= true;
                    return true;
                case MotionEvent.ACTION_MOVE:
                    if (minx > pointX)
                        minx = pointX;
                    if (miny > pointY)
                        miny = pointY;
                    if (maxx < pointX)
                        maxx = pointX;
                    if (maxy < pointY)
                        maxy = pointY;

                    path.lineTo(pointX, pointY);
                    break;

                default:
                    return false;
            }
        }else{
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(pointX, pointY);
                    return true;
                case MotionEvent.ACTION_UP:
                    maxx2 = (int) ((maxx2 - x) * relation);
                    maxy2 = (int) ((maxy2 - y) * relation);
                    minx2 = (int) ((minx2 - x) * relation);
                    miny2 = (int) ((miny2 - y) * relation);

                    secondObjectBounds=new RectF(minx2,miny2,maxx2,maxy2);
                    Log.d("get bounds", "*" + maxx);
                    secondObjectMarked= true;
                    return true;
                case MotionEvent.ACTION_MOVE:
                    if (minx2 > pointX)
                        minx2 = pointX;
                    if (miny2 > pointY)
                        miny2 = pointY;
                    if (maxx2 < pointX)
                        maxx2 = pointX;
                    if (maxy2 < pointY)
                        maxy2 = pointY;

                    path.lineTo(pointX, pointY);
                    break;

                default:
                    return false;
            }
        }
        postInvalidate();
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas){

        Rect screen_bounds = canvas.getClipBounds();
        if(clean_flag){
            clean_flag = false;
            canvas.drawColor(Color.WHITE);
        }

        float x_relation = (float)bmt.getWidth()/(float)screen_bounds.right;
        float y_relation = (float)bmt.getHeight()/(float)screen_bounds.bottom;
        relation = x_relation < y_relation ? y_relation: x_relation;

        x=(int)((float)screen_bounds.right - (bmt.getWidth()/relation))/2;
        y=(int)(((float)screen_bounds.bottom - (bmt.getHeight()/relation))/2);

        Rect btm_bounds = new Rect(0,0,bmt.getWidth(),bmt.getHeight());
        Rect dest_bounds = new Rect(x,y, x+(int)(bmt.getWidth()/relation),y+(int)(bmt.getHeight()/relation));

        canvas.drawBitmap(bmt,btm_bounds,dest_bounds,paint);
        canvas.drawPath(path, brush);
        paint.setStrokeWidth(15);
        paint.setStyle(Paint.Style.STROKE);
    }
}
