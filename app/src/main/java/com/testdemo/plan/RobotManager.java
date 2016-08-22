package com.testdemo.plan;

import android.util.Log;

import java.util.LinkedList;

/**
 * Created by liguanyi on 16-3-16.
 * 机器人管理类
 */
public class RobotManager {

    public class CoveragePlan{
        private final byte STATE_NORMAL = 0;
        private final byte STATE_P2P = 1;
        private final byte[][] directions = new byte[][]{Constant.LEFT, Constant.UP, Constant.DOWN, Constant.RIGHT};
        private byte mState = STATE_NORMAL;
        private byte[] mCurrentDirection = Constant.LEFT;
        private int mTurnCount = 0;
        private int mAStarCount = 0;

        //左上下右简单方式
        private byte[] getMove_1(){
            for (byte[] temp : directions) {
                if (getType(mX + temp[0], mY + temp[1]) < Constant.TYPE_OBSTACLE) {
                    return temp;
                }
            }
            return null;
        }

        //旋转 內缩
        private byte[] getMove_2(){
            byte[] temp = null;
            if (getType(mX + mCurrentDirection[0], mY + mCurrentDirection[1]) < Constant.TYPE_OBSTACLE) {
                return mCurrentDirection;
            }else{
                if(isSameDirection(mCurrentDirection, Constant.LEFT)){
                    temp = Constant.DOWN;
                }else if(isSameDirection(mCurrentDirection, Constant.DOWN)){
                    temp = Constant.RIGHT;
                }else if(isSameDirection(mCurrentDirection, Constant.RIGHT)){
                    temp = Constant.UP;
                }else if(isSameDirection(mCurrentDirection, Constant.UP)){
                    temp = Constant.LEFT;
                }
                if (temp != null && getType(mX + temp[0], mY + temp[1]) < Constant.TYPE_OBSTACLE) {
                    return temp;
                }
            }
            return null;
        }

        //旋转 外放
        private byte[] getMove_3(){
            byte[] temp = null;
            if(isSameDirection(mCurrentDirection, Constant.LEFT)){
                temp = Constant.DOWN;
            }else if(isSameDirection(mCurrentDirection, Constant.DOWN)){
                temp = Constant.RIGHT;
            }else if(isSameDirection(mCurrentDirection, Constant.RIGHT)){
                temp = Constant.UP;
            }else if(isSameDirection(mCurrentDirection, Constant.UP)){
                temp = Constant.LEFT;
            }
            if (temp != null && getType(mX + temp[0], mY + temp[1]) < Constant.TYPE_OBSTACLE) {
                return temp;
            }else if(getType(mX + mCurrentDirection[0], mY + mCurrentDirection[1]) < Constant.TYPE_OBSTACLE){
                return mCurrentDirection;
            }
            return null;
        }

        private boolean isSameDirection(byte[] a, byte[] b){
            return a[0] == b[0] && a[1] == b[1];
        }

        protected void start(){
            byte[] move;
            boolean isMove = true;
            LinkedList<byte[]> aStarResult = null;
            while (true){
                move = null;
                if(isMove) {
                    //获取传感器数据
                    for (byte[] temp : directions) {
//                        byte type = mSensor.getSensorData(temp);
//                        if(type == Constant.TYPE_OBSTACLE){
//                            Log.d("lgy", "obstacle " + (mX + temp[0]) + " " + (mY + temp[1]));
//                            savePosition(temp, Constant.TYPE_OBSTACLE);
//                        }else if(type == Constant.TYPE_NONE){
//                        }
                        savePosition(temp, Constant.TYPE_NOT_KNOW);
                    }
                }
                if(mState == STATE_NORMAL){
                    //获取move
//                    for (byte[] temp : directions) {
//                        if (getType(mX + temp[0], mY + temp[1]) < Constant.TYPE_OBSTACLE) {
//                            move = temp;
//                            break;
//                        }
//                    }
                    move = getMove_1();
                    if(move == null){
                        //寻找未去过的点
                        int[] point = getNotCleanPoint();
                        if(point != null){
                            aStarResult = getAStarPath(mX, mY, point[0], point[1]);
                            Log.d("lgy", "A* " + mX + " " + mY + " | " + point[0] + " " + point[1]);
                            if(aStarResult != null) {
                                mState = STATE_P2P;
                                mAStarCount ++;
                            }
                        }
                    }
                }
                if(mState == STATE_P2P && aStarResult != null){
                    move = aStarResult.removeFirst();
                    if(aStarResult.isEmpty()){
                        mState = STATE_NORMAL;
                    }
                }

                if(move == null){
                    //stop
                    break;
                }else{
                    //尝试移动
                    if(mController.move(move)) {
                        //移动成功 记录自身位置
                        if(mCurrentDirection != move){
                            mCurrentDirection = move;
                            mTurnCount++;
                        }
                        isMove = true;
                        savePosition(move, Constant.TYPE_CLEARED);
                    }else{
                        //行动失败，碰到障碍
                        isMove = false;
                        savePosition(move, Constant.TYPE_OBSTACLE);
                        if(mState == STATE_P2P){
                            //A*搜索出现问题 重新执行
                            mState = STATE_NORMAL;
                        }
                    }
                }

            }
            Log.d("lgy", "MOVE END!!!  Turn Count: " + mTurnCount + " A* Count: " + mAStarCount);
        }

