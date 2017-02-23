package com.vungtv.film.feature.recharge;

import android.content.Context;

import com.vungtv.film.R;
import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.data.source.remote.service.BillingServices;
import com.vungtv.film.model.CardType;
import com.vungtv.film.model.CardValue;
import com.vungtv.film.model.PackageVip;
import com.vungtv.film.model.User;
import com.vungtv.film.util.StringUtils;

import java.util.ArrayList;

/**
 *
 * Created by pc on 2/23/2017.
 */

public class RechargePresenter implements RechargeContract.Presenter {

    private static final String TAG = RechargePresenter.class.getSimpleName();

    private final Context context;

    private final RechargeContract.View activityView;

    private final BillingServices billingServices;

    private String accessToken;

    public RechargePresenter(Context context, RechargeContract.View activityView, BillingServices billingServices) {
        this.context = context;
        this.activityView = activityView;
        this.billingServices = billingServices;

        this.activityView.setPresenter(this);

        accessToken = UserSessionManager.getAccessToken(context);

        setBillingServicesResult();
    }

    @Override
    public void rechargeSubmit(String[] cardInfo) {

        if (StringUtils.isEmpty(cardInfo[0])) {

            activityView.showMsgError(context.getResources().getString(R.string.rechange_error_msg_card_type));

        } else if (StringUtils.isEmpty(cardInfo[1]) || StringUtils.isEmpty(cardInfo[2])) {

            activityView.showMsgError(context.getResources().getString(R.string.rechange_error_msg_empty));

        } else {
            activityView.showLoading(true);
            billingServices.recharge(accessToken, cardInfo);
        }
    }

    @Override
    public void start() {
        activityView.showLoading(true);
        billingServices.loadRechargeInfo(accessToken);
    }

    @Override
    public void onDestroy() {
        billingServices.cancel();
    }

    private void setBillingServicesResult() {

        billingServices.setOnBillingListener(new BillingServices.OnBillingListener() {
            @Override
            public void onLoadPackagesVipSuccess(int vungPoint, ArrayList<PackageVip> list) {

            }

            @Override
            public void onBuyVipSuccess(String msg, User user, String token) {

            }

            @Override
            public void onLoadRechargeInfo(ArrayList<CardType> cardTypes, ArrayList<CardValue> cardValues) {
                activityView.showLoading(false);
                activityView.addViewCardType(cardTypes);
                activityView.addViewCardValue(cardValues);
            }

            @Override
            public void onRechargeSuccess(String msg, User user, String token) {

                activityView.showLoading(false);
                activityView.showMsgRechargeSuccess(msg);
                activityView.clearEdittexts();

                // Update user info and access token
                UserSessionManager.updateUserSession(context, user);
                if (!StringUtils.isEmpty(token)) {
                    UserSessionManager.setAccessToken(context, token);
                }
            }

            @Override
            public void onFailure(int code, String error) {
                activityView.showLoading(false);
                activityView.showMsgError(error);

                if (code == -999) {
                    // Đăng xuất khi lỗi: tài khoản được đăng nhập ở thiết bị khác.
                    activityView.logOutAccount();
                }
            }
        });
    }
}
