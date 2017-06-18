package com.vpaliy.chips_lover;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChipView extends RelativeLayout{

    private TextView chipTextView;
    private Chip chip;
    private ImageView fronIcon;
    private ImageView endIcon;

    public ChipView(Context context) {
        this(context, null, 0);
    }

    public ChipView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttrs(attrs);
        setUp();
    }

    private void setAttrs(AttributeSet attrs){
        int icon=-1;
        if(attrs!=null){
            TypedArray array=getContext().obtainStyledAttributes(attrs,R.styleable.ChipView);
            chip.text=array.getString(R.styleable.ChipView_chip_text);
            chip.backgroundColor=array.getColor(R.styleable.ChipView_chip_backgroundColor,color(R.color.colorChipBackground));
            chip.textColor=array.getColor(R.styleable.ChipView_chipTextColor,color(R.color.colorChipText));
            chip.textStyle=array.getResourceId(R.styleable.ChipView_chipTextStyle,-1);
            chip.isDefaultAnimation=array.getBoolean(R.styleable.ChipView_chipDefaultAnimation,false);
            chip.selectedEndColor=array.getColor(R.styleable.ChipView_chip_selectedEndColor,color(R.color.colorSelectedEndIcon));
            chip.closeable=array.getBoolean(R.styleable.ChipView_chipCloseable,false);
            chip.selectedFrontColor=array.getColor(R.styleable.ChipView_chip_selectedFrontColor,color(R.color.colorSelectedFrontIcon));
            chip.selectable=array.getBoolean(R.styleable.ChipView_chipSelectable,false);
            chip.selectedBackgroundColor=array.getColor(R.styleable.ChipView_chip_selectedBackgroundColor,
                    color(R.color.colorSelectedChipBackground));
            chip.selectedTextColor=array.getColor(R.styleable.ChipView_chip_selectedTextColor,
                    color(R.color.colorChipTextSelected));
            icon=array.getResourceId(R.styleable.ChipView_chipFrontIcon,-1);
            if(icon!=-1) chip.frontIcon=drawable(icon);
            icon=array.getResourceId(R.styleable.ChipView_chipEndIcon,-1);
            if(icon!=-1) chip.endIcon=drawable(icon);
            array.recycle();
        }
    }

    private int color(@ColorRes int color){
        return ContextCompat.getColor(getContext(),color);
    }

    private Drawable drawable(@DrawableRes int drawable){
        return ContextCompat.getDrawable(getContext(), drawable);
    }

    private void setUp(){
        initBackgroundColor();
        initTextView();
    }

    private void initTextView() {
        chipTextView= new TextView(getContext(),null,chip.textStyle);

        RelativeLayout.LayoutParams chipTextParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        chipTextParams.addRule(CENTER_IN_PARENT);

        int margins = (int) getResources().getDimension(R.dimen.spacing_medium);
        int bottom=(int)getResources().getDimension(R.dimen.spacing_small);
        chipTextParams.setMargins(margins, bottom, margins,bottom);
        chipTextView.setLayoutParams(chipTextParams);
        chipTextView.setText(chip.text);
        this.addView(chipTextView);
    }

    private void initBackgroundColor() {
        PaintDrawable bgDrawable = new PaintDrawable(chip.backgroundColor);
        bgDrawable.setCornerRadius(getResources().getDisplayMetrics().density*32f);
        setBackgroundDrawable(bgDrawable);
    }

    public void setChipText(String chipText) {
        this.chip.text=chipText;
        chipTextView.setText(chipText);
    }

    public String getChipText() {
        return chip.text;
    }
}
