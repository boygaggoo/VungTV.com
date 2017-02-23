package com.vungtv.film.feature.buyvip;

import com.vungtv.film.BasePresenter;
import com.vungtv.film.BaseView;
import com.vungtv.film.model.PackageVip;
import com.vungtv.film.model.User;

import java.util.ArrayList;

/**
 *
 * Created by Mr Cuong on 2/18/2017.
 */

public interface BuyVipContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean show);

        void showMsgError(String error);

        void showMsgBuyVipSuccess(String msg);

        void updateUserInfoView(User user);

        void setTextMsgVung(int point);

        void addPackageVip(ArrayList<PackageVip> packageVips);

        void openActRechargeCard();

        void openActRechargebank();

        void logOutAccount();
    }

    interface Presenter extends BasePresenter {

        void accountModify();

        void buyVip(int position);

        void openActRechargeCard();

        void openActRechargebank();
    }
}
