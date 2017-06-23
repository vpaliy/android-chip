package com.vpaliy.chipview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.slider.LightnessSlider;
import com.vpaliy.chips_lover.ChipBuilder;
import com.vpaliy.chips_lover.ChipView;
import com.vpaliy.chips_lover.ChipsLayout;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BottomSheet extends BottomSheetDialogFragment {

    @BindView(R.id.chips_config)
    protected ChipsLayout chipsLayout;

    @BindView(R.id.lightness_bar)
    protected LightnessSlider lightnessSlider;

    @BindView(R.id.color_picker_view)
    protected ColorPickerView pickerView;

    private ChipBuilder builder;
    private int selection=-1;

    private OnUpdateChipsListener updateChipsListener;

    public BottomSheet setUpdateListener(OnUpdateChipsListener updateListener){
        this.updateChipsListener=updateListener;
        return this;
    }

    public static BottomSheet newInstance(){
        return new BottomSheet();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View root=inflater.inflate(R.layout.fragment_bottom_sheet,container,false);
        ButterKnife.bind(this,root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(view!=null){
            lightnessSlider.setColorPicker(pickerView);
            final List<ChipView> chips=chipsLayout.getChips();
            builder=ChipBuilder.create(getContext());
            pickerView.addOnColorChangedListener(new OnColorChangedListener() {
                @Override
                public void onColorChanged(int color) {
                    ChipView chipView=chips.get(selection);
                    chipView.setSelectedBackgroundColor(color);
                    chipView.setBackgroundColor(color);
                    switch (selection){
                        case 0:
                            builder.setBackgroundColor(color);
                            break;
                        case 1:
                            builder.setSelectedBackgroundColor(color);
                            break;
                        case 2:
                            builder.setTextColor(color);
                            break;
                        case 3:
                            builder.setSelectedTextColor(color);
                            break;
                        case 4:
                            builder.setFrontIconColor(color);
                            break;
                        case 5:
                            builder.setEndIconColor(color);
                            break;
                        case 6:
                            builder.setSelectedEndColor(color);
                            break;
                        case 7:
                            builder.setSelectedFrontColor(color);
                            break;
                    }
                }
            });
            selection=0;
            chips.get(selection).select();
            final int color= ContextCompat.getColor(getContext(),R.color.colorSelectedChipBackground);
            chipsLayout.setClickListenerToAll(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selection=chips.indexOf(view);
                    ChipView chipView=chips.get(selection);
                    if(chipView.getSelectedBackgroundColor()!=color) {
                        pickerView.setColor(chipView.getSelectedBackgroundColor(), false);
                    }else{
                        pickerView.setColor(Color.WHITE,false);
                    }
                    lightnessSlider.setColor(Color.WHITE);

                }
            });
        }
    }

    @OnClick(R.id.submit_chip)
    public void submit(){
        updateChipsListener.onUpdate(builder);
    }

    @Override
    public void onPause() {
        super.onPause();
        updateChipsListener.onUpdate(builder);
    }

    public interface OnUpdateChipsListener{
        void onUpdate(ChipBuilder builder);
    }

}
