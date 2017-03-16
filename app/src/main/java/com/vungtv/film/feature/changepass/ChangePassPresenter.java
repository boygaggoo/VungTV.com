package com.vungtv.film.feature.changepass;

import android.content.Context;

import com.vungtv.film.R;
import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.data.source.remote.service.AccountServices;
import com.vungtv.film.util.StringUtils;

/**
 *
 * Created by Mr Cuong on 2/18/2017.
 */

public class ChangePassPresenter implements ChangePassContract.Presenter {

    private final Context context;

    private final AccountServices accountServices;

    private final ChangePassContract.View activityView;

    public ChangePassPresenter(Context context, AccountServices accountServices, ChangePassContract.View activityView) {
        this.context = context;
        this.accountServices = accountServices;
        this.activityView = activityView;
        this.activityView.setPresenter(this);

        setAccountServicesResult();
    }

    @Override
    public boolean validateInput(String[] passwords) {

        if (StringUtils.isEmpty(passwords[0]) || StringUtils.isEmpty(passwords[1]) || StringUtils.isEmpty(passwords[2])) {
            activityView.showMsgError(context.getString(R.string.user_change_pass_error_msg_input));
            return false;
        }

        if (passwords[1].length() < 6 || passwords[2].length() < 6) {
            activityView.showMsgError(context.getString(R.string.user_change_pass_error_msg_pass_short));
            return false;
        }

        if (!passwords[1].equals(passwords[2])) {
            activityView.showMsgError(context.getString(R.string.user_change_pass_error_msg_repass));
            return false;
        }

        return true;
    }

    @Override
    public void changePass(String[] passwords) {
        if (!validateInput(passwords)) return;

        activityView.showLoading(true);
        accountServices.changePassword(UserSessionManager.getAccessToken(context), passwords);
    }

    @Override
    public void start() {
        activityView.updateUserInfoView(UserSessionManager.getCurrentUser(context));
    }

    @Override
    public void onDestroy() {
        accountServices.cancel();
    }

    private void setAccountServicesResult() {
        accountServices.setOnAccountChangeResultListener(new AccountServices.OnAccountChangeResultListener() {
            @Override
            public void onAccountChangeSuccess(String token) {
                if (StringUtils.isNotEmpty(token)) {
                    UserSessionManager.setAccessToken(context, token);
                }
                activityView.showLoading(false);
                activityView.changePassSuccess();
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
