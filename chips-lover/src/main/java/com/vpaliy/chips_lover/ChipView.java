package com.vpaliy.chips_lover;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ChipView extends RelativeLayout{

    private TextView chipTextView;
    private CircleImageView frontIcon;
    private ImageView endIcon;
    private String text;
    private Drawable endIconDrawable;
    private Drawable frontIconDrawable;
    private int frontIconColor;
    private int endIconColor;
    private boolean selectable;
    private boolean closeable;
    private boolean isDefaultAnimation;
    private int textStyle;
    private int backgroundColor;
    private int textColor;
    private int selectedBackgroundColor;
    private int selectedTextColor;
    private int selectedEndColor;
    private int selectedFrontColor;

    public ChipView(Context context) {
        this(context, null, 0);
    }

    public ChipView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        setAttrs(attrs);
        setUp();
    }

    private void init(){
        textColor=color(R.color.colorChipText);
        textStyle=-1;
        backgroundColor=color(R.color.colorChipBackground);
        endIconColor=-1;
        frontIconColor=-1;
        selectedEndColor=color(R.color.colorSelectedEndIcon);
        selectedFrontColor=color(R.color.colorSelectedFrontIcon);
        selectedBackgroundColor=color(R.color.colorSelectedChipBackground);
        selectedTextColor=color(R.color.colorChipTextSelected);
    }
    private void setAttrs(AttributeSet attrs){
        if(attrs!=null){
            TypedArray array=getContext().obtainStyledAttributes(attrs,R.styleable.ChipView);
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
            if(icon!=-1) frontIconDrawable=drawable(icon);
            icon=array.getResourceId(R.styleable.ChipView_chipEndIcon,-1);
            if(icon!=-1) {
                endIconDrawable = drawable(icon);
            }else if(closeable){
                endIconDrawable=drawable(R.drawable.ic_close);
            }
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void setUp(){
        initBackgroundColor();
        initTextView();
        initEndIcon();
        initFrontIcon();
    }

    private void initTextView() {
        chipTextView= new TextView(getContext());
        RelativeLayout.LayoutParams chipTextParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        if(endIconDrawable!=null||frontIconDrawable!=null){
            chipTextParams.addRule(RIGHT_OF,R.id.chip_front_icon);
            chipTextParams.addRule(CENTER_VERTICAL);
        }else {
            chipTextParams.addRule(CENTER_IN_PARENT);
        }
        int left=frontIconDrawable!=null?dimens(R.dimen.chip_text_icon_margin):dimens(R.dimen.chip_text_margin);
        int right=endIconDrawable!=null?0 :dimens(R.dimen.chip_text_margin);

        chipTextParams.setMargins(left,0,right,0);
        chipTextView.setLayoutParams(chipTextParams);
        chipTextView.setId(R.id.chip_text);
        chipTextView.setText(text);
        if(textColor!=-1){
            chipTextView.setTextColor(textColor);
        }
        if(textStyle!=-1){
            if(Build.VERSION.SDK_INT>=23){
                chipTextView.setTextAppearance(textStyle);
            }
        }
        this.addView(chipTextView);
    }

    private void initFrontIcon(){
        if(frontIconDrawable!=null) {
            frontIcon = new CircleImageView(getContext());
            LayoutParams params = new LayoutParams(dimens(R.dimen.chip_front_icon_size), dimens(R.dimen.chip_front_icon_size));
            params.addRule(ALIGN_PARENT_LEFT);
            params.addRule(CENTER_VERTICAL);
            frontIcon.setLayoutParams(params);
            frontIcon.setId(R.id.chip_front_icon);
            frontIcon.setImageDrawable(frontIconDrawable);
            frontIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            addView(frontIcon);
        }
    }

    private void initEndIcon(){
        if(closeable){
            endIcon=new ImageView(getContext());
            LayoutParams params=new LayoutParams(dimens(R.dimen.chip_end_icon_size), dimens(R.dimen.chip_end_icon_size));
            params.addRule(RIGHT_OF, R.id.chip_text);
            params.addRule(CENTER_VERTICAL);
            params.setMargins(dimens(R.dimen.chip_close_margin),0, dimens(R.dimen.chip_close_margin),0);
            endIcon.setLayoutParams(params);
            endIcon.setId(R.id.chip_end_icon);
            endIcon.setImageDrawable(endIconDrawable);
            if(endIconColor!=-1){
                DrawableCompat.setTint(endIconDrawable,endIconColor);
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
        PaintDrawable bgDrawable = new PaintDrawable(backgroundColor);
        bgDrawable.setCornerRadius(dimens(R.dimen.chip_height)/2);
        setBackgroundDrawable(bgDrawable);
    }

    public void setChipText(String chipText) {
        this.text=chipText;
        chipTextView.setText(chipText);
    }

    public String getChipText() {
        return text;
    }
}
