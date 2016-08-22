package com.testdemo.viewtest;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.testdemo.viewtest.ItemView.ItemData;
import java.util.List;

/**
 * Created by liguanyi on 16-1-4.
 *
 */
public class ExpandableView extends ViewGroup implements IconArrayView.ItemClickListener{
    private static final int MAX_ITEM_ARRAY_COUNT = 2;
    private static final int ANIMATOR_DURATION = 500;
    private Context mContext;
    private ValueAnimator mOpenAnimator;
    private ValueAnimator mCloseAnimator;
    private float mAnimatorPercent;

    public ExpandableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    private void init(){
        initAnimator();
    }

    public void openOrClose(){
        if(mAnimatorPercent == 0 && !mOpenAnimator.isRunning()){
            mOpenAnimator.start();
        }else if(mAnimatorPercent == 1 && !mCloseAnimator.isRunning()){
            mCloseAnimator.start();
        }else{
            // 暂时不允许在运行时点击
        }
    }

    public boolean isAllowOpen(){
        return getChildCount() > 1;
    }

    public boolean isOpened(){
        return mAnimatorPercent == 1;
    }

    private void initAnimator(){
        mOpenAnimator = ValueAnimator.ofFloat(0, 1);
        mOpenAnimator.setDuration(ANIMATOR_DURATION);
        mOpenAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mOpenAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorPercent = (float) animation.getAnimatedValue();
                requestLayout();
            }
        });

        mCloseAnimator = ValueAnimator.ofFloat(1, 0);
        mCloseAnimator.setDuration(ANIMATOR_DURATION);
        mCloseAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mCloseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorPercent = (float) animation.getAnimatedValue();
                requestLayout();
            }
        });
    }

    public void setData(List<ItemData> dataList){
        int new_count = getViewCount(dataList.size());
        if(new_count > MAX_ITEM_ARRAY_COUNT){
            new_count = MAX_ITEM_ARRAY_COUNT;
        }
        int old_count = getViewCount(getChildCount());
        int dValue = new_count - old_count;
        for(int i = 0; i < Math.abs(dValue); i++) {
            if (dValue > 0) {
                ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                IconArrayView view = new IconArrayView(mContext);
                view.setItemClickListener(this);
                addView(view, params);
            } else {
                removeView(getChildAt(0));
            }
        }
        //refresh
        for(int i = 0; i < getChildCount() ; i++){
            IconArrayView view = (IconArrayView)getChildAt(i);
            if(dataList.size() > i * IconArrayView.MAX_ITEM_COUNT) {
                view.setData(dataList.subList(i * IconArrayView.MAX_ITEM_COUNT, dataList.size()));
            }else{
                throw new RuntimeException("ExpandableView ERROR： 数据数量与View无法对应");
            }
        }
    }

    @Override
    public void onItemClick(int index, IconArrayView view) {
        if(view.isOpened()){
            view.closeItem();
        }else {
            for (int i = 0; i < getChildCount(); i++) {
                IconArrayView child = (IconArrayView) getChildAt(i);
                if(child.isOpened()) {
                    child.closeItem();
                }
            }
            view.openItem(index);
        }
    }

    private int getViewCount(int dataSize){
        return dataSize / IconArrayView.MAX_ITEM_COUNT + (dataSize % IconArrayView.MAX_ITEM_COUNT == 0 ? 0 : 1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxHeight = 0;
        int minHeight = 0;
        for(int i = 0 ; i < getChildCount() ; i++){
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
            if(i == 0){
                minHeight = view.getMeasuredHeight();
            }
            maxHeight += view.getMeasuredHeight();
        }
        int nowHeight = minHeight + (int)((maxHeight - minHeight) * mAnimatorPercent);
        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(nowHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int tempTop = 0;
        for(int i = 0 ; i < getChildCount() ; i++){
            View view = getChildAt(i);
            view.layout(0, tempTop, view.getMeasuredWidth(), tempTop + view.getMeasuredHeight());
            if(getHeight() <= view.getMeasuredHeight()){
                break;
            }
            tempTop += view.getMeasuredHeight();
        }
    }
}
