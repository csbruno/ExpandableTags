package br.com.brunocs.expandabletags;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

public class TagLayout {
    private int paddingTop;
    private int paddingBottom;
    private int paddingLeft;
    private int paddingRight;
    private int marginLeft;
    private int marginRight;
    private int defaultBackgroundColor;
    private int defaultTextColor;
    private float tagRadius;
    private int borderColor;
    private int borderSize;
    private boolean hasBorder;
    private boolean allowCollapse;
    private boolean allowOverlap;
    private boolean allowFill;

    public TagLayout() {
        hasBorder = true;
        allowCollapse = true;
        allowOverlap = true;
    }

    public void loadAttrs(AttributeSet attributeSet, Context context) {
        TypedArray attr = context.obtainStyledAttributes(attributeSet, R.styleable.ExpandableTags, 0, 0);
        try {
            int padding = attr.getDimensionPixelSize(R.styleable.ExpandableTags_padding, 0);
            int margin = attr.getDimensionPixelSize(R.styleable.ExpandableTags_margin, 0);
            if (padding == 0) {
                paddingTop = attr.getDimensionPixelSize(R.styleable.ExpandableTags_paddingTop, 0);
                paddingBottom = attr.getDimensionPixelSize(R.styleable.ExpandableTags_paddingBottom, 0);
                paddingLeft = attr.getDimensionPixelSize(R.styleable.ExpandableTags_paddingLeft, 0);
                paddingRight = attr.getDimensionPixelSize(R.styleable.ExpandableTags_paddingRight, 0);
            } else {
                setPadding(padding);
            }

            if (margin == 0) {
                marginLeft = attr.getDimensionPixelSize(R.styleable.ExpandableTags_marginLeft, 0);
                marginRight = attr.getDimensionPixelSize(R.styleable.ExpandableTags_marginRight, 0);
            } else {
                setMargin(margin);
            }

            defaultBackgroundColor = attr.getColor(R.styleable.ExpandableTags_backgroundColor, Color.CYAN);
            defaultTextColor = attr.getColor(R.styleable.ExpandableTags_textColor, Color.GRAY);
            tagRadius = attr.getFloat(R.styleable.ExpandableTags_tagRadius, 0.0f);
            borderColor = attr.getColor(R.styleable.ExpandableTags_borderColor, Color.BLACK);
            borderSize = attr.getDimensionPixelSize(R.styleable.ExpandableTags_borderSize, 3);
            hasBorder = attr.getBoolean(R.styleable.ExpandableTags_hasBorder, true);
            allowCollapse = attr.getBoolean(R.styleable.ExpandableTags_allowCollapse, true);
            allowOverlap = attr.getBoolean(R.styleable.ExpandableTags_allowOverlap, true);
            allowFill = attr.getBoolean(R.styleable.ExpandableTags_allowFill, true);
        } finally {
            attr.recycle();
        }
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public int getDefaultBackgroundColor() {
        return defaultBackgroundColor;
    }

    public int getDefaultTextColor() {
        return defaultTextColor;
    }

    public float getTagRadius() {
        return tagRadius;
    }


    public int getBorderColor() {
        return borderColor;
    }

    public boolean isHasBorder() {
        return hasBorder;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public void setPadding(int padding) {
        this.paddingBottom = padding;
        this.paddingLeft = padding;
        this.paddingRight = padding;
        this.paddingTop = padding;
    }

    public void setDefaultBackgroundColor(int defaultBackgroundColor) {
        this.defaultBackgroundColor = defaultBackgroundColor;
    }

    public void setDefaultTextColor(int defaultTextColor) {
        this.defaultTextColor = defaultTextColor;
    }

    public void setTagRadius(float tagRadius) {
        this.tagRadius = tagRadius;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public void setHasBorder(boolean hasBorder) {
        this.hasBorder = hasBorder;
    }

    public boolean allowCollapse() {
        return allowCollapse;
    }

    public void setAllowCollapse(boolean allowCollapse) {
        this.allowCollapse = allowCollapse;
    }

    public boolean allowOverlap() {
        return allowOverlap;
    }

    public void setAllowOverlap(boolean allowOverlap) {
        this.allowOverlap = allowOverlap;
    }

    public int getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
    }

    public int getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }

    public void setMargin(int margin) {
        this.marginLeft = margin;
        this.marginRight = margin;
    }

    public int getBorderSize() {
        return borderSize;
    }

    public boolean allowFill() {
        return allowFill;
    }

    public void setAllowFill(boolean allowFill) {
        this.allowFill = allowFill;
    }

    public void setBorderSize(int borderSize) {
        this.borderSize = borderSize;
    }
}
