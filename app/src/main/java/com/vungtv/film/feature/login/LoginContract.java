package com.vungtv.film.feature.login;

import android.content.Intent;

import com.vungtv.film.BasePresenter;
import com.vungtv.film.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface LoginContract {

    interface View extends BaseView<Presenter> {
        void showLoading(boolean show);

        void showMsgError(String error);
    }

    interface Presenter extends BasePresenter {

        void loginWithEmail(String email, String pass);

        void loginWithFacebook();

        void loginWithGoogle();

        void setActivityResult(int requestCode, int resultCode, Intent data);

        void registerAcc(String email, String pass, String rePass);

        void fogetPass();

        void openFragLogin(LoginContract.View view);

        void openFragRegister(LoginContract.View view);
    }
}
