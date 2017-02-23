package com.vungtv.film.feature.userinfo;

import android.content.Context;

import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.data.source.remote.service.AccountServices;
import com.vungtv.film.model.User;
import com.vungtv.film.util.StringUtils;
import com.vungtv.film.util.TimeUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 *
 * Created by Mr Cuong on 2/7/2017.
 */

public class UserInfoPresenter implements UserInfoContract.Presenter {
    private final Context context;

    private final UserInfoContract.View activityView;

    private final AccountServices accountServices;

    private String newDisplayName;

    public UserInfoPresenter(Context context, UserInfoContract.View userInfoView, AccountServices accountServices) {
        this.context = checkNotNull(context);
        this.activityView = checkNotNull(userInfoView);
        this.accountServices = checkNotNull(accountServices);

        this.activityView.setPresenter(this);
        setAccountServicesResult();
    }

    @Override
    public void start() {

        User user = UserSessionManager.getCurrentUser(context);
        activityView.setUserInfoView(user);

        if (user == null) return;

        activityView.setTextEmail(user.getUserEmail());

        if (!StringUtils.isEmpty(user.getUserDisplayName())) {
            activityView.setTextDisplayName(user.getUserDisplayName());
        } else {
            activityView.setTextDisplayName(user.getUserEmail());
        }

        if (!StringUtils.isEmpty(user.getUserCreatedAt())) {

            if (StringUtils.isNumeric(user.getUserCreatedAt())) {
                activityView.setTextCreatedDate(TimeUtils
                        .convertTimeStampToDate(Long.parseLong(user.getUserCreatedAt())));
            } else {
                activityView.setTextCreatedDate(user.getUserCreatedAt());
            }
        }
    }

    @Override
    public void onDestroy() {
        accountServices.cancel();
    }

    @Override
    public void showPopupChangeName() {
        activityView.showPopupChangeName();
    }

    @Override
    public void changeDisplayName(String newName) {
        newDisplayName = newName;
        accountServices.changeDisplayName(UserSessionManager.getAccessToken(context), newName);
    }

    @Override
    public void openActChangePass() {
        activityView.openActChangePass();
    }

    @Override
    public void updateUserInfo() {
        start();
    }

    private void setAccountServicesResult() {
        accountServices.setOnAccountChangeResultListener(new AccountServices.OnAccountChangeResultListener() {
            @Override
            public void onSuccess() {
                UserSessionManager.updateDisplayName(context, newDisplayName);
                activityView.showLoading(false);
                activityView.showMsgChangenameSuccess();
            }

            @Override
            public void onFailure(int code, String error) {
                activityView.showLoading(false);
                activityView.showMsgError(error);
                if (code == -999) {
                    activityView.logOutAccount();
                }
            }
        });
    }
}
