package com.vungtv.film.feature.personal;

import com.vungtv.film.BasePresenter;
import com.vungtv.film.BaseView;
import com.vungtv.film.model.User;

/**
 * This specifies the contract between the view and the presenter.
 * Created by Mr Cuong on 2/5/2017.
 */

public interface PersonalContract {

    interface View extends BaseView<Presenter> {

        void updateUserInfoView(User user);

        void updateRecylerViewItems(boolean isLogin);

        void setVisibleBtnGiftCodeAndVip(boolean show);

        void setNumberMoviesFollow(int num);

        void openActLogin();

        void openActNapVip();

        void openActGiftcode();
    }

    interface Presenter extends BasePresenter {

        void loginStatusChange();

        void logOut();

        void openActLogin();

        void openActNapVip();

        void openActGiftcode();
    }
}
