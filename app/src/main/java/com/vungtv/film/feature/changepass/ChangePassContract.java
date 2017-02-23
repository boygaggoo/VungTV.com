package com.vungtv.film.feature.changepass;

import com.vungtv.film.BasePresenter;
import com.vungtv.film.BaseView;
import com.vungtv.film.model.User;

/**
 *
 * Created by Mr Cuong on 2/18/2017.
 */

public interface ChangePassContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean show);

        void showMsgError(String error);

        void updateUserInfoView(User user);

        void clearEdittexts();

        void changePassSuccess();

        void logOutAccount();
    }

    interface Presenter extends BasePresenter {

        boolean validateInput(String[] passwords);

        void changePass(String[] passwords);
    }
}