        private int[] getNotCleanPoint(){
            //简单遍历 （可以优化）
            boolean isEnd = false;
            int cost = Integer.MAX_VALUE;
            int temp = 0;
            int[] result = null;
            for(int i = 1 ; !isEnd ; i ++){
                isEnd = true;
                //上边
                for(int x = mX - i ; x <= mX + i ; x++){
                    int type = getType(x, mY + i);
                    if(type == Constant.TYPE_NOT_KNOW){
                        temp = Math.abs(x - mX) + Math.abs(i);
                        if(temp < cost) {
                            result = new int[]{x, mY + i};
                            cost = temp;
                        }
                    }else if(type == Constant.TYPE_CLEARED){
                        isEnd = false;
                    }
                }
                //下边
                for(int x = mX - i ; x <= mX + i ; x++){
                    int type = getType(x, mY - i);
                    if(type == Constant.TYPE_NOT_KNOW){
                        temp = Math.abs(x - mX) + Math.abs(i);
                        if(temp < cost) {
                            result = new int[]{x, mY - i};
                            cost = temp;
                        }
                    }else if(type == Constant.TYPE_CLEARED){
                        isEnd = false;
                    }
                }
                //左边
                for(int y = mY - i + 1 ; y <= mY + i - 1 ; y++){
                    int type = getType(mX - i, y);
                    if(type == Constant.TYPE_NOT_KNOW){
                        temp = Math.abs(y - mY) + Math.abs(i);
                        if(temp < cost) {
                            result = new int[]{mX - i, y};
                            cost = temp;
                        }
                    }else if(type == Constant.TYPE_CLEARED){
                        isEnd = false;
                    }
                }
                //右边
                for(int y = mY - i + 1 ; y <= mY + i - 1 ; y++){
                    int type = getType(mX + i, y);
                    if(type == Constant.TYPE_NOT_KNOW){
                        temp = Math.abs(y - mY) + Math.abs(i);
                        if(temp < cost) {
                            result = new int[]{mX + i, y};
                            cost = temp;
                        }
                    }else if(type == Constant.TYPE_CLEARED){
                        isEnd = false;
                    }
                }
            }
            Log.d("lgy", " getNotCleanPoint ");
            return result;
        }

        private LinkedList<byte[]> getAStarPath(int x, int y, int targetX, int targetY){
            long time = System.nanoTime();
            LinkedList<AStarNode> openList = new LinkedList<>();
            LinkedList<AStarNode> closeList = new LinkedList<>();
            AStarNode rootNode = new AStarNode(x, y);
            rootNode.estimateCost = Math.abs(targetX - x) + Math.abs(targetY - y);
            openList.add(rootNode);
            while(!openList.isEmpty()){
                AStarNode node = openList.removeFirst();
                if(node.isSame(targetX, targetY)){
                    //使用偏移量
                    LinkedList<byte[]> result = new LinkedList<>();
//                    AStarNode tempNode = node;
//                    while(tempNode != null){
//                        Log.d("lgy" ,"a*: " + tempNode.x + " " + tempNode.y);
//                        tempNode = tempNode.parent;
//                    }
                    while (node.parent != null){
                        result.addFirst(new byte[]{(byte) (node.x - node.parent.x),
                                (byte) (node.y - node.parent.y)});
                        node = node.parent;
                    }
                    Log.d("lgy", "A*搜索成功 time: " + (float)(System.nanoTime() - time)/1000000 + "ms");
                    return result;
                }
                closeList.addLast(node);
                ALL:
                for(byte[] direction : directions){
                    int tempX = node.x + direction[0];
                    int tempY = node.y + direction[1];
                    if(node.parent == null || !node.parent.isSame(tempX, tempY)){
                        if(getType(tempX, tempY) == Constant.TYPE_NONE ||
                                getType(tempX, tempY) == Constant.TYPE_OBSTACLE){
                            //如果是障碍物或者未知区域则排除
                            continue;
                        }
                        for(AStarNode n : closeList){
                            //如果已存在于close表中则排除
                            if(n.isSame(tempX, tempY)){
                                continue ALL;
                            }
                        }
                        AStarNode child = new AStarNode(tempX, tempY);
                        child.estimateCost = Math.abs(targetX - tempX) + Math.abs(targetY - tempY);
                        child.cost = node.cost + 1;
                        child.parent = node;
                        for(AStarNode n : openList){
                            //检查是否已存在于open表
                            if(n.equals(child)){
                                if(child.cost < n.cost){
                                    //用低成本代替高成本 (estimateCost必然相等)
                                    n.cost = child.cost;
                                    n.parent = node;
                                }
                                continue ALL;
                            }
                        }
                        //将子结点按顺序加入open表
                        if(openList.size() > 0) {
                            for (int i = 0; i < openList.size(); i++) {
                                AStarNode n = openList.get(i);
                                if (child.getAllCost() <= n.getAllCost()) {
                                    //低成本优先
                                    openList.add(i, child);
                                    break;
                                }
                            }
                        }else {
                            openList.add(child);
                        }
                    }
                }
            }
            Log.d("lgy", "A*搜索失败");
            return null;
        }


