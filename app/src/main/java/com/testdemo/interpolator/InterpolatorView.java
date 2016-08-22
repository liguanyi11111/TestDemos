package com.testdemo.interpolator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

/**
 * 差速器测试
 * Created by liguanyi on 2015/7/15.
 */
public class InterpolatorView extends View {
    final float yOffset = 0.8f;
    float x;
    float y;
    Bitmap mBitmap;
    Paint mBitmapPaint;
    Paint mDrawPaint;


    public InterpolatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InterpolatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBitmapPaint = new Paint();
        mDrawPaint = new Paint();
        mDrawPaint.setStrokeWidth(2 * context.getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void start(final Interpolator interpolator) {
        if(interpolator == null){
            return;
        }
        if(mBitmap != null){
            mBitmap.recycle();
        }
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(mBitmap);
        canvas.drawColor(0xffffffff);
        mDrawPaint.setColor(0xff00ff00);
        canvas.drawLine(0, getMeasuredHeight() * yOffset, getMeasuredWidth(), getMeasuredHeight() * yOffset, mDrawPaint);
        canvas.drawLine(0, getMeasuredHeight() * yOffset - getMeasuredWidth(),
                getMeasuredWidth(), getMeasuredHeight() * yOffset - getMeasuredWidth(), mDrawPaint);
        mDrawPaint.setColor(0xffff0000);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(10000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                x = (float)animation.getAnimatedValue();
                y = interpolator.getInterpolation(x);
                canvas.drawPoint(getMeasuredWidth() * x, getMeasuredHeight() * yOffset - getMeasuredWidth() * y, mDrawPaint);
                invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        }
    }
}
