package com.vungtv.film.feature.menumovies;

import android.content.Context;
import android.content.Intent;

import com.vungtv.film.data.source.remote.ApiQuery;
import com.vungtv.film.data.source.remote.service.FilterMoviesServices;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 2/24/2017.
 * Email: vancuong2941989@gmail.com
 */

public class MenuMoviesPresenter implements MenuMoviesContract.Presenter {

    private final Context context;

    private final MenuMoviesContract.View activityView;

    private final FilterMoviesServices filterMoviesServices;


    public MenuMoviesPresenter(Context context, MenuMoviesContract.View activityView) {
        this.context = context;
        this.activityView = activityView;

        this.activityView.setPresenter(this);

        filterMoviesServices = new FilterMoviesServices(context);

    }

    @Override
    public void loadContent() {

    }

    @Override
    public void reLoadContent() {

    }

    @Override
    public void openActFilterMovies() {

    }

    @Override
    public void openActSearch() {

    }

    @Override
    public void getIntent(Intent intent) {
        if (intent == null) return;

        String danhMuc = intent.getStringExtra(MenuMoviesActivity.INTENT_DANHMUC);

        if (danhMuc == null) return;

        switch (danhMuc) {
            case ApiQuery.P_PHIM_LE:

                break;
            case ApiQuery.P_PHIM_BO:

                break;
            case ApiQuery.P_ANIME:

                break;
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void onDestroy() {
        filterMoviesServices.cancel();
    }
}
