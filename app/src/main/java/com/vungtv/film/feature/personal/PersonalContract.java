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

        void openActLogin();

        void openActNapVip();

        void openActGiftcode();

        void openActUserInfo();

        void openActChangepass();

        void openActRecent();

        void openActFavorite();

        void openActFollow();

        void openActSetting();

        void openActAbout();

        void openActContact();

        void openActLogout();
    }

    interface Presenter extends BasePresenter {

        void loginStatusChange();

        void openActChangepass();
    }
}
