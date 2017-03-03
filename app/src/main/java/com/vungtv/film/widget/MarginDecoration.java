package com.vungtv.film.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 * Created by pc on 3/3/2017.
 */

public class MarginDecoration extends RecyclerView.ItemDecoration {
    private int margin;

    public MarginDecoration(Context context, @DimenRes int resDimen) {
        margin = context.getResources().getDimensionPixelSize(resDimen);
    }

    @Override
    public void getItemOffsets(
            Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(margin, margin, margin, margin);
    }
}
