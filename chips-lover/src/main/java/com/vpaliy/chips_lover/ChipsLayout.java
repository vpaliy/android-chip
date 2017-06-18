package com.vpaliy.chips_lover;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ChipsLayout extends ViewGroup{

    private List<ChipView> chips;
    private int lineHeight;
    private int textColor;
    private int chipBackground;
    private int textStyle;

    public ChipsLayout(Context context){
        this(context,null,0);
    }

    public ChipsLayout(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }

    public ChipsLayout(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs){
        chipBackground= ContextCompat.getColor(getContext(), R.color.colorChipBackground);
        textColor=-1;
    }

    private static class LayoutParams extends ViewGroup.LayoutParams {

        final int horizontalSpacing;
        final int verticalSpacing;

        LayoutParams(int horizontalSpacing, int verticalSpacing) {
            super(0, 0);
            this.horizontalSpacing = horizontalSpacing;
            this.verticalSpacing = verticalSpacing;
        }
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(1, 1); // default of 1px spacing
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(1, 1);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        final int width = r - l;
        int xPos = getPaddingLeft();
        int yPos = getPaddingTop();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (xPos + childWidth > width) {
                    xPos = getPaddingLeft();
                    yPos += lineHeight;
                }
                child.layout(xPos, yPos, xPos + childWidth, yPos + childHeight);
                xPos += childWidth + lp.horizontalSpacing;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = View.MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = View.MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        int count = getChildCount();
        int lineHeight = 0;

        int xPos = getPaddingLeft();
        int yPos = getPaddingTop();

        int childHeightMeasureSpec;
        if (View.MeasureSpec.getMode(heightMeasureSpec) == View.MeasureSpec.AT_MOST) {
            childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST);
        } else {
            childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                child.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST), childHeightMeasureSpec);
                final int childWidth = child.getMeasuredWidth();
                lineHeight = Math.max(lineHeight, child.getMeasuredHeight() + lp.verticalSpacing);

                if (xPos + childWidth> width) {
                    xPos = getPaddingLeft();
                    yPos += lineHeight;
                }
                xPos += childWidth + lp.horizontalSpacing;
            }
        }
        this.lineHeight = lineHeight;
        if (View.MeasureSpec.getMode(heightMeasureSpec) == View.MeasureSpec.UNSPECIFIED) {
            height = yPos + lineHeight;

        } else if (View.MeasureSpec.getMode(heightMeasureSpec) == View.MeasureSpec.AT_MOST) {
            if (yPos + lineHeight < height) {
                height = yPos + lineHeight;
            }
        }
        setMeasuredDimension(width, height);
    }

    public void setTags(List<String> tags){
        if(tags==null||tags.isEmpty()) return;
        if(chips==null){
            chips=new ArrayList<>(tags.size());
        }
        if(tags.size()>chips.size()){
            int diff=tags.size()-chips.size();
            for(int index=0;index<diff;index++) {
                ChipView chip = new ChipView(getContext());
                chip.setId(index+1);
                chips.add(chip);
                addView(chip,new LayoutParams(10,10));
            }
        }
        int index=0;
        for(;index<tags.size();index++){
            ChipView chip=chips.get(index);
            chip.setChipText(tags.get(index));
            if(chip.getVisibility()!= View.VISIBLE){
                chip.setVisibility(View.VISIBLE);
            }
        }

        if(index<chips.size()){
            for(;index<chips.size();index++){
                chips.get(index).setVisibility(View.GONE);
            }
        }
        requestLayout();
    }
}
