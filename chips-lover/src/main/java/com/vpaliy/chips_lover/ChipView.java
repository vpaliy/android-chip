package com.vpaliy.chips_lover;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Build;
import android.support.annotation.DimenRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ChipView extends RelativeLayout{

    private static final float SCALE_BY=1.03F;

    private TextView chipTextView;
    private CircleImageView frontIcon;
    private ImageView endIcon;
    private String text;
    private Drawable endIconDrawable;
    private Drawable frontIconDrawable;
    private boolean selectable;
    private boolean closeable;
    private boolean isPressAnimation;
    private boolean isSelected;
    private int frontIconColor;
    private int endIconColor;
    private int textStyle;
    private int elevation;
    private int backgroundColor;
    private int textColor;
    private int selectedBackgroundColor;
    private int selectedTextColor;
    private int selectedEndColor;
    private int selectedFrontColor;

    private OnChipChangeListener chipChangeListener;
    private OnFrontIconEventClick frontIconClickEvent;
    private OnEndIconEventClick endIconEventClick;
    private OnClickListener selfClickListener;
    private OnClickListener externalClickListener;

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

    void init(ChipBuilder builder){
        text=builder.text;
        endIconDrawable=builder.endIconDrawable;
        frontIconDrawable=builder.frontIconDrawable;
        elevation=builder.elevation;
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
        if(elevation>0){
            ViewCompat.setElevation(this,elevation);
        }
    }

    private void setUp(){
        initBackgroundColor();
        initTextView();
        initEndIcon();
        initFrontIcon();
        selfClickListener=new OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerSelection();
            }
        };
        setOnClickListener(selfClickListener);
    }

    private void triggerSelection(){
        if(selectable){
            isSelected=!isSelected;
            initBackgroundColor();
            setFrontIcon();
            setEndIcon();
            setTextColor();
            if(isPressAnimation) {
                ViewCompat.animate(this)
                        .scaleX(isSelected?SCALE_BY: 1)
                        .scaleY(isSelected?SCALE_BY: 1)
                        .setDuration(getResources().getInteger(R.integer.default_anim_duration))
                        .setListener(new ViewPropertyAnimatorListenerAdapter(){
                            @Override
                            public void onAnimationEnd(View view) {
                                super.onAnimationEnd(view);
                                if(chipChangeListener!=null){
                                    chipChangeListener.onScaleChanged(ChipView.this);
                                }
                            }
                        })
                        .start();
            }
        }
        if(externalClickListener!=null){
            externalClickListener.onClick(this);
        }
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

    public void setSelectedBackgroundColor(int selectedBackgroundColor) {
        this.selectedBackgroundColor = selectedBackgroundColor;
        if(isSelected){
            initBackgroundColor();
        }
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

    @Override
    public void setOnClickListener(@Nullable OnClickListener listener) {
        if(listener==selfClickListener) {
            super.setOnClickListener(listener);
        }else{
            this.externalClickListener=listener;
        }
    }

    public void select(){
        triggerSelection();
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

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        initBackgroundColor();
    }

    public void setSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
        if(isSelected){
            setTextColor();
        }
    }

    public void setSelectedEndColor(int selectedEndColor) {
        this.selectedEndColor = selectedEndColor;
        if(isSelected){
            setEndIconColor(selectedEndColor);
        }
    }

    public void setText(String text) {
        this.text = text;
        chipTextView.setText(text);
    }

    public void setCloseable(boolean closeable) {
        this.closeable = closeable;
        if(closeable){
            endIconColor=ContextCompat.getColor(getContext(),R.color.colorChipCloseInactive);
            endIconDrawable=ContextCompat.getDrawable(getContext(),R.drawable.ic_close);
            setEndIconColor(endIconColor);
        }
    }

    public void setEndIconDrawable(Drawable endIconDrawable) {
        this.endIconDrawable = endIconDrawable;
        if(endIconDrawable!=null){
            if(endIcon!=null){
                endIcon.setImageDrawable(endIconDrawable);
            }else{
                initEndIcon();
            }
        }
    }

    public void setFrontIconColor(int frontIconColor) {
        this.frontIconColor = frontIconColor;
        setFrontIcon();
    }

    public void setFrontIconDrawable(Drawable frontIconDrawable) {
        this.frontIconDrawable = frontIconDrawable;
        if(frontIconDrawable!=null){
            if(frontIcon!=null){
                frontIcon.setImageDrawable(frontIconDrawable);
            }else{
                initFrontIcon();
            }
        }
    }

    public void setPressAnimation(boolean pressAnimation) {
        isPressAnimation = pressAnimation;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public void setEndIconColor(int endIconColor) {
        this.endIconColor = endIconColor;
        setEndIcon();
    }

    public void setSelectedFrontColor(int selectedFrontColor) {
        this.selectedFrontColor = selectedFrontColor;
        setFrontIcon();
    }

    public void setTextStyle(int textStyle) {
        this.textStyle = textStyle;
        if(chipTextView!=null){
            if(Build.VERSION.SDK_INT>=23) {
                chipTextView.setTextAppearance(textStyle);
            }
        }
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        setTextColor();
    }

    public void setChipText(String chipText) {
        this.text=chipText;
        chipTextView.setText(chipText);
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getSelectedEndColor() {
        return selectedEndColor;
    }

    public int getEndIconColor() {
        return endIconColor;
    }

    public int getFrontIconColor() {
        return frontIconColor;
    }

    public int getSelectedBackgroundColor() {
        return selectedBackgroundColor;
    }

    public int getSelectedFrontColor() {
        return selectedFrontColor;
    }

    public int getSelectedTextColor() {
        return selectedTextColor;
    }


    interface OnChipChangeListener{
        void onScaleChanged(ChipView chipView);
        void onRemove(ChipView chipView);
    }

    public String getChipText() {
        return text;
    }
}
