package com.testdemo.viewtest;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.testdemo.viewtest.ItemView.ItemData;

import com.testdemo.R;
import java.util.List;

/**
 * Created by liguanyi on 16-1-6.
 *
 */
public class IconArrayView extends ViewGroup implements View.OnClickListener{
    protected static final int MAX_ITEM_COUNT = 4;
    private static final int ANIMATOR_DURATION = 300;
    private static final int MIN_TEXT_INTERVAL_DP = 16;   //DP
    private static final int HEIGHT_DP = 80;   //DP
    private final int MIN_TEXT_INTERVAL_PX;
    private final int HEIGHT_PX;

    private Context mContext;
    private ValueAnimator mOpenAnimator;
    private ValueAnimator mCloseAnimator;
    private ItemClickListener mListener;
    private float mAnimatorPercent;
    private float mMoveItem;

    public IconArrayView(Context context){
        this(context, null);
    }

    public IconArrayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconArrayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        float density = context.getResources().getDisplayMetrics().density;
        MIN_TEXT_INTERVAL_PX = (int)(MIN_TEXT_INTERVAL_DP * density);
        HEIGHT_PX = (int)(HEIGHT_DP * density);
        init();
    }

    public void setData(List<ItemData> dataList){
        if(dataList == null){
            return;
        }
        dataList = dataList.subList(0, Math.min(dataList.size(), MAX_ITEM_COUNT));
        int dValue = dataList.size() - getChildCount();
        for(int i = 0 ; i < Math.abs(dValue) ; i++){
            if(dValue > 0){
                ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                View view = View.inflate(mContext, R.layout.icon_array_item, null);
                addView(view, params);
                view.setOnClickListener(this);
            }else{
                removeView(getChildAt(0));
            }
        }
        //刷新显示
        for(int i = 0; i < getChildCount() ; i++){
            ItemView itemView = (ItemView)getChildAt(i);
            ItemData data = dataList.get(i);
            itemView.setTag(i);
            itemView.setData(data);
        }
    }

    public void setItemClickListener(ItemClickListener l){
        mListener = l;
    }

    public boolean isOpened(){
        return mAnimatorPercent == 1;
    }

    public void openItem(int index){
        if(mAnimatorPercent == 0 && !mOpenAnimator.isRunning()){
            mMoveItem = index;
            mOpenAnimator.start();
        }
    }

    public void closeItem(){
        if(mAnimatorPercent == 1 && !mCloseAnimator.isRunning()){
            mCloseAnimator.start();
        }
    }

    @Override
    public void onClick(View v) {
        if(mListener != null){
            mListener.onItemClick((int) v.getTag(), this);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("IconArrayView", "onMeasure");
        //优先设置自己大小
        int myHeightMeasureSpec = MeasureSpec.makeMeasureSpec(HEIGHT_PX, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, myHeightMeasureSpec);
        //计算子View大小
        int childWidth = (getMeasuredWidth() - MIN_TEXT_INTERVAL_PX * (MAX_ITEM_COUNT - 1)) / MAX_ITEM_COUNT;
        int childWidthSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
        for(int i = 0 ; i < getChildCount() ; i++){
            getChildAt(i).measure(childWidthSpec, myHeightMeasureSpec);
//            measureChild(getChildAt(i), childWidthSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d("IconArrayView ", " layout " + left + " " + top + " " + right + " " + bottom);
        Log.d("IconArrayView ", " mAnimatorPercent " + mAnimatorPercent + " mItem" + mMoveItem);
        if(getChildCount() > 0) {
            int childWidth = getChildAt(0).getMeasuredWidth();
            int iconLeft = (int)((childWidth + MIN_TEXT_INTERVAL_PX) * mMoveItem * -mAnimatorPercent);
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                int tempWidth = childWidth;
                if(i == mMoveItem){
                    tempWidth += (getMeasuredWidth() - childWidth) * mAnimatorPercent;
                }
                view.layout(iconLeft, 0, iconLeft + tempWidth, bottom - top);
                iconLeft += tempWidth + MIN_TEXT_INTERVAL_PX;
            }
        }
    }

    private void init(){
        setBackgroundColor(Color.YELLOW);
        initAnimator();
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

    public interface ItemClickListener{

        void onItemClick(int index, IconArrayView view);

    }

}
