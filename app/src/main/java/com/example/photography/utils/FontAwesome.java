package com.example.photography.utils;

import android.content.Context;
import android.graphics.Typeface;

public class FontAwesome {
    public static final String ROOT = "fonts/",
            FONTAWESOME = ROOT + "fa-solid-900.ttf";
    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }
}
