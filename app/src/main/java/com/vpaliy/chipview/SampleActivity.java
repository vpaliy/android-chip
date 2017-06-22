package com.vpaliy.chipview;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.vpaliy.chips_lover.ChipsLayout;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SampleActivity extends AppCompatActivity {

    @BindView(R.id.chips)
    protected ChipsLayout chipsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        ButterKnife.bind(this);
        chipsLayout.setTags(Arrays.asList("Ch.Dickson","E.Farmer","M.Phil","J. White","L. Frazier","Iliana Ho",
                "Hugo Horne","Cesar Quinn","Seth Pugh","Valentina Green","Ayla Carney","Kyleigh Steele"));
    }
}
