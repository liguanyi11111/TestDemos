package com.testdemo.viewtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.testdemo.R;

/**
 * Created by liguanyi on 16-1-6.
 *
 */
public class ItemView extends ViewGroup implements View.OnClickListener{

    private Context mContext;
    private ImageView mIconView;
    private View mDetailLayout;
    private ViewGroup mDetailView;
    private TextView mTitleView;
    private ItemData mData;

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    protected void setData(ItemData data){
        mData = data;
        refreshData(mData);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mIconView = (ImageView)findViewById(R.id.item_view_icon);
        mDetailLayout = findViewById(R.id.item_view_detail_layout);
        mDetailView = (ViewGroup)findViewById(R.id.item_view_detail_content_layout);
        mTitleView = (TextView) findViewById(R.id.item_view_title);
        for(int i = 0 ; i< mDetailView.getChildCount() ; i++){
            final int index = i;
            mDetailView.getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.click(index);
                }
            });
        }
    }

    private void refreshData(ItemData data){
        mTitleView.setText(data.title);
        if(data.icon != null){
            mIconView.setImageBitmap(data.icon);
        }
    }

    @Override
    public int getMinimumWidth() {
        return mIconView.getMeasuredWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        measureChild(mIconView, widthMeasureSpec, heightMeasureSpec);
        measureChild(mTitleView, widthMeasureSpec, heightMeasureSpec);
        int parentWidth = ((View)getParent()).getMeasuredWidth();
        mDetailLayout.measure(MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST), heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int iconWidth = mIconView.getMeasuredWidth();
        int iconHeight = mIconView.getMeasuredHeight();
        int titleWidth = mTitleView.getMeasuredWidth();
        int titleHeight = mTitleView.getMeasuredHeight();
        int iconLeft = (getMeasuredWidth() - iconWidth) / 2;
        int iconTop = (getMeasuredHeight() - iconHeight - titleHeight) / 2;
        int titleLeft = (getMeasuredWidth() - titleWidth) / 2;
        int titleTop = iconTop + iconHeight;
        mIconView.layout(iconLeft, iconTop, iconLeft + iconWidth, iconTop + iconHeight);
        mTitleView.layout(titleLeft, titleTop, titleLeft + titleWidth, titleTop + titleHeight);
        if(getWidth() > getMeasuredWidth()){
            int parentWidth = ((View)getParent()).getMeasuredWidth();
            float percent = (float)(getWidth() - getMeasuredWidth())/(parentWidth - getMeasuredWidth());
            int detailWidth = mDetailLayout.getMeasuredWidth();
            int detailHeight = mDetailLayout.getMeasuredHeight();
            int detailLeft = iconLeft + iconWidth;
            int detailTop = iconTop + (iconHeight + titleHeight - detailHeight) / 2;
            if(percent > 0){
                mDetailView.setTranslationX(detailWidth * (percent - 1));
                mDetailView.setAlpha(percent);
            }
            mDetailLayout.layout(detailLeft, detailTop, detailLeft + detailWidth, detailTop + detailHeight);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getX() > getMeasuredWidth()){
            return false;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {

    }

    public static class ItemData{
        public String title;
        public Bitmap icon;

        public ItemData(String title , Bitmap icon){
            this.icon = icon;
            this.title = title;
        }

        private void click(int index){
            Log.d("test", "----" + index);
        }
    }
}
