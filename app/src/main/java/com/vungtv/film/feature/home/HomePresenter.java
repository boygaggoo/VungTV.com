package com.vungtv.film.feature.home;


import android.content.Context;

import com.vungtv.film.R;
import com.vungtv.film.data.source.local.MovieRecentManager;
import com.vungtv.film.data.source.remote.model.ApiHome;
import com.vungtv.film.data.source.remote.service.HomeServices;
import com.vungtv.film.model.MovieRecent;
import com.vungtv.film.model.Slider;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.widget.moviesrowview.MoviesRowAdapter;
import com.vungtv.film.widget.moviesrowview.VtvMovieRowView;

import java.util.ArrayList;
import java.util.List;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link HomeFragment}), retrieves the data and updates the
 * UI as required.
 */
public class HomePresenter implements HomeContract.Presenter {
    private static final String TAG = HomePresenter.class.getSimpleName();

    private Context context;

    private final HomeContract.View fragmentView;

    private final HomeServices homeServices;

    private final MovieRecentManager movieRecentManager;

    private ApiHome.DataHome dataHome;

    private List<MovieRecent> listRecent;

    public HomePresenter(Context context, HomeContract.View fragmentView) {
        this.context = context;
        this.fragmentView = checkNotNull(fragmentView);

        this.fragmentView.setPresenter(this);

        homeServices = new HomeServices(context);
        homeServicesResponse();

        movieRecentManager = new MovieRecentManager();
    }

    @Override
    public void start() {
        //start load home data;
        loadData();
    }

    @Override
    public void onDestroy() {
        homeServices.cancel();
        movieRecentManager.close();
    }

    @Override
    public void loadData() {
        // Load home data from remote server;
        homeServices.cancel();
        homeServices.loadHomeData();
        fragmentView.showLoading(true);
    }

    @Override
    public void reloadData() {
        // Reload home data when prev load failed;
        fragmentView.removeAllViews();
        loadData();
    }

    @Override
    public void reloadRecentMovies() {
        // add recent movies;
        listRecent = movieRecentManager.getAll(20);
        fragmentView.updateRecentView(new ArrayList<Object>(listRecent));
    }

    @Override
    public void configChange() {
        // Call when orientation change;
        fragmentView.removeAllViews();
        addViews();
    }

    @Override
    public void playRecentMovies(int pos) {
        fragmentView.openActPlayer(listRecent.get(pos));
    }

    private void homeServicesResponse() {
        homeServices.setHomeResultCallback(new HomeServices.HomeResultCallback() {
            @Override
            public void onSuccess(ApiHome.DataHome data) {
                fragmentView.showLoading(false);
                dataHome = data;
                addViews();
            }

            @Override
            public void onFailure(int code, final String error) {
                fragmentView.showLoading(false);
                fragmentView.showMsgError(true, error);
            }
        });
    }

    /**
     * Add views to layout {@link HomeFragment}
     */
    private void addViews() {
        if (dataHome == null) {
            LogUtils.d(TAG, "addViews: dataHome = NULL");
            return;
        }

        // Add slider;
        ArrayList<Slider> listSlider = dataHome.getSliders();
        if (context.getResources().getBoolean(R.bool.isTabletLand)) {
            listSlider.add(null);
        }
        fragmentView.addSliderView(listSlider);

        //add film de cu, top, han, trung, anime, tivishow, 18+;
        ArrayList<ApiHome.ModuleRow> listDataRow = dataHome.getModuleRows();
        if (listDataRow != null && listDataRow.size() > 0) {

            for (int i = 0; i < listDataRow.size(); i++) {

                ApiHome.ModuleRow moduleRow = listDataRow.get(i);

                int itemType = MoviesRowAdapter.ITEM_DEFAULT;

                int rowStyle = VtvMovieRowView.STYLE_DEFAULT;

                int iconTitle = R.drawable.icon_filmroll;
                if (i == 0) {
                    // Phim de cu
                    itemType = MoviesRowAdapter.ITEM_RECOMMENT;
                    rowStyle = VtvMovieRowView.STYLE_BLACK;
                    iconTitle = R.drawable.icon_star1;
                } else if (i == 1 || i == 2) {
                    // phim top vung
                    itemType = MoviesRowAdapter.ITEM_RECOMMENT;
                    rowStyle = VtvMovieRowView.STYLE_GRAY;
                    iconTitle = R.drawable.icon_lavung;
                } else if (i == listDataRow.size() -1) {
                    // phim sap chieu
                    itemType = MoviesRowAdapter.ITEM_COMMING;
                    rowStyle = VtvMovieRowView.STYLE_BLACK_NO_FILMNAME;
                    iconTitle = R.drawable.icon_star1;
                }

                fragmentView.addMoviesView(
                        rowStyle,
                        itemType,
                        iconTitle,
                        moduleRow.getTitle(),
                        moduleRow.getLink(),
                        new ArrayList<Object>(moduleRow.getMovies())
                );
            }

            // add row hot topic
            if (listDataRow.size() > 6) {
                fragmentView.addHotTopicView(
                        VtvMovieRowView.STYLE_BLACK_NO_TITLE,
                        MoviesRowAdapter.ITEM_HOT_TOPIC,
                        null,
                        new ArrayList<Object>(dataHome.getHotTopic())
                );
            }

            // add recent movies;
            listRecent = movieRecentManager.getAll(20);
            if (listRecent.size() > 0) {
                fragmentView.addReCentView(new ArrayList<Object>(listRecent));
            }

            // add footer view
            fragmentView.addFooterView();
        }
    }
}
