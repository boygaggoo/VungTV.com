package com.vungtv.film.feature.recharge;

import com.vungtv.film.BasePresenter;
import com.vungtv.film.BaseView;
import com.vungtv.film.model.CardType;
import com.vungtv.film.model.CardValue;

import java.util.ArrayList;

/**
 *
 * Created by pc on 2/22/2017.
 */

public interface RechargeContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean show);

        void showMsgError(String error);

        void showMsgRechargeSuccess();

        void addViewCardType(ArrayList<CardType> list);

        void addViewCardValue(ArrayList<CardValue> list);

        void clearEdittexts();

        void logOutAccount();
    }

    interface Presenter extends BasePresenter {

        void rechargeSubmit(String[] cardInfo);

    }
}
