package br.com.brunocs.expandabletags;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ResizeAnimation extends Animation {
    private View mView;

    private float mToWidth;
    private float mFromWidth;

    public ResizeAnimation(View v, float fromWidth, float toWidth) {

        mToWidth = toWidth;

        mFromWidth = fromWidth;
        mView = v;
        setDuration(300);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float width = (mToWidth - mFromWidth) * interpolatedTime + mFromWidth;
         mView.getLayoutParams().width = (int) width;;
        mView.requestLayout();
    }
}