package com.vungtv.film.feature.userinfo;

import android.content.Intent;
import android.os.Bundle;

import com.vungtv.film.App;
import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;
import com.vungtv.film.data.source.remote.service.AccountServices;
import com.vungtv.film.eventbus.AccountModifyEvent;
import com.vungtv.film.feature.changepass.ChangePassActivity;
import com.vungtv.film.feature.logout.LogOutActivity;
import com.vungtv.film.model.User;
import com.vungtv.film.popup.PopupUserChangeName;
import com.vungtv.film.widget.VtvTwoTextView;
import com.vungtv.film.widget.VtvUserInfoHeaderView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class UserInfoActivity extends BaseActivity implements UserInfoContract.View {

    private UserInfoContract.Presenter presenter;

    @BindView(R.id.userinfo_header)
    VtvUserInfoHeaderView userInfoView;

    @BindView(R.id.userinfo_email)
    VtvTwoTextView tvEmail;

    @BindView(R.id.userinfo_displayname)
    VtvTwoTextView tvDisplayName;

    @BindView(R.id.userinfo_pass)
    VtvTwoTextView tvPass;

    @BindView(R.id.userinfo_creatDate)
    VtvTwoTextView tvCreatedDate;

    private PopupUserChangeName  popupUserChangeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        EventBus.getDefault().register(this);
        tvDisplayName.setOnVtvTwoTextViewListener(new VtvTwoTextView.OnVtvTwoTextViewListener() {
            @Override
            public void onBtnEditClick() {
                presenter.showPopupChangeName();
            }
        });

        tvPass.setOnVtvTwoTextViewListener(new VtvTwoTextView.OnVtvTwoTextViewListener() {
            @Override
            public void onBtnEditClick() {
                presenter.openActChangePass();
            }
        });

        new UserInfoPresenter(this, this, new AccountServices(this));
        presenter.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getInstance().trackScreenView("User Infomation Screen");
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (popupUserChangeName != null) {
            popupUserChangeName.dismiss();
            popupUserChangeName = null;
        }
        presenter.onDestroy();
        super.onDestroy();
    }

    @Subscribe
    public void onEventBusUserUpdated(AccountModifyEvent event) {
        presenter.updateUserInfo();
    }

    @Override
    public void showLoading(boolean show) {
        if (show) {
            popupLoading.show();
        } else {
            popupLoading.dismiss();
        }
    }

    @Override
    public void showMsgError(String error) {
        showToast(error);
    }

    @Override
    public void showMsgChangenameSuccess() {
        showToast(getString(R.string.user_change_name_success));
    }

    @Override
    public void setUserInfoView(User user) {
        userInfoView.setLogin(user);
    }

    @Override
    public void setTextEmail(String email) {
        tvEmail.setTextRight(email);
    }

    @Override
    public void setTextDisplayName(String name) {
        tvDisplayName.setTextRight(name);
    }

    @Override
    public void setTextCreatedDate(String date) {
        tvCreatedDate.setTextRight(date);
    }

    @Override
    public void showPopupChangeName() {
        if (popupUserChangeName == null) {
            popupUserChangeName = new PopupUserChangeName(this);
            popupUserChangeName.setOnPopupChangeNameListener(new PopupUserChangeName.OnPopupChangeNameListener() {
                @Override
                public void onChangename(String newName) {
                    presenter.changeDisplayName(newName);
                }
            });
        }

        popupUserChangeName.show();
    }

    @Override
    public void openActChangePass() {
        startActivity(new Intent(this, ChangePassActivity.class));
    }

    @Override
    public void logOutAccount() {
        startActivityForResult(new Intent(this, LogOutActivity.class), LogOutActivity.INTENT_RC);
    }

    @Override
    public void setPresenter(UserInfoContract.Presenter Presenter) {
        this.presenter = checkNotNull(Presenter);
    }

    @OnClick(R.id.userinfo_btn_back)
    public void onBtnExitClick() {
        finish();
    }
}
