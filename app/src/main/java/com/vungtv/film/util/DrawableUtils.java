package com.vungtv.film.util;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import com.vungtv.film.R;


public class DrawableUtils {

    public static Drawable createShapeWithColor(int color, int radius) {
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(color);
        shape.setCornerRadius(radius);

        return shape;
    }

    public static Drawable createBgMovieInfo(Context context, int color) {
        Drawable drawable = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getResources().getDrawable(R.drawable.ds_bg_white_radius, null);
        } else {
            drawable = context.getResources().getDrawable(R.drawable.ds_bg_white_radius);
        }

        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        return drawable;
    }
}
