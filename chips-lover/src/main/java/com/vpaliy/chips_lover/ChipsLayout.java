package com.vpaliy.chips_lover;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChipsLayout extends ViewGroup
        implements ChipView.OnChipChangeListener{

    private List<ChipView> chips;
    private int lineHeight;
    private int horizontalSpacing;
    private int verticalSpacing;
    private ChipBuilder chipBuilder;
    private boolean deleteAnimationEnabled;
    private int removeAnimationRes;
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
        if(attrs!=null){
            TypedArray array=getContext().obtainStyledAttributes(attrs,R.styleable.ChipsLayout);
            chipBuilder=ChipBuilder.create(getContext(),array);
            horizontalSpacing=(int)(array.getDimension(R.styleable.ChipsLayout_chip_layout_horizontal_margin,1));
            verticalSpacing=(int)(array.getDimension(R.styleable.ChipsLayout_chip_layout_vertical_margin,1));
            deleteAnimationEnabled=array.getBoolean(R.styleable.ChipsLayout_remove_anim_enabled,true);
            removeAnimationRes=array.getInteger(R.styleable.ChipsLayout_remove_anim,-1);
            int arrayRes=array.getResourceId(R.styleable.ChipsLayout_chips_array,-1);
            if(arrayRes!=-1){
                String[] textArray=getResources().getStringArray(arrayRes);
                setTags(Arrays.asList(textArray));
            }
            array.recycle();
            return;
        }
        chipBuilder=ChipBuilder.create(getContext());
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
        return new LayoutParams(horizontalSpacing, verticalSpacing);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(horizontalSpacing, verticalSpacing);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public void updateColorWith(ChipBuilder chipBuilder) {
        this.chipBuilder = chipBuilder;
        if(chips!=null){
            for(ChipView chipView:chips){
                chipView.setSelectedTextColor(chipBuilder.selectedTextColor);
                chipView.setSelectedFrontColor(chipBuilder.selectedFrontColor);
                chipView.setSelectedEndColor(chipBuilder.selectedEndColor);
                chipView.setSelectedBackgroundColor(chipBuilder.selectedBackgroundColor);
                chipView.setFrontIconColor(chipBuilder.frontIconColor);
                chipView.setEndIconColor(chipBuilder.endIconColor);
                chipView.setTextColor(chipBuilder.textColor);
                chipView.setBackgroundColor(chipBuilder.backgroundColor);
            }
        }

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
    public void onScaleChanged(ChipView chipView) {
        requestLayout();
    }

    @Override
    public void onRemove(ChipView chipView) {
        if(chips.contains(chipView)) {
            if (Build.VERSION.SDK_INT >= 19 && deleteAnimationEnabled) {
                if(removeAnimationRes!=-1) {
                    Transition transition=TransitionInflater.from(getContext()).inflateTransition(removeAnimationRes);
                    TransitionManager.beginDelayedTransition(this,transition);
                }else {
                    TransitionManager.beginDelayedTransition(this);
                }
            }
            removeView(chipView);
        }
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = verticalSpacing;
    }

    public int getVerticalSpacing() {
        return verticalSpacing;
    }

    public int getHorizontalSpacing() {
        return horizontalSpacing;
    }

    public List<ChipView> getChips() {
        return chips;
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
                ChipView chip = chipBuilder.build();
                chip.setChipChangeListener(this);
                chips.add(chip);
                addView(chip);
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

    public ChipView assignListenerByName(String chipTitle, OnClickListener listener){
        if(chips!=null){
            for(ChipView chip:chips){
                if(TextUtils.equals(chip.getChipText(),chipTitle)){
                    chip.setOnClickListener(listener);
                    return chip;
                }
            }
        }
        return null;
    }

    public void setClickListenerToAll(OnClickListener listener){
        if(chips!=null){
            for(ChipView chip:chips){
                chip.setOnClickListener(listener);
            }
        }
    }

    public void setChips(List<ChipView> chipViews){
        if(chipViews!=null){
            for(ChipView chip:chipViews){
                if(!chips.contains(chip)){
                    chips.add(chip);
                    addView(chip);
                }
            }
        }
    }

}
