package com.testdemo.plan;

import android.graphics.Rect;

import java.util.List;

/**
 * Created by liguanyi on 16-3-16.
 *
 */
public class MoveSimulation {
    private EnvMapManager mEnvMapManager;
    private RobotManager mRobotManager;
    private ISimulationListener mListener;
    private boolean isFinish = false;
    private ResultData mResultData;

    public MoveSimulation(){
        mEnvMapManager = new EnvMapManager(Constant.WIDTH, Constant.HEIGHT);
        mRobotManager = new RobotManager();
        init();
    }

    private void init(){
        //设置初始参数
        for(Rect rect : Constant.OBSTACLES){
            mEnvMapManager.addObstacle(rect);
        }
        mEnvMapManager.moveTo(Constant.POSITION[0], Constant.POSITION[1]);

        mRobotManager.setSensor(new RobotManager.ISensor() {
            @Override
            public byte getSensorData(byte[] direction) {
                return mEnvMapManager.getType(direction[0], direction[1]);
            }

        });
        mRobotManager.setController(new RobotManager.IMoveController() {
            @Override
            public boolean move(byte[] move) {
                return mEnvMapManager.moveOffset(move);
            }
        });
        mRobotManager.setListener(new RobotManager.IRobotListener() {
            @Override
            public void onCalcStart() {

            }

            @Override
            public void onCalcComplete() {
                isFinish = true;
                if (mListener != null) {
                    mListener.onCalcComplete();
                }
            }
        });

        mResultData = new ResultData(mEnvMapManager.getMapData(), mEnvMapManager.getObstacleList(), mEnvMapManager.getHistoryRecord());
    }

    public void setListener(ISimulationListener l){
        mListener = l;
    }

    public ResultData getResultData(){
        return mResultData;
    }

    public void start(){
        if(!isFinish) {
            mRobotManager.start();
        }
    }

    public interface ISimulationListener{

        void onCalcComplete();

    }

    public static class ResultData{
        public float coveragePercent;
        public float repeatPercent;
        public byte[][] mapData;
        public List<Rect> obstacleList;
        public List<int[]> historyRecord;

        public ResultData(byte[][] mapData, List<Rect> obstacleList, List<int[]> historyRecord){
            this.mapData = mapData;
            this.obstacleList = obstacleList;
            this.historyRecord = historyRecord;
        }

        public void update(){
            int total = 0;
            float repeat = 0;
            float cleared = 0;
            for (byte[] aMapData : mapData) {
                for (int j = 0; j < mapData[0].length; j++) {
                    byte type = aMapData[j];
                    if (type != Constant.TYPE_OBSTACLE) {
                        total++;
                        if (type >= Constant.TYPE_CLEARED) {
                            cleared++;
                            repeat += type - Constant.TYPE_CLEARED + 1;
                        }
                    }
                }
            }
            if(total == 0){
                coveragePercent = -1;
            }
            coveragePercent = cleared/total * 100;
            repeatPercent = repeat/total * 100;
        }
    }

}
