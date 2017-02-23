package com.vungtv.film.feature.setting;

import android.content.Context;
import android.view.View;

import com.vungtv.film.BuildConfig;
import com.vungtv.film.data.source.local.SettingsManager;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 *
 * Created by pc on 2/6/2017.
 */

public class SettingPresenter implements SettingContract.Presenter {

    private final Context context;

    private final SettingContract.View settingView;


    public SettingPresenter(Context context, SettingContract.View settingView) {
        this.context = checkNotNull(context);
        this.settingView = checkNotNull(settingView);
        settingView.setPresenter(this);
    }

    @Override
    public void showPopupLanguage(View anchor) {

    }

    @Override
    public void changeLanguage(String lang) {
        SettingsManager.saveSettingLanguage(context, lang);
    }

    @Override
    public void changeDownloadOnWifiStatus(boolean checked) {
        SettingsManager.saveDownLoadOnWifi(context, checked);
    }

    @Override
    public void start() {
        settingView.setTextVersion(String.format("v %s", BuildConfig.VERSION_NAME));
        settingView.setTextLanguage(SettingsManager.getTextSettingLanguage(context));
        settingView.setCheckedBtnSwitch(SettingsManager.isDownLoadOnWifi(context));
    }

    @Override
    public void onDestroy() {

    }
}
