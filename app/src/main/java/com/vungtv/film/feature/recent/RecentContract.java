package com.vungtv.film.feature.recent;

import com.vungtv.film.BasePresenter;
import com.vungtv.film.BaseView;
import com.vungtv.film.model.MovieRecent;

import java.util.ArrayList;

/**
 *
 * Created by pc on 3/8/2017.
 */

public interface RecentContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean show);

        void showMsgError(boolean show, String error);

        void setListAdapter(ArrayList<MovieRecent> list);

        void openActMovieDetail(int movId);

        void openActPlayerRecent(MovieRecent movieRecent);

        void clearData();
    }

    interface Presenter extends BasePresenter {

        void reload();
    }
}
