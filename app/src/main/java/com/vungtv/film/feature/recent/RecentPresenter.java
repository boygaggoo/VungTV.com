package com.vungtv.film.feature.recent;

import android.content.Context;

import com.vungtv.film.R;
import com.vungtv.film.data.source.local.MovieRecentManager;
import com.vungtv.film.feature.player.PlayerPresenter;
import com.vungtv.film.model.MovieRecent;
import com.vungtv.film.util.LogUtils;

import java.util.ArrayList;

/**
 *
 * Created by pc on 3/8/2017.
 */

public class RecentPresenter implements RecentContract.Presenter {
    private static final String TAG = PlayerPresenter.class.getSimpleName();

    private final Context context;

    private final RecentContract.View activityView;

    private final MovieRecentManager movieRecentManager;

    public RecentPresenter(Context context, RecentContract.View activityView) {
        this.context = context;
        this.activityView = activityView;
        this.activityView.setPresenter(this);

        movieRecentManager = new MovieRecentManager();
    }


    @Override
    public void start() {
        ArrayList<MovieRecent> list = movieRecentManager.getAll();
        if (list.size() == 0) {
            activityView.showMsgError(true, context.getString(R.string.error_no_data));
        } else {
            activityView.setListAdapter(list);
        }
        activityView.showLoading(false);
    }

    @Override
    public void onDestroy() {
        movieRecentManager.close();
    }

    @Override
    public void reload() {
        LogUtils.d(TAG, "start()");
        activityView.showMsgError(false, null);
        start();
    }
}
