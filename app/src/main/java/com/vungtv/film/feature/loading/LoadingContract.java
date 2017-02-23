package com.vungtv.film.feature.loading;

import com.vungtv.film.BasePresenter;
import com.vungtv.film.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface LoadingContract {

    interface View extends BaseView<Presenter> {

        void setVersionName(String version);

        void startHomePage();
    }

    interface Presenter extends BasePresenter {

        void checkAccountInfo();

        void logoutAccount();

        void refreshAccessToken(String token);
    }
}
