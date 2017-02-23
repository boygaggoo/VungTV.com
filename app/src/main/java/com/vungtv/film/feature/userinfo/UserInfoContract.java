package com.vungtv.film.feature.userinfo;

import com.vungtv.film.BasePresenter;
import com.vungtv.film.BaseView;
import com.vungtv.film.model.User;

/**
 *
 * Created by Mr Cuong on 2/7/2017.
 */

public interface UserInfoContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean show);

        void showMsgError(String error);

        void showMsgChangenameSuccess();

        void setUserInfoView(User user);

        void setTextEmail(String email);

        void setTextDisplayName(String name);

        void setTextCreatedDate(String date);

        void showPopupChangeName();

        void openActChangePass();

        void logOutAccount();
    }

    interface Presenter extends BasePresenter {

        void showPopupChangeName();

        void changeDisplayName(String newName);

        void openActChangePass();

        void updateUserInfo();
    }
}
