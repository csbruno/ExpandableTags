package br.com.brunocs.expandabletags;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
public class ExpandableTags extends ViewGroup {

    private TagLayout mTagLayout;
    private ArrayList<Tag> mTagList;
    private static int OVERSIZE =15;

    public ExpandableTags(Context context) {
        this(context, null);
    }

    public ExpandableTags(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);

    }

    public ExpandableTags(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        init();
        mTagLayout.loadAttrs(attributeSet, context);
    }

    public void init() {
        mTagLayout = new TagLayout();
        mTagList = new ArrayList();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("deprecation")
    public void updateChildren() {
        if (getChildCount() > 0) removeAllViews();

        if (mTagList.size() == 0) return;

        final int listCount = mTagList.size();

        for (int i = 0; i < listCount; i++) {
            final Tag tag = mTagList.get(i);

            if (!tag.isVisible()) continue;

            final GradientDrawable backgroundDrawable = generateShape(tag.getBackgroundColor(), tag.getTextColor());

            TextView view = new TextView(getContext());

            view.setGravity(Gravity.CENTER_HORIZONTAL);

            view.setText(tag.getName());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

            view.setLayoutParams(params);

            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackgroundDrawable(backgroundDrawable);
            } else {
                view.setBackground(backgroundDrawable);
            }
            view.setTextColor(mTagLayout.getDefaultTextColor());
            view.setPadding(mTagLayout.getPaddingLeft(), mTagLayout.getPaddingTop(),
                    mTagLayout.getPaddingRight(), mTagLayout.getPaddingBottom());

            addView(view);

        }
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

        super.onMeasure(widthMeasureSpec,heightMeasureSpec);

       final int count = getChildCount();

        final int thisWidth = getMeasuredWidth();
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        int maxChildWidth = 0;
        for (int i = 0;i<count; i++){
            final View child = getChildAt(i);
            child.measure(widthMeasureSpec,heightMeasureSpec);
            if (child.getVisibility() == GONE)
                continue;
            maxChildWidth = Math.max(maxChildWidth,child.getMeasuredWidth() );

        }
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if(thisWidth > maxChildWidth * count && mTagLayout.allowFill()){
                if(i!=0) {
                    child.getLayoutParams().width = thisWidth / count + (int) (Utils.convertDpToPixel(OVERSIZE, getContext()));
                } else {
                    child.getLayoutParams().width = thisWidth / count ;
                }
            } else {
                child.getLayoutParams().width = maxChildWidth + (int)(Utils.convertDpToPixel(OVERSIZE,getContext())) ;

            }

            child.measure(widthMeasureSpec,heightMeasureSpec);
            if (child.getVisibility() == GONE)
                continue;

            // Measure the child.
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            maxWidth += Math.max(maxWidth, child.getMeasuredWidth());

            maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
            childState = combineMeasuredStates(childState, child.getMeasuredState());
        }

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        // Report our final dimensions.
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
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

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();

            if (child.getVisibility() != GONE) {
                int lastpos = tWidth - width ;
                int posx = i== 0 ? 0:  (lastpos / (count-1)) * i ;
                setChildFrame(child,posx ,childTop,width,childTop +height);

             }
        }

    }

    private void setChildFrame(View child, int left, int top, int width, int height) {
        child.layout(left, top, left + width, top + height);
    }

    public void addTag(Tag tag) {
        if (!mTagList.contains(tag)) {
            mTagList.add(tag);
        }
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
        if (mTagList.contains(tag)) {
            return true;
        } else {
            return false;
        }
    }
}
