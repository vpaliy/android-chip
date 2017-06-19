package com.vpaliy.chips_lover;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

public class ChipBuilder {

    String text;
    Drawable endIconDrawable;
    Drawable frontIconDrawable;
    int frontIconColor=-1;
    int endIconColor=-1;
    boolean selectable;
    boolean closeable;
    boolean isDefaultAnimation;
    int textStyle=-1;
    int backgroundColor;
    int textColor;
    int selectedBackgroundColor;
    int selectedTextColor;
    int selectedEndColor;
    int selectedFrontColor;
    private Context context;

    private ChipBuilder(Context context, TypedArray array){
        this(context);
        if(array!=null){
            text=array.getString(R.styleable.ChipView_chip_text);
            backgroundColor=array.getColor(R.styleable.ChipView_chip_backgroundColor,backgroundColor);
            textColor=array.getColor(R.styleable.ChipView_chipTextColor,textColor);
            textStyle=array.getResourceId(R.styleable.ChipView_chipTextStyle,textStyle);
            endIconColor=array.getColor(R.styleable.ChipView_chipEndIconColor,endIconColor);
            frontIconColor=array.getColor(R.styleable.ChipView_chipFrontIconColor,frontIconColor);
            isDefaultAnimation=array.getBoolean(R.styleable.ChipView_chipDefaultAnimation,false);
            selectedEndColor=array.getColor(R.styleable.ChipView_chip_selectedEndColor,selectedEndColor);
            closeable=array.getBoolean(R.styleable.ChipView_chipCloseable,false);
            selectedFrontColor=array.getColor(R.styleable.ChipView_chip_selectedFrontColor,selectedFrontColor);
            selectable=array.getBoolean(R.styleable.ChipView_chipSelectable,false);
            selectedBackgroundColor=array.getColor(R.styleable.ChipView_chip_selectedBackgroundColor,selectedBackgroundColor);
            selectedTextColor=array.getColor(R.styleable.ChipView_chip_selectedTextColor, selectedTextColor);
            int icon=array.getResourceId(R.styleable.ChipView_chipFrontIcon,-1);
            if(icon!=-1) frontIconDrawable=ContextCompat.getDrawable(context,icon);
            icon=array.getResourceId(R.styleable.ChipView_chipEndIcon,-1);
            if(icon!=-1) {
                endIconDrawable = ContextCompat.getDrawable(context,icon);
            }else if(closeable){
                endIconDrawable=ContextCompat.getDrawable(context,R.drawable.ic_close);
            }
        }
    }

    private void init(){
        textColor=ContextCompat.getColor(context,R.color.colorChipText);
        textStyle=-1;
        backgroundColor=ContextCompat.getColor(context,R.color.colorChipBackground);
        endIconColor=-1;
        frontIconColor=-1;
        selectedEndColor=ContextCompat.getColor(context,R.color.colorSelectedEndIcon);
        selectedFrontColor=ContextCompat.getColor(context,R.color.colorSelectedFrontIcon);
        selectedBackgroundColor=ContextCompat.getColor(context,R.color.colorSelectedChipBackground);
        selectedTextColor=ContextCompat.getColor(context,R.color.colorChipTextSelected);
    }

    private ChipBuilder(Context context){
        this.context=context;
        init();
    }

    public ChipBuilder setTextStyle(int textStyle) {
        this.textStyle = textStyle;
        return this;
    }

    public ChipBuilder setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public ChipBuilder setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public ChipBuilder setCloseable(boolean closeable) {
        this.closeable = closeable;
        return this;
    }

    public ChipBuilder setDefaultAnimation(boolean defaultAnimation) {
        isDefaultAnimation = defaultAnimation;
        return this;
    }

    public ChipBuilder setEndIconColor(int endIconColor) {
        this.endIconColor = endIconColor;
        return this;
    }

    public ChipBuilder setEndIconDrawable(Drawable endIconDrawable) {
        this.endIconDrawable = endIconDrawable;
        return this;
    }

    public ChipBuilder setFrontIconColor(int frontIconColor) {
        this.frontIconColor = frontIconColor;
        return this;
    }

    public ChipBuilder setFrontIconDrawable(Drawable frontIconDrawable) {
        this.frontIconDrawable = frontIconDrawable;
        return this;
    }

    public ChipBuilder setSelectable(boolean selectable) {
        this.selectable = selectable;
        return this;
    }

    public ChipBuilder setSelectedBackgroundColor(int selectedBackgroundColor) {
        this.selectedBackgroundColor = selectedBackgroundColor;
        return this;
    }

    public ChipBuilder setSelectedEndColor(int selectedEndColor) {
        this.selectedEndColor = selectedEndColor;
        return this;
    }

    public ChipBuilder setSelectedFrontColor(int selectedFrontColor) {
        this.selectedFrontColor = selectedFrontColor;
        return this;
    }

    public ChipBuilder setSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
        return this;
    }

    public ChipBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public static ChipBuilder create(Context context,TypedArray array){
        return new ChipBuilder(context,array);
    }

    public static ChipBuilder create(Context context){
        return new ChipBuilder(context);
    }

    public ChipView build(){
        return new ChipView(context,this );
    }
}
