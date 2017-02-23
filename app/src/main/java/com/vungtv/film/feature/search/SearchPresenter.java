package com.vungtv.film.feature.search;

import android.content.Context;

import com.vungtv.film.R;
import com.vungtv.film.util.DensityUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 *
 * Created by pc on 2/8/2017.
 */

public class SearchPresenter implements SearchContract.Presenter {

    private final Context context;

    private final SearchContract.View searchView;

    private int columNumber = 3;

    public SearchPresenter(Context context, SearchContract.View searchView) {
        this.context = checkNotNull(context);
        this.searchView = checkNotNull(searchView);
        this.searchView.setPresenter(this);
    }

    @Override
    public void start() {
        float itemWidth = DensityUtils.getWidthInPx(context);
        int itemSpace = context.getResources().getDimensionPixelSize(R.dimen.space_3);
        if (context.getResources().getBoolean(R.bool.isTabletLand)) {
            columNumber = 6;
        }

        itemWidth = itemWidth - itemSpace*(columNumber + 1);
        itemWidth = itemWidth / columNumber;
        searchView.setRecyclerView(columNumber, itemWidth, itemSpace);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void loadData(int searchType, String query) {

    }

    @Override
    public void clearSearchView() {

    }

    @Override
    public void changeSearchType(int searchType) {

    }

    @Override
    public void openActMovieDetail(int movieId) {

    }


}
