package com.vungtv.film.feature.home;


import android.content.Context;

import com.vungtv.film.R;
import com.vungtv.film.data.source.local.DbRecent;
import com.vungtv.film.data.source.remote.model.ApiHome;
import com.vungtv.film.data.source.remote.service.HomeServices;
import com.vungtv.film.model.Slider;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.widget.moviesrowview.MoviesRowAdapter;
import com.vungtv.film.widget.moviesrowview.VtvMoviesRowView;

import java.util.ArrayList;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link HomeFragment}), retrieves the data and updates the
 * UI as required.
 */
public class HomePresenter implements HomeContract.Presenter {
    private static final String TAG = HomePresenter.class.getSimpleName();

    private Context context;

    private final HomeContract.View homeView;

    private final HomeServices homeServices;

    private ApiHome.DataHome dataHome;

    public HomePresenter(Context context, HomeContract.View homeView, HomeServices homeServices) {
        this.context = context;
        this.homeView = checkNotNull(homeView);
        this.homeServices = checkNotNull(homeServices);

        this.homeView.setPresenter(this);
        homeServicesResponse();
    }

    @Override
    public void start() {
        //start load home data;
        loadData();
    }

    @Override
    public void onDestroy() {
        homeServices.cancel();
    }

    @Override
    public void loadData() {
        // Load home data from remote server;
        homeServices.cancel();
        homeServices.loadHomeData();
        homeView.showLoading(true);
    }

    @Override
    public void reloadData() {
        // Reload home data when prev load failed;
        homeView.removeAllViews();
        loadData();
    }

    @Override
    public void configChange() {
        // Call when orientation change;
        homeView.removeAllViews();
        addViews();
    }

    @Override
    public void openActMovieDetails(int movieId) {
        homeView.openActMovieDetails(movieId);
    }

    @Override
    public void openActFilterMovies(String url) {
        homeView.openActFilterMovies(checkNotNull(url));
    }

    private void homeServicesResponse() {
        homeServices.setHomeResultCallback(new HomeServices.HomeResultCallback() {
            @Override
            public void onSuccess(ApiHome.DataHome data) {
                homeView.showLoading(false);
                dataHome = data;
                addViews();
            }

            @Override
            public void onFailure(int code, final String error) {
                homeView.showLoading(false);
                homeView.showMsgError(true, error);
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
        homeView.addSliderView(listSlider);

        //add film de cu, top, han, trung, anime, tivishow, 18+;
        ArrayList<ApiHome.ModuleRow> listDataRow = dataHome.getModuleRows();
        if (listDataRow != null && listDataRow.size() > 0) {
            for (int i = 0; i < listDataRow.size(); i++) {
                ApiHome.ModuleRow moduleRow = listDataRow.get(i);
                int itemType = MoviesRowAdapter.ITEM_DEFAULT;
                int rowStyle = VtvMoviesRowView.STYLE_DEFAULT;
                int iconTitle = 0;
                if (i == 0) {
                    // Phim de cu
                    itemType = MoviesRowAdapter.ITEM_RECOMMENT;
                    rowStyle = VtvMoviesRowView.STYLE_BLACK;
                    iconTitle = R.drawable.icon_star1;
                } else if (i == 1 || i == 2) {
                    // phim top vung
                    itemType = MoviesRowAdapter.ITEM_RECOMMENT;
                    rowStyle = VtvMoviesRowView.STYLE_GRAY;
                    iconTitle = R.drawable.icon_lavung;
                } else if (i == listDataRow.size() -1) {
                    // phim sap chieu
                    itemType = MoviesRowAdapter.ITEM_COMMING;
                    rowStyle = VtvMoviesRowView.STYLE_BLACK_NO_FILMNAME;
                    iconTitle = R.drawable.icon_star1;
                }

                homeView.addMoviesView(
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
                homeView.addHotTopicView(
                        VtvMoviesRowView.STYLE_BLACK_NO_TITLE,
                        MoviesRowAdapter.ITEM_HOT_TOPIC,
                        null,
                        new ArrayList<Object>(dataHome.getHotTopic())
                );
            }

            // add recent movies;
            homeView.addReCentView(
                    VtvMoviesRowView.STYLE_DEFAULT,
                    MoviesRowAdapter.ITEM_RECENT,
                    R.drawable.icon_lavung,
                    context.getResources().getString(R.string.home_text_xem_gan_day),
                    new ArrayList<Object>(new DbRecent().getLisRecent())
            );

            // add footer view
            homeView.addFooterView();
        }
    }
}
