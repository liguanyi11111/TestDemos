package com.testdemo.plan;

import android.graphics.Rect;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by liguanyi on 16-3-16.
 */
public class EnvMapManager {

    private static final int MAX = 1000;

    private List<Rect> mObstacleList;
    private LinkedList<int[]> mHistoryRecord;
    private int[] mLocation;
    private byte[][] mMapData;
    private int mWidth, mHeight;

    public EnvMapManager(int width, int height){
        mWidth = width;
        mHeight = height;
        mMapData = null;
        if(width < 0 || width > MAX ||
                height < 0 || height > MAX){
            throw new RuntimeException("width or height error");
        }
        mMapData = new byte[width][height];
        mObstacleList = new LinkedList<>();
        mHistoryRecord = new LinkedList<>();
        mLocation = new int[2];
    }

    public void addObstacle(Rect rect){
        if(rect.top < 0 || rect.bottom > mHeight ||
                rect.left < 0 || rect.right > mWidth){
            return;
        }
        for(int x = rect.left ; x <= rect.right ; x++){
            for(int y = rect.top ; y <= rect.bottom ; y++){
                mMapData[x][y] = Constant.TYPE_OBSTACLE;
            }
        }
        mObstacleList.add(rect);
    }

    public boolean moveTo(int x, int y){
        if(x < 0 || x >= mMapData.length || y < 0 || y >= mMapData[0].length){
            return false;
        }
        byte type = mMapData[x][y];
        if(type == Constant.TYPE_OBSTACLE){
            return false;
        }
        mLocation[0] = x;
        mLocation[1] = y;
        if(mMapData[x][y] >= Constant.TYPE_CLEARED) {
            mMapData[x][y] ++;
        }else{
            mMapData[x][y] = Constant.TYPE_CLEARED;
        }
        mHistoryRecord.add(mLocation.clone());
        Log.d("lgy", "success move to " + x + " " + y);
        return true;
    }

    public boolean moveOffset(byte[] move){
        return moveTo(mLocation[0] + move[0], mLocation[1] + move[1]);
    }

    public byte[][] getMapData(){
        return mMapData;
    }

    public List<int[]> getHistoryRecord(){
        return mHistoryRecord;
    }

    public List<Rect> getObstacleList(){
        return mObstacleList;
    }

    public byte getType(int offsetX, int offsetY){
        int x = mLocation[0] + offsetX;
        int y = mLocation[1] + offsetY;
        byte type;
        if(x < 0 || x >= mMapData.length || y < 0 || y >= mMapData[0].length){
            type = Constant.TYPE_OBSTACLE;
        }else {
            type = mMapData[x][y];
        }
        return type;
    }



}
