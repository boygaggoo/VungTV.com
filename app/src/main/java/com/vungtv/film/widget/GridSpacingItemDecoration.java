package com.vungtv.film.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private final int spacing;

    public GridSpacingItemDecoration(int spacingPX) {
        this.spacing = spacingPX;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.left = spacing;
        outRect.right = spacing;
        if (spacing > 0) {
            outRect.top = spacing * 2;
            outRect.bottom = spacing * 2;
        } else {
            outRect.top = 0;
            outRect.bottom = 0;
        }
    }
}
