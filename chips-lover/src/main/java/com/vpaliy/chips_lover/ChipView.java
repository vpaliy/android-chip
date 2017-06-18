package com.vpaliy.chips_lover;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChipView extends RelativeLayout{

    private TextView chipTextView;
    private Chip chip;
    private ImageView frontIcon;
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
        if(attrs!=null){
            TypedArray array=getContext().obtainStyledAttributes(attrs,R.styleable.ChipView);
            chip.text=array.getString(R.styleable.ChipView_chip_text);
            chip.backgroundColor=array.getColor(R.styleable.ChipView_chip_backgroundColor,color(R.color.colorChipBackground));
            chip.textColor=array.getColor(R.styleable.ChipView_chipTextColor,color(R.color.colorChipText));
            chip.textStyle=array.getResourceId(R.styleable.ChipView_chipTextStyle,-1);
            chip.endIconColor=array.getColor(R.styleable.ChipView_chipEndIconColor,-1);
            chip.frontIconColor=array.getColor(R.styleable.ChipView_chipFrontIconColor,-1);
            chip.isDefaultAnimation=array.getBoolean(R.styleable.ChipView_chipDefaultAnimation,false);
            chip.selectedEndColor=array.getColor(R.styleable.ChipView_chip_selectedEndColor,color(R.color.colorSelectedEndIcon));
            chip.closeable=array.getBoolean(R.styleable.ChipView_chipCloseable,false);
            chip.selectedFrontColor=array.getColor(R.styleable.ChipView_chip_selectedFrontColor,color(R.color.colorSelectedFrontIcon));
            chip.selectable=array.getBoolean(R.styleable.ChipView_chipSelectable,false);
            chip.frontIconSize=array.getDimensionPixelSize(R.styleable.ChipView_chipFrontIconSize,dimens(R.dimen.chip_front_icon_size));
            chip.endIconSize=array.getDimensionPixelSize(R.styleable.ChipView_chipEndIconSize,dimens(R.dimen.chip_end_icon_size));
            chip.selectedBackgroundColor=array.getColor(R.styleable.ChipView_chip_selectedBackgroundColor,
                    color(R.color.colorSelectedChipBackground));
            chip.selectedTextColor=array.getColor(R.styleable.ChipView_chip_selectedTextColor,
                    color(R.color.colorChipTextSelected));
            int icon=array.getResourceId(R.styleable.ChipView_chipFrontIcon,-1);
            if(icon!=-1) chip.frontIcon=drawable(icon);
            icon=array.getResourceId(R.styleable.ChipView_chipEndIcon,R.drawable.ic_close);
            chip.endIcon=drawable(icon);
            array.recycle();
        }
    }

    private int color(@ColorRes int color){
        return ContextCompat.getColor(getContext(),color);
    }

    private Drawable drawable(@DrawableRes int drawable){
        return ContextCompat.getDrawable(getContext(), drawable);
    }

    private int dimens(@DimenRes int dimen){
        return (int)getResources().getDimension(dimen);
    }

    private void setUp(){
        initBackgroundColor();
        initEndIcon();
        initFrontIcon();
        initTextView();
    }

    private void initTextView() {
        chipTextView= new TextView(getContext(),null,chip.textStyle);

        RelativeLayout.LayoutParams chipTextParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        if(frontIcon!=null||endIcon!=null){
            chipTextParams.addRule(RIGHT_OF,R.id.chip_front_icon);
        }
        chipTextParams.addRule(CENTER_VERTICAL);

        int margins = (int) getResources().getDimension(R.dimen.spacing_medium);
        int bottom=(int)getResources().getDimension(R.dimen.spacing_small);
        chipTextParams.setMargins(margins, bottom, margins,bottom);
        chipTextView.setLayoutParams(chipTextParams);
        chipTextView.setText(chip.text);
        this.addView(chipTextView);
    }

    private void initFrontIcon(){
        if(chip.frontIcon!=null) {
            frontIcon = new ImageView(getContext());
            LayoutParams params = new LayoutParams(chip.frontIconSize, chip.frontIconSize);
            params.addRule(ALIGN_PARENT_LEFT);
            frontIcon.setLayoutParams(params);
            frontIcon.setId(R.id.chip_front_icon);

            if (chip.frontIcon != null) {
                Bitmap bitmap = BitmapDrawable.class.cast(chip.frontIcon).getBitmap();
                bitmap = ChipUtils.getSquareBitmap(bitmap);
                bitmap = ChipUtils.getScaledBitmap(getContext(), bitmap, chip.frontIconSize);
                frontIcon.setImageBitmap(ChipUtils.getCircleBitmap(getContext(), bitmap, chip.frontIconSize));
            }
            frontIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            addView(frontIcon);
        }
    }

    private void initEndIcon(){
        if(chip.closeable){
            endIcon=new ImageView(getContext());
            LayoutParams params=new LayoutParams(chip.endIconSize,chip.endIconSize);
            params.addRule(CENTER_VERTICAL);
            params.addRule(RIGHT_OF,R.id.chip_text);
            params.setMargins(dimens(R.dimen.chip_icon_horizontal_margin),
                            0,dimens(R.dimen.chip_icon_horizontal_margin), 0);
            endIcon.setLayoutParams(params);
            endIcon.setId(R.id.chip_end_icon);
            endIcon.setImageDrawable(chip.endIcon);

            if(chip.endIconColor!=-1){
                ChipUtils.setIconColor(endIcon,chip.endIconColor);
            }
            endIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            addView(endIcon);
        }
    }

    private void initBackgroundColor() {
        PaintDrawable bgDrawable = new PaintDrawable(chip.backgroundColor);
        bgDrawable.setCornerRadius(getResources().getDimension(R.dimen.spacing_huge));
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
