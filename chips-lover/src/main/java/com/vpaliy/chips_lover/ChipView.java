package com.vpaliy.chips_lover;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Build;
import android.support.annotation.DimenRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
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
    private boolean isPressAnimation;
    private int textStyle;
    private int backgroundColor;
    private int textColor;
    private int selectedBackgroundColor;
    private int selectedTextColor;
    private int selectedEndColor;
    private int selectedFrontColor;
    private boolean isSelected;

    private OnChipChangeListener chipChangeListener;
    private OnFrontIconEventClick frontIconClickEvent;
    private OnEndIconEventClick endIconEventClick;

    public ChipView(Context context) {
        this(context, null, 0);
    }

    public ChipView(Context context, ChipBuilder builder){
        super(context);
        init(builder);
    }

    public ChipView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(attrs!=null){
            TypedArray array=getContext().obtainStyledAttributes(attrs,R.styleable.ChipView);
            init(ChipBuilder.create(getContext(),array));
            array.recycle();
        }else {
            init(ChipBuilder.create(getContext()));
        }
    }

    private void init(ChipBuilder builder){
        text=builder.text;
        endIconDrawable=builder.endIconDrawable;
        frontIconDrawable=builder.frontIconDrawable;
        frontIconColor=builder.frontIconColor;
        endIconColor=builder.endIconColor;
        selectable=builder.selectable;
        closeable=builder.closeable;
        isPressAnimation =builder.isDefaultAnimation;
        textStyle=builder.textStyle;
        backgroundColor=builder.backgroundColor;
        textColor=builder.textColor;
        selectedBackgroundColor=builder.selectedBackgroundColor;
        selectedTextColor=builder.selectedTextColor;
        selectedEndColor=builder.selectedEndColor;
        selectedFrontColor=builder.selectedFrontColor;
        setUp();
    }

    private int dimens(@DimenRes int dimen){
        return (int)getResources().getDimension(dimen);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getLayoutParams().height=dimens(R.dimen.chip_height);
        getLayoutParams().width=WRAP_CONTENT;
    }

    private void setUp(){
        initBackgroundColor();
        initTextView();
        initEndIcon();
        initFrontIcon();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectable){
                    isSelected=!isSelected;
                    initBackgroundColor();
                    setFrontIcon();
                    setEndIcon();
                    setTextColor();
                    if(isPressAnimation) {
                        ViewCompat.animate(v)
                                .scaleX(isSelected ? 1.05f : 1f)
                                .scaleY(isSelected ? 1.05f : 1f)
                                .setDuration(getResources().getInteger(R.integer.default_anim_duration))
                                .start();
                    }
                }
            }
        });
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

    private Drawable makeCopyIfPossible(Drawable drawable){
        if(drawable.getConstantState()!=null){
            return drawable.getConstantState().newDrawable().mutate();
        }
        return drawable;
    }
    private void initFrontIcon(){
        if(frontIconDrawable!=null) {
            frontIconDrawable=makeCopyIfPossible(frontIconDrawable);
            frontIcon = new CircleImageView(getContext());
            LayoutParams params = new LayoutParams(dimens(R.dimen.chip_front_icon_size), dimens(R.dimen.chip_front_icon_size));
            params.addRule(ALIGN_PARENT_LEFT);
            params.addRule(CENTER_VERTICAL);
            frontIcon.setLayoutParams(params);
            frontIcon.setId(R.id.chip_front_icon);
            setFrontIcon();
            frontIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(frontIconClickEvent!=null){
                        frontIconClickEvent.onClick(v);
                    }
                }
            });
            addView(frontIcon);
        }
    }

    public void setChipChangeListener(OnChipChangeListener chipChangeListener) {
        this.chipChangeListener = chipChangeListener;
    }

    private void initEndIcon(){
        if(closeable||endIconDrawable!=null){
            endIconDrawable=makeCopyIfPossible(endIconDrawable);
            endIcon=new CircleImageView(getContext());
            LayoutParams params=new LayoutParams(dimens(R.dimen.chip_end_icon_size), dimens(R.dimen.chip_end_icon_size));
            params.addRule(RIGHT_OF, R.id.chip_text);
            params.addRule(CENTER_VERTICAL);
            params.setMargins(dimens(R.dimen.chip_close_margin),0, dimens(R.dimen.chip_close_margin),0);
            endIcon.setLayoutParams(params);
            endIcon.setId(R.id.chip_end_icon);
            setEndIcon();
            endIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(endIconEventClick!=null){
                        endIconEventClick.onClick(v);
                    }

                    if(closeable){
                        chipChangeListener.onRemove(ChipView.this);
                    }
                }
            });
            addView(endIcon);
        }
    }

    private void setTextColor(){
        if(chipTextView!=null){
            chipTextView.setTextColor(isSelected?selectedTextColor:textColor);
        }
    }

    private void setFrontIcon(){
        if(frontIcon!=null){
            if(isSelected){
                if(selectedFrontColor!=-1) {
                    DrawableCompat.setTint(frontIconDrawable, selectedFrontColor);
                }
            }else if(frontIconColor!=-1){
                DrawableCompat.setTint(frontIconDrawable,frontIconColor);
            }
            frontIcon.setImageDrawable(frontIconDrawable);
        }
    }

    private void setEndIcon(){
        if(endIcon!=null){
            if(isSelected) {
                DrawableCompat.setTint(endIconDrawable, selectedEndColor);
            }else if(endIconColor!=-1){
                DrawableCompat.setTint(endIconDrawable,endIconColor);
            }
            endIcon.setImageDrawable(endIconDrawable);
        }
    }

    public void setFrontIconClickEvent(OnFrontIconEventClick frontIconClickEvent) {
        this.frontIconClickEvent = frontIconClickEvent;
    }

    public void setEndIconEventClick(OnEndIconEventClick endIconEventClick) {
        this.endIconEventClick = endIconEventClick;
    }

    private void initBackgroundColor() {
        PaintDrawable bgDrawable = new PaintDrawable(isSelected?selectedBackgroundColor:backgroundColor);
        bgDrawable.setCornerRadius(dimens(R.dimen.chip_height)/2);
        setBackgroundDrawable(bgDrawable);
    }

    public void setChipText(String chipText) {
        this.text=chipText;
        chipTextView.setText(chipText);
    }

    interface OnChipChangeListener{
        void onScaleChanged(ChipView chipView);
        void onRemove(ChipView chipView);
    }

    public String getChipText() {
        return text;
    }
}
