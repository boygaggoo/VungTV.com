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

    private final PersonalContract.View activityView;

    private final LoginGoogleUtils loginGoogleUtils;

    public PersonalPresenter(Context context, PersonalContract.View activityView, LoginGoogleUtils loginGoogleUtils) {
        this.context = checkNotNull(context);
        this.activityView = checkNotNull(activityView);
        this.loginGoogleUtils = loginGoogleUtils;
        this.activityView.setPresenter(this);
    }

    @Override
    public void loginStatusChange() {

        User user = UserSessionManager.getCurrentUser(context);

        activityView.updateUserInfoView(user);

        activityView.updateRecylerViewItems(user != null);

        activityView.setVisibleBtnGiftCodeAndVip(user != null);
    }

    @Override
    public void openActChangepass() {
        if (UserSessionManager.isProviderEmail(context)) {
            activityView.openActChangepass();
        }
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
