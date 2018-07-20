package br.com.brunocs.expandabletags;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.ArrayList;

/*
 * Copyright 2016 Bruno Soares
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ExpandableTags extends ViewGroup implements GestureDetector.OnGestureListener {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private TagLayout mTagLayout;
    private ArrayList<Tag> mTagList;
    private Boolean isAnimating = false;

    private GestureDetector gestureScanner;

    private Scroller _scroller;
    private int _viewPortX, _viewPortY;


    public ExpandableTags(Context context) {
        this(context, null);
        gestureScanner = new GestureDetector(this);
        _scroller = new Scroller(this.getContext());

    }

    public ExpandableTags(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
        gestureScanner = new GestureDetector(this);
        _scroller = new Scroller(this.getContext());

    }

    public ExpandableTags(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        gestureScanner = new GestureDetector(this);
        _scroller = new Scroller(this.getContext());

        init();
        mTagLayout.loadAttrs(attributeSet, context);
    }

    public void init() {
        mTagLayout = new TagLayout();
        mTagList = new ArrayList();
    }

    public void animateCollapse() {

        final int count = getChildCount();
        final int tWidth = getMeasuredWidth();
        int widthSum = 0;
        for (int i = 0; i < count; i++) {
            final RelativeLayout child = (RelativeLayout) getChildAt(i);
            widthSum += child.getWidth();
            child.clearAnimation();
        }
        if (!mTagLayout.isCollapsed()) {

            for (int i = 0; i < count; i++) {
                final RelativeLayout child = (RelativeLayout) getChildAt(i);
                if (tWidth > widthSum) return;
                int lastpos = tWidth - child.getMeasuredWidth();
                final int posx = i == 0 ? 0 : ((lastpos / (count - 1)) * i);
                TranslateAnimation anim = new TranslateAnimation(0, posx - child.getX(), 0, 0);
                anim.setDuration(1000);

                anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        isAnimating = false;
                    }
                });


                anim.setFillAfter(true);
                child.startAnimation(anim);

                mTagLayout.setCollapsed(true);
            }
        } else {
            for (int i = 0; i < count; i++) {
                final RelativeLayout child = (RelativeLayout) getChildAt(i);
                if (tWidth > widthSum) return;
                int lastpos = tWidth - child.getMeasuredWidth();
                final int posx = i == 0 ? 0 : ((lastpos / (count - 1)) * i);
                TranslateAnimation anim = new TranslateAnimation(0, posx - child.getX(), 0, 0);
                anim.setDuration(1000);

                anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        isAnimating = false;
                    }
                });

                anim.setFillAfter(true);
                anim.setInterpolator(new ReverseInterpolator());
                child.startAnimation(anim);

                mTagLayout.setCollapsed(false);
            }
        }

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
    }

    public void updateChildren() {
        if (mTagLayout.isCollapsed()) {
            animateCollapse();
        }
        if (getChildCount() > 0) removeAllViewsInLayout();

        if (mTagList.size() == 0) return;


        final int listCount = mTagList.size();

        for (int i = 0; i < listCount; i++) {
            final Tag tag = mTagList.get(i);

            if (!tag.isVisible()) continue;

            final GradientDrawable backgroundDrawable = generateShape(tag.getBackgroundColor(), tag.getTextColor());

            HorizontalScrollView horizontalScrollView = new HorizontalScrollView(getContext());
            RelativeLayout relativeLayout = new RelativeLayout(getContext());
            TextView textView = new TextView(getContext());

            textView.setGravity(Gravity.CENTER_HORIZONTAL);

            textView.setText(tag.getName());

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

            textView.setLayoutParams(params);

            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                relativeLayout.setBackgroundDrawable(backgroundDrawable);
            } else {
                relativeLayout.setBackground(backgroundDrawable);
            }
            textView.setTextColor(mTagLayout.getDefaultTextColor());
            textView.setPadding(Utils.dpToPixelint(mTagLayout.getPaddingLeft(), getContext()),
                    Utils.dpToPixelint(mTagLayout.getPaddingTop(), getContext()),
                    Utils.dpToPixelint(mTagLayout.getPaddingRight(), getContext()),
                    Utils.dpToPixelint(mTagLayout.getPaddingBottom(), getContext()));

            relativeLayout.addView(textView);
            relativeLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            addView(relativeLayout);

        }

        invalidate();
        requestLayout();


    }

    private GradientDrawable generateShape(@Nullable Integer backgroundColor, @Nullable Integer borderColor) {
        GradientDrawable shape = new GradientDrawable();

        shape.setShape(GradientDrawable.RECTANGLE);

        shape.setCornerRadius(mTagLayout.getTagRadius());
        if (backgroundColor == null) {
            shape.setColor(mTagLayout.getDefaultBackgroundColor());
        } else {
            shape.setColor(backgroundColor);
        }
        if (mTagLayout.isHasBorder()) {
            if (borderColor == null) {
                shape.setStroke(mTagLayout.getBorderSize(), mTagLayout.getBorderColor());
            } else {
                shape.setStroke(mTagLayout.getBorderSize(), borderColor);

            }
        }

        return shape;
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int count = getChildCount();

        final int thisWidth = getMeasuredWidth();
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        int maxChildWidth = 0;
        int widthSum = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            child.measure(widthMeasureSpec, heightMeasureSpec);
            if (child.getVisibility() == GONE)
                continue;

            if (mTagLayout.isCollapsed()) {

                widthSum += child.getMeasuredWidth();
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());

            }
            maxChildWidth = Math.max(maxChildWidth, child.getMeasuredWidth());
        }
        if (mTagLayout.isAllowResize()) {
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);
                child.getLayoutParams().width = maxChildWidth;


                child.measure(widthMeasureSpec, heightMeasureSpec);
                if (child.getVisibility() == GONE)
                    continue;

                // Measure the child.
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }
        }
        if (mTagLayout.allowFill()) {
            if (widthSum < thisWidth) {
                for (int i = 0; i < count; i++) {
                    final View child = getChildAt(i);
                    if (i != 0) {
                        child.getLayoutParams().width = thisWidth / count;
                    } else {
                        child.getLayoutParams().width = thisWidth / count;
                    }

                    child.measure(widthMeasureSpec, heightMeasureSpec);
                    if (child.getVisibility() == GONE)
                        continue;

                    // Measure the child.
                    measureChild(child, widthMeasureSpec, heightMeasureSpec);

                }
            }
        }
        if (mTagLayout.isCollapsed()) {

            setMeasuredDimension(widthSum, maxHeight);
            return;

        } else {
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);
                if (mTagLayout.isAllowResize()) {
                    if (mTagLayout.allowFill()) {
                        if (thisWidth > maxChildWidth * count) {
                            if (i != 0) {
                                child.getLayoutParams().width = (thisWidth / count);
                            } else {
                                child.getLayoutParams().width = thisWidth / count;
                            }
                        } else {
                            child.getLayoutParams().width = maxChildWidth;

                        }
                    } else {

                    }

                }

                if (child.getVisibility() == GONE)
                    continue;

                // Measure the child.
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth());

                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }

            // Check against our minimum height and width
            maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
            maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

            // Report our final dimensions.
            setMeasuredDimension(getMeasuredWidth(), maxHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int parentLeft = getPaddingLeft();
        final int parentRight = r - l - getPaddingRight();
        final int parentTop = getPaddingTop();
        final int parentBottom = b - t - getPaddingBottom();
        final int count = getChildCount();

        int childLeft = parentLeft;
        int childTop = parentTop;

        final int tWidth = getMeasuredWidth();

        int currentPosX = 0;
        int currentPosY = 0;
        int widthSum = 0;

        if (_scroller.computeScrollOffset()) {
            scrollNow(_scroller.getCurrX());

            /* This is necessary to request layout while in layout loop */
            post(new Runnable() {
                public void run() {
                    requestLayout();
                }
            });
        }
        if (mTagLayout.isCollapsed()) {
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);
                widthSum += child.getMeasuredWidth();
            }
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);
                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();

                if (child.getVisibility() != GONE) {

                    int lastpos = tWidth - width;

                    int posx = 0;
                    if (tWidth > widthSum) {
                        posx = currentPosX;
                    } else {
                        posx = i == 0 ? 0 : ((lastpos / (count - 1)) * i);
                    }

                    setChildFrame(child, Math.min(posx +_viewPortX,posx) , childTop, width, childTop + height);
                    currentPosX += width;
                }
            }
        } else {
            for (int i = 0; i < count; i++) {

                final View child = getChildAt(i);
                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();

                if (child.getVisibility() != GONE) {

                    setChildFrame(child, Math.min(currentPosX +_viewPortX,currentPosX), childTop, width, childTop + height);
                    currentPosX += width;
                }

            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return gestureScanner.onTouchEvent(me);
    }

    public void scrollNow(float x) {
        _viewPortX = (int) x;

        requestLayout();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float
          velocityX, float velocityY) {
//        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH){
//            return false;
//        }
//        // right to left swipe
//        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
//                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//
//
//        }
//        // left to right swipe
//        else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
//                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//
//        }
        return true;
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {

        if(mTagLayout.isCollapsed()) return false;
        int widthSum = 0 ;
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final RelativeLayout child = (RelativeLayout) getChildAt(i);
            widthSum += child.getWidth();

        }
        int delta = Math.abs(getMeasuredWidth() - widthSum);

            int scrollToX = _viewPortX - (int) distanceX;
           if(getMeasuredWidth() > widthSum) return false;

           Log.d("Value", String.valueOf(scrollToX));
           if(Math.abs(scrollToX  ) < delta ){
               scrollNow(scrollToX);
           }



        return false;

    }


    @Override
    public boolean onDown(MotionEvent e) {
// TODO Auto-generated method stub
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
// TODO Auto-generated method stub
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
// TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
// TODO Auto-generated method stub
    }

    private void setChildFrame(View child, int left, int top, int width, int height) {
        child.layout(left, top, left + width, top + height);
    }

    public void addTag(Tag tag) {
        if (!mTagList.contains(tag)) {
            mTagList.add(tag);
        }
        isAnimating = false;
        updateChildren();
    }

    public void removeTag(Tag tag) {
        if (mTagList.contains(tag)) {
            mTagList.remove(tag);
        }
        updateChildren();
    }

    public void setTagArrayList(ArrayList<Tag> list) {
        mTagList = list;
        updateChildren();
    }

    public void clearTags() {
        mTagList = new ArrayList<>();
        updateChildren();
    }

    public boolean tagExists(Tag tag) {
        return mTagList.contains(tag);
    }

    public class ReverseInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float paramFloat) {
            return Math.abs(paramFloat - 1f);
        }
    }
}
