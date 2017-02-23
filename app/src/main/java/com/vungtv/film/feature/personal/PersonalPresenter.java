package com.vungtv.film.feature.personal;

import android.content.Context;

import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.model.User;
import com.vungtv.film.util.LoginGoogleUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link PersonalActivity}), retrieves the data and updates the
 * UI as required.
 */

public class PersonalPresenter implements PersonalContract.Presenter {

    private final Context context;

    private final PersonalContract.View userPageView;

    private final LoginGoogleUtils loginGoogleUtils;

    public PersonalPresenter(Context context, PersonalContract.View userPageView, LoginGoogleUtils loginGoogleUtils) {
        this.context = checkNotNull(context);
        this.userPageView = checkNotNull(userPageView);
        this.loginGoogleUtils = loginGoogleUtils;
        this.userPageView.setPresenter(this);
    }

    @Override
    public void loginStatusChange() {

        User user = UserSessionManager.getCurrentUser(context);

        userPageView.updateUserInfoView(user);

        userPageView.updateRecylerViewItems(user != null);

        userPageView.setVisibleBtnGiftCodeAndVip(user != null);

        if (user != null) {
            userPageView.setNumberMoviesFollow(0);
        }
    }

    @Override
    public void logOut() {
        UserSessionManager.logout(context, loginGoogleUtils);
    }

    @Override
    public void openActLogin() {
        userPageView.openActLogin();
    }

    @Override
    public void openActNapVip() {
        userPageView.openActNapVip();
    }

    @Override
    public void openActGiftcode() {
        userPageView.openActGiftcode();
    }

    @Override
    public void start() {
        loginStatusChange();
    }

    @Override
    public void onDestroy() {
        loginGoogleUtils.disconect();
    }
}
