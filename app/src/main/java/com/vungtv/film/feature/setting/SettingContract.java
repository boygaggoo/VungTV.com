package com.vungtv.film.feature.setting;

import com.vungtv.film.BasePresenter;
import com.vungtv.film.BaseView;

/**
 * Created by pc on 2/6/2017.
 */

public interface SettingContract {

    interface View extends BaseView<Presenter> {

        void setTextVersion(String versionName);

        void setTextLanguage(String language);

        void setCheckedBtnSwitch(boolean checked);

        void showPopupSelectlanguage(android.view.View view);
    }

    interface Presenter extends BasePresenter {

        void showPopupLanguage(android.view.View anchor);

        void changeLanguage(String lang);

        void changeDownloadOnWifiStatus(boolean checked);
    }
}
