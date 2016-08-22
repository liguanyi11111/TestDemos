package com.testdemo.floatcontrol;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.testdemo.BaseFragment;
import com.testdemo.R;

/**
 * Created by liguanyi on 16-1-14.
 */
public class FloatControlFragment extends BaseFragment {
    private View testView;
    private FloatController mFloatController;


    @Override
    protected int getRootViewId() {
        return R.layout.fragment_float_control;
    }

    @Override
    protected void init() {
        mFloatController = new FloatController(getActivity().getApplicationContext());
        final int[] colors = new int[]{
                Color.BLUE, Color.GREEN
        };
        super.init();
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int[] location = new int[2];
                testView.getLocationOnScreen(location);
                Log.d("lgy", location[0] + " " + location[1] + " " + (location[0] + testView.getWidth()) + " " + (location[1] + testView.getHeight()));
                mFloatController.updatePosition(location[0], location[1], testView.getWidth(), testView.getHeight());
            }

            @Override
            public void onPageSelected(int position) {
                int[] location = new int[2];
                testView.getLocationOnScreen(location);
                Log.d("lgy", location[0] + " " + location[1] + " " + (location[0] + testView.getWidth()) + " " + (location[1] + testView.getHeight()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                LinearLayout linearLayout;
                if (position == 0) {
                    linearLayout = new MyTestLayout(getActivity());
                    testView = linearLayout;
                } else {
                    linearLayout = new LinearLayout(getActivity());
                }
                linearLayout.setBackgroundColor(colors[position]);
                linearLayout.setGravity(Gravity.CENTER);
                TextView view = new TextView(getActivity());
                view.setText("" + position);
                view.setTextSize(25);
                view.setTextColor(Color.BLACK);
                linearLayout.addView(view, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                container.addView(linearLayout, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return linearLayout;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
    }

    private class MyTestLayout extends LinearLayout{

        public MyTestLayout(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
        }
    }

}
