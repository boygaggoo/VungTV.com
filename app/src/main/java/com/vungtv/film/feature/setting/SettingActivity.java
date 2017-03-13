package com.vungtv.film.feature.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.vungtv.film.App;
import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;
import com.vungtv.film.widget.VtvToolbarSetting;
import com.vungtv.film.widget.VtvTwoTextView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class SettingActivity extends BaseActivity implements SettingContract.View {

    private SettingContract.Presenter presenter;

    @BindView(R.id.setting_toolbar)
    VtvToolbarSetting toolbar;

    @BindView(R.id.setting_version)
    VtvTwoTextView tvVersion;

    @BindView(R.id.setting_language)
    VtvTwoTextView tvLanguage;

    @BindView(R.id.setting_switch_download)
    Switch btnSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar.setOnToolbarListener(new VtvToolbarSetting.OnToolbarListener() {
            @Override
            public void onBtnBackClick() {
                finish();
            }
        });
        btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                presenter.changeDownloadOnWifiStatus(b);
            }
        });

        new SettingPresenter(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getInstance().trackScreenView("Setting Screen");
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void setTextVersion(String versionName) {
        tvVersion.setTextRight(versionName);
    }

    @Override
    public void setTextLanguage(String language) {
        tvLanguage.setTextRight(language);
    }

    @Override
    public void setCheckedBtnSwitch(boolean checked) {
        btnSwitch.setChecked(checked);
    }

    @Override
    public void showPopupSelectlanguage(View view) {

    }

    @Override
    public void setPresenter(SettingContract.Presenter Presenter) {
        this.presenter = checkNotNull(Presenter);
    }

    @OnClick(R.id.setting_language)
    public void changeLanguage(View view) {
        presenter.showPopupLanguage(view);
    }
}
