package com.testdemo.plan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liguanyi on 16-3-16.
 *
 */
public class MapView extends View{
    private final float DENSITY;
    private final int SIDE_WIDTH;
    private byte[][] mMapData;
    private Paint mFencesPaint;
    private Paint mObstaclePaint;
    private Paint mRobotPaint;
    private Paint mPathPaint;
    private float mPointSize;
    private List<Rect> mObstacleList;
    private int[] mRobotPoint = new int[]{-1, -1};
    private List<int[]> mMovePath;
    private Bitmap mPointBitmap;

    public MapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        DENSITY = getResources().getDisplayMetrics().density;
        SIDE_WIDTH = (int)(DENSITY * 2);
        initPaint();
    }

    public void setMapData(byte[][] mapData){
        mMapData = mapData;
    }

    public void setObstacleList(List<Rect> list){
        mObstacleList = list;
    }

    public void setMovePath(List<int[]> movePath){
        mMovePath = movePath;
    }

    public void setRobotPosition(int x, int y){
        mRobotPoint[0] = x;
        mRobotPoint[1] = y;
    }

    public void showPath(){
        if(mPointBitmap == null){
            mPointBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(mPointBitmap);
            Paint paint = new Paint(mRobotPaint);
            for(int i = 0 ; i < mMapData.length ; i++){
                for(int j = 0 ; j < mMapData[0].length ; j++){
                    byte type = mMapData[i][j];
                    if(type != Constant.TYPE_OBSTACLE){
                        int repeat = type - Constant.TYPE_CLEARED;
                        if(repeat >= 0){
                            paint.setColor(0xFFFFFF00 - 0x2000 * repeat);
                            drawRectF(canvas, changePointToRect(new int[]{i, j}), paint);
                        }
                    }
                }
            }
            drawPath(canvas);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mPointBitmap != null) {
            mPointBitmap.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if(width != 0 && mMapData != null){
            int height = MeasureSpec.getSize(heightMeasureSpec);
            int internal_width = width - SIDE_WIDTH*2;
            int internal_height = height - SIDE_WIDTH*2;

            int temp_height = (int)(internal_width * (float)mMapData[0].length/mMapData.length);
            if(temp_height > internal_height){
                internal_width = (int)(internal_height * (float)mMapData.length/mMapData[0].length);
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(internal_width + SIDE_WIDTH*2, MeasureSpec.EXACTLY);
            }else{
                internal_height = temp_height;
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(internal_height + SIDE_WIDTH*2, MeasureSpec.EXACTLY);
            }
            mPointSize = (float)internal_width/mMapData.length;
            float temp = (float)internal_height/mMapData[0].length;
            Log.d("lgy", "width: " + width + " height: " + height + " mPointSize: " + mPointSize + " " + temp);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mPointBitmap != null) {
            canvas.drawBitmap(mPointBitmap, 0, 0, mFencesPaint);
        }
        drawSide(canvas);
        drawFences(canvas);
        drawObstacle(canvas);
        drawRobot(canvas);
    }

    private void initPaint(){
        mFencesPaint = new Paint();
        mFencesPaint.setColor(Color.BLACK);
        mFencesPaint.setAntiAlias(true);
        mFencesPaint.setStrokeWidth(DENSITY / 2);
        mObstaclePaint = new Paint();
        mObstaclePaint.setColor(Color.BLACK);
        mObstaclePaint.setAntiAlias(true);
        mRobotPaint = new Paint(mObstaclePaint);
        mRobotPaint.setColor(Color.GREEN);
        mPathPaint = new Paint(mRobotPaint);
        mPathPaint.setStrokeWidth(DENSITY);
        mPathPaint.setColor(Color.RED);
        mPathPaint.setStyle(Paint.Style.STROKE);
    }

    private void drawSide(Canvas canvas){
        canvas.drawRect(0, 0, getWidth() - SIDE_WIDTH, SIDE_WIDTH, mFencesPaint);
        canvas.drawRect(getWidth() - SIDE_WIDTH, 0, getWidth(), getHeight() - SIDE_WIDTH, mFencesPaint);
        canvas.drawRect(0, SIDE_WIDTH, SIDE_WIDTH, getHeight(), mFencesPaint);
        canvas.drawRect(SIDE_WIDTH, getHeight() - SIDE_WIDTH, getWidth(), getHeight(), mFencesPaint);
    }

    private void drawFences(Canvas canvas){
        if(mMapData != null) {
            mFencesPaint.setStrokeWidth(DENSITY / 2);
            for (int i = 1; i < mMapData.length; i++) {
                float start = i * mPointSize + SIDE_WIDTH;
                canvas.drawLine(start, 0, start, getHeight(), mFencesPaint);
            }
            for (int i = 1; i < mMapData[0].length; i++) {
                float start = i * mPointSize + SIDE_WIDTH;
                canvas.drawLine(0, start, getWidth(), start, mFencesPaint);
            }
        }
    }

    private void drawObstacle(Canvas canvas){
        if(mObstacleList != null) {
            for (Rect rect : mObstacleList) {
                RectF rectF = changePointToRect(rect);
                drawRectF(canvas, rectF, mObstaclePaint);
            }
        }
    }

    private void drawPath(Canvas canvas){
        if(mMovePath != null) {
            Path path = new Path();
            int[] temp = mMovePath.get(0) ;
            mRobotPaint.setColor(Color.GREEN);
            canvas.drawOval(changePointToRect(temp), mRobotPaint);
            mRobotPaint.setColor(Color.RED);
            path.moveTo(getRectCenter(temp[0]), getRectCenter(temp[1]));
            for (int i = 1 ; i < mMovePath.size() ; i++){
                temp = mMovePath.get(i);
                path.lineTo(getRectCenter(temp[0]), getRectCenter(temp[1]));
                if(i == mMovePath.size() - 1){
                    canvas.drawOval(changePointToRect(temp), mRobotPaint);
                }
            }
            canvas.drawPath(path, mPathPaint);
            mRobotPaint.setColor(Color.GREEN);
        }
    }

    private void drawRobot(Canvas canvas){
        if(mRobotPoint[0] >= 0 && mRobotPoint[0] >= 0) {
            drawRectF(canvas, changePointToRect(mRobotPoint), mRobotPaint);
        }
    }

    private void drawRectF(Canvas canvas, RectF rectF, Paint paint){
        canvas.drawRect(rectF.left, rectF.top,rectF.right, rectF.bottom, paint);
    }

    private RectF changePointToRect(Rect rect){
        return new RectF(rect.left * mPointSize + SIDE_WIDTH, rect.top * mPointSize + SIDE_WIDTH,
                (rect.right + 1) * mPointSize + SIDE_WIDTH, (rect.bottom + 1) * mPointSize + SIDE_WIDTH);
    }

    private RectF changePointToRect(int[] point){
        return new RectF(point[0] * mPointSize + SIDE_WIDTH, point[1] * mPointSize + SIDE_WIDTH,
                (point[0] + 1) * mPointSize + SIDE_WIDTH, (point[1] + 1) * mPointSize + SIDE_WIDTH);
    }

    private float getRectCenter(int size){
        return SIDE_WIDTH + (size + 0.5f) * mPointSize;
    }

}
