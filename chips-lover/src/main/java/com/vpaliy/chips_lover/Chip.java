package com.vpaliy.chips_lover;

import android.graphics.drawable.Drawable;

class Chip {
    String text;
    Drawable endIcon;
    Drawable frontIcon;
    int frontIconColor;
    int endIconColor;
    int frontIconSize;
    int endIconSize;
    boolean selectable;
    boolean closeable;
    boolean isDefaultAnimation;   //if a chip is selectable, it will animate on click
    int textStyle;
    int backgroundColor;
    int textColor;
    //selected colors
    int selectedBackgroundColor;
    int selectedTextColor;
    int selectedEndColor;
    int selectedFrontColor;


}
