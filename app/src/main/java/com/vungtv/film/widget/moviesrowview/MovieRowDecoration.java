package com.vungtv.film.widget.moviesrowview;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vungtv.film.R;

/**
 *
 * Created by pc on 3/3/2017.
 */

public class MovieRowDecoration extends RecyclerView.ItemDecoration {
    private final int MARGIN, MARGIN_BETWEEN_ITEM;

    public MovieRowDecoration(Context context) {
        MARGIN = context.getResources().getDimensionPixelSize(R.dimen.margin);
        MARGIN_BETWEEN_ITEM = context.getResources().getDimensionPixelSize(R.dimen.space_7);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if(parent.getChildAdapterPosition(view) == 0){
            outRect.left = MARGIN;
            outRect.right = MARGIN_BETWEEN_ITEM;
        } else if(parent.getChildAdapterPosition(view) == state.getItemCount()-1){
            outRect.left = 0;
            outRect.right = MARGIN;
        } else {
            outRect.left = 0;
            outRect.right = MARGIN_BETWEEN_ITEM;
        }
    }
}