        private class AStarNode{
            AStarNode parent;
            int x,y;
            int cost = 0;
            int estimateCost;

            public AStarNode(int x, int y){
                this.x = x;
                this.y = y;
            }

            public int getAllCost(){
                return cost + estimateCost;
            }

            @Override
            public boolean equals(Object o) {
                if(o instanceof AStarNode){
                    AStarNode node = (AStarNode)o;
                    return x == node.x && y == node.y;
                }
                return super.equals(o);
            }

            public boolean isSame(int x, int y){
                return this.x == x && this.y == y;
            }

            @Override
            public String toString() {
                return "AStarNode: " + x + " " + y;
            }
        }

    }

//    private static final byte[][] directions = new byte[][]{Constant.DOWN, Constant.UP, Constant.RIGHT, Constant.LEFT};

    private ISensor mSensor;
    private IMoveController mController;
    private CoveragePlan mCoveragePlan;
    private IRobotListener mListener;
    private byte[][][] mRobotMapData = new byte[4][100][100];
    private int mX ;
    private int mY ;
    public RobotManager(){
        mCoveragePlan = new CoveragePlan();
        mRobotMapData[0][0][0] = Constant.TYPE_CLEARED;
    }

    public void start(){
        Log.d("lgy", "robot start");
        mListener.onCalcStart();
        new Thread(){
            @Override
            public void run() {
                mCoveragePlan.start();
                Log.d("lgy", "robot stop");
                mListener.onCalcComplete();
            }
        }.start();

    }

    public void setSensor(ISensor sensor){
        mSensor = sensor;
    }

    public void setListener(IRobotListener l){
        mListener = l;
    }

    public void setController(IMoveController controller){
        mController = controller;
    }

    private void savePosition(byte[] offset, byte type){
        int x = mX + offset[0];
        int y = mY + offset[1];
        if(type == Constant.TYPE_CLEARED){
            //更新当前位置
            mX = x;
            mY = y;
            Log.d("lgy", "move to " + x + " " + y + " type: " + type);
        }
        byte quadrant = getQuadrant(x, y);
        byte tempType = mRobotMapData[quadrant][Math.abs(x)][Math.abs(y)];
        if(tempType < type){
            mRobotMapData[quadrant][Math.abs(x)][Math.abs(y)] = type;
//            Log.d("lgy", "save " + x + " " + y + " type: " + type);
        }
    }

    private byte getType(int x, int y){
        if(Math.abs(x) >= 100 && Math.abs(y) >= 100){
            return Constant.TYPE_NONE;
        }
        byte quadrant = getQuadrant(x, y);
        return mRobotMapData[quadrant][Math.abs(x)][Math.abs(y)];
    }

    private byte getQuadrant(int x, int y){
        byte quadrant;
        if(x >= 0){
            if(y >= 0){
                //第一象限
                quadrant = 0;
            }else{
                //第二象限
                quadrant = 1;
            }
        }else{
            if(y >= 0){
                //第四象限
                quadrant = 3;
            }else{
                //第三象限
                quadrant = 2;
            }
        }
        return quadrant;
    }

    public interface IRobotListener{

        void onCalcStart();

        void onCalcComplete();

    }

    public interface ISensor{

        //return: type
        byte getSensorData(byte[] offset);
    }

    public interface IMoveController {

        boolean move(byte[] move);
    }

}
