package com.vungtv.film.feature.sendrequest;

import com.vungtv.film.BasePresenter;
import com.vungtv.film.BaseView;

/**
 *
 * Created by pc on 3/10/2017.
 */

public interface RequestContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean show);

        void showMsgError(String msg);

        void resetEdittext();
    }

    interface Presenter extends BasePresenter {

        void sendRequest(String title, String content);
    }
}
