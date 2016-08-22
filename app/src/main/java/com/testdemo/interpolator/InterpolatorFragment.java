package com.testdemo.interpolator;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import com.testdemo.BaseFragment;
import com.testdemo.R;

/**
 *
 * Created by liguanyi on 2015/7/15.
 */
public class InterpolatorFragment extends BaseFragment {
    int index;
    Interpolator[] interpolators = new Interpolator[]{
        new AccelerateInterpolator(),new AnticipateInterpolator(),
            new AnticipateOvershootInterpolator(), new BounceInterpolator(),
            new DecelerateInterpolator(3), new LinearInterpolator(),
            new OvershootInterpolator(), new CycleInterpolator(2)
    };

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_interpolator;
    }

    @Override
    protected void init() {
        final InterpolatorView view = findViewById(R.id.view_interpolator);
        final TextView show = findViewById(R.id.text_show);
        mRootView.findViewById(R.id.button_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == interpolators.length) {
                    index = 0;
                }
                Interpolator interpolator = interpolators[index];
                view.start(interpolator);
                show.setText(interpolator.getClass().getSimpleName());
                index++;
            }
        });
    }
}
