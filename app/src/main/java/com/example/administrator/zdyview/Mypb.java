package com.example.administrator.zdyview;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 何永武 on 2017/11/5.
 */

public class Mypb extends View {

    private int color;
    private float dimension;
    private float cx;
    private float cy;
    private Paint paint;
    private int sweep;
    public Mypb(Context context) {
        this(context,null);
    }

    public Mypb(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyPb);
        color = array.getColor(R.styleable.MyPb_circle_color, Color.BLACK);
        dimension = array.getDimension(R.styleable.MyPb_circle_radius, 60);
        cx = array.getDimension(R.styleable.MyPb_circle_x, 300);
        cy = array.getDimension(R.styleable.MyPb_circle_y, 100);
        array.recycle();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(sweep > 360){
                    return;
                }
                sweep = sweep+1;
                if(sweep == 360){
                    Intent intent = new Intent(context, SendActivity.class);
                    context.startActivity(intent);
                }
                postInvalidate();

            }
        },1000,20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(color);
        paint.setStrokeWidth(5);
        canvas.drawCircle(cx,cy,dimension,paint);
        paint.setColor(Color.RED);
        RectF rectF = new RectF(cx - dimension, cy - dimension, cx + dimension, cy + dimension);
        canvas.drawArc(rectF , -90 , sweep , false , paint);
        int progress = (int)(sweep / 360f *100);
        paint.setStrokeWidth(0);
        paint.setColor(Color.BLACK);
        paint.setTextSize(35);
        canvas.drawText(progress + "%" , cx - 30 , cy + 10 , paint);





    }
}
