package com.vungtv.film.feature.loading;

import android.content.Intent;

import com.vungtv.film.BasePresenter;
import com.vungtv.film.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface LoadingContract {

    interface View extends BaseView<Presenter> {

        void setVersionName(String version);

        void openActHome(int movID);

        void resumeActHome(int movID);
    }

    interface Presenter extends BasePresenter {

        void getIntent(Intent intent);

        void checkAccountInfo();

        void getAppConfig();
    }
}
