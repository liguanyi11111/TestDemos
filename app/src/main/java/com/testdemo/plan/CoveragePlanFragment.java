package com.testdemo.plan;

import android.view.View;
import android.widget.TextView;

import com.testdemo.BaseFragment;
import com.testdemo.R;

/**
 * Created by liguanyi on 16-3-16.
 */
public class CoveragePlanFragment extends BaseFragment {
    MoveSimulation mSimulation = null;
    MapView mMapView;
    TextView mText;
    MoveSimulation.ResultData mResultData;

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_plan;
    }

    @Override
    protected void init() {
        mMapView = findViewById(R.id.map_view);
        mText = findViewById(R.id.text);
        mSimulation = new MoveSimulation();
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSimulation.start();
            }
        });
        findViewById(R.id.path).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlanPath();
            }
        });
        mResultData = mSimulation.getResultData();
        mSimulation.setListener(new MoveSimulation.ISimulationListener() {

            @Override
            public void onCalcComplete() {
                mText.post(new Runnable() {
                    @Override
                    public void run() {
                        mResultData.update();
                        mText.setText(String.format("%.2f", mResultData.coveragePercent) + "% | "
                                + String.format("%.2f", mResultData.repeatPercent) + "%");
                        mMapView.setMovePath(mResultData.historyRecord);
                        mMapView.showPath();
                        mMapView.invalidate();
                    }
                });
            }
        });
        mMapView.setMapData(mResultData.mapData);
        mMapView.setObstacleList(mResultData.obstacleList);
    }


    public void showPlanPath(){
        if(mResultData.historyRecord != null) {
            new Thread() {
                @Override
                public void run() {
                    for (int[] history : mResultData.historyRecord) {
                        mMapView.setRobotPosition(history[0], history[1]);
                        mMapView.postInvalidate();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
