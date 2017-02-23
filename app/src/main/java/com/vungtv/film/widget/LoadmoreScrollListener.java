package com.vungtv.film.widget;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;


public abstract class LoadmoreScrollListener extends RecyclerView.OnScrollListener {
    private static final String TAG = LoadmoreScrollListener.class.getSimpleName();

    private final RecyclerView.LayoutManager layoutManager;

    private static final int TOLOAD = 1;

    public LoadmoreScrollListener(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        final int cur = recyclerView.getChildCount();

        if (layoutManager != null) {

            final int total = layoutManager.getItemCount();
            final int first;

            if (layoutManager instanceof StaggeredGridLayoutManager) {

                StaggeredGridLayoutManager gridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                int[] firstVisibleItems = null;
                firstVisibleItems = gridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);

                if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                    first = firstVisibleItems[0];
                } else {
                    first = 0;
                }

            } else if (layoutManager instanceof GridLayoutManager) {

                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                first = gridLayoutManager.findFirstVisibleItemPosition();
            } else {

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                first = linearLayoutManager.findFirstVisibleItemPosition();
            }

            if (first + cur + TOLOAD >= total) {
                //LogUtils.i(TAG, "first = " + first + " | cur = " + cur + " | total = " + total);
                onLoadmore();
            }
        }
    }

    public abstract void onLoadmore();
}
