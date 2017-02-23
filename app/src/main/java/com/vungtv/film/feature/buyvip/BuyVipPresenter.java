package com.vungtv.film.feature.buyvip;

import android.content.Context;

import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.data.source.remote.service.BillingServices;
import com.vungtv.film.model.CardType;
import com.vungtv.film.model.CardValue;
import com.vungtv.film.model.PackageVip;
import com.vungtv.film.model.User;

import java.util.ArrayList;

/**
 *
 * Created by pc on 2/21/2017.
 */

public class BuyVipPresenter implements BuyVipContract.Presenter {

    private final Context context;

    private final BuyVipContract.View activityView;

    private final BillingServices billingServices;

    private String accessToken;

    public BuyVipPresenter(Context context, BuyVipContract.View activityView, BillingServices billingServices) {
        this.context = context;
        this.activityView = activityView;
        this.billingServices = billingServices;

        this.activityView.setPresenter(this);

        accessToken = UserSessionManager.getAccessToken(context);

        setBillingServicesResult();
    }

    @Override
    public void accountModify() {
        User user = UserSessionManager.getCurrentUser(context);
        activityView.updateUserInfoView(user);
        if (user != null) {
            activityView.setTextMsgVung(user.getUserBalance());
        }
    }

    @Override
    public void buyVip(int position) {
        billingServices.buyVip(accessToken, position);
        activityView.showLoading(true);
    }

    @Override
    public void openActRechargeCard() {
        activityView.openActRechargeCard();
    }

    @Override
    public void openActRechargebank() {
        activityView.openActRechargebank();
    }

    @Override
    public void start() {
        User user = UserSessionManager.getCurrentUser(context);
        activityView.updateUserInfoView(user);

        // Lấy các gói VIP từ server về;
        activityView.showLoading(true);
        billingServices.loadPackagesVip(accessToken);
    }

    @Override
    public void onDestroy() {
        billingServices.cancel();
    }

    private void setBillingServicesResult() {

        billingServices.setOnBillingListener(new BillingServices.OnBillingListener() {
            @Override
            public void onLoadPackagesVipSuccess(int vungPoint, ArrayList<PackageVip> list) {
                activityView.showLoading(false);
                activityView.setTextMsgVung(vungPoint);
                activityView.addPackageVip(list);
            }

            @Override
            public void onBuyVipSuccess(User user, String msg) {
                activityView.showLoading(false);
                activityView.showMsgBuyVipSuccess(msg);
                UserSessionManager.updateUserSession(context, user);
            }

            @Override
            public void onLoadRechargeInfo(ArrayList<CardType> cardTypes, ArrayList<CardValue> cardValues) {

            }

            @Override
            public void onRechargeSuccess() {

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
