package com.vungtv.film.feature.menumovies;

import android.content.Context;
import android.content.Intent;

import com.vungtv.film.R;
import com.vungtv.film.data.source.remote.model.ApiMenuMovies;
import com.vungtv.film.data.source.remote.service.MenuMoviesServices;

import static com.vungtv.film.feature.menumovies.MenuMoviesActivity.INTENT_DANHMUC;
import static com.vungtv.film.feature.menumovies.MenuMoviesActivity.INTENT_TITLE;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 2/24/2017.
 * Email: vancuong2941989@gmail.com
 */

public class MenuMoviesPresenter implements MenuMoviesContract.Presenter, MenuMoviesServices.MenuMoviesResultCallback {

    private final Context context;

    private final MenuMoviesContract.View activityView;

    private final MenuMoviesServices menuMoviesServices;

    private String pathDanhMuc;

    /**
     * Contructor
     *
     * @param context context
     * @param activityView @link {MenuMoviesContract.View}
     */
    public MenuMoviesPresenter(Context context, MenuMoviesContract.View activityView) {
        this.context = context;
        this.activityView = activityView;

        this.activityView.setPresenter(this);

        menuMoviesServices = new MenuMoviesServices(context);
        menuMoviesServices.setMenuMoviesResultCallback(this);

    }

    @Override
    public void loadContent() {
        if (pathDanhMuc == null) {
            activityView.showMsgError(true, context.getResources().getString(R.string.error_no_data));
            return;
        }

        activityView.showLoading(true);
        activityView.showMsgError(false, null);
        menuMoviesServices.cancel();
        menuMoviesServices.loadMovies(pathDanhMuc);
    }

    @Override
    public void reLoadContent() {
        activityView.removeAllViews();
        loadContent();
    }

    @Override
    public void getIntent(Intent intent) {
        if (intent == null) return;
        pathDanhMuc = intent.getStringExtra(INTENT_DANHMUC);
        activityView.setToolbarTitle(intent.getStringExtra(INTENT_TITLE));
    }

    @Override
    public void start() {

    }

    @Override
    public void onDestroy() {
        menuMoviesServices.cancel();
    }

    @Override
    public void onFailure(int code, String error) {
        activityView.showLoading(false);
        activityView.showMsgError(true, error);
    }

    @Override
    public void onMenuMoviesResultSuccess(ApiMenuMovies.Data data) {
        activityView.showLoading(false);

        if (data.items == null || data.items.size() == 0) {
            activityView.showMsgError(true, context.getResources().getString(R.string.error_no_data));
            return;
        }

        for (int i = 0; i < data.items.size(); i++) {
            activityView.addRowMoviesView(i,
                    data.items.get(i).title,
                    data.items.get(i).movies,
                    data.items.get(i).link);
        }

        // Add Ad view
        for (int i = 0; i < data.items.size(); i++) {
            if(i == 0) {
                activityView.addAdView(0);
            } else if (i%4 == 0) {
                activityView.addAdView(i);
            }
        }

        activityView.addFooterView();
    }
}
