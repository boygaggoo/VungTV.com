package com.vungtv.film.feature.changepass;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.vungtv.film.App;
import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;
import com.vungtv.film.data.source.remote.service.AccountServices;
import com.vungtv.film.feature.logout.LogOutActivity;
import com.vungtv.film.model.User;
import com.vungtv.film.widget.VtvUserInfoHeaderView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class ChangePassActivity extends BaseActivity implements ChangePassContract.View{

    private ChangePassContract.Presenter presenter;

    @BindView(R.id.userchangepass_header)
    VtvUserInfoHeaderView userInfoView;

    @BindView(R.id.userchangepass_ed_pass)
    EditText edPass;

    @BindView(R.id.userchangepass_ed_newpass)
    EditText edNewPass;

    @BindView(R.id.userchangepass_ed_renewpass)
    EditText edReNewPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        new ChangePassPresenter(this, new AccountServices(this), this);
        presenter.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getInstance().trackScreenView("Change Pass Screen");
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
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
    public void updateUserInfoView(User user) {
        userInfoView.setLogin(user);
    }

    @Override
    public void clearEdittexts() {
        edPass.setText("");
        edNewPass.setText("");
        edReNewPass.setText("");
    }

    @Override
    public void changePassSuccess() {
        showToast(getString(R.string.user_change_pass_success));
    }

    @Override
    public void logOutAccount() {
        startActivityForResult(new Intent(this, LogOutActivity.class), LogOutActivity.INTENT_RC);
    }

    @Override
    public void setPresenter(ChangePassContract.Presenter Presenter) {
        presenter = checkNotNull(Presenter);
    }

    @OnClick(R.id.userchangepass_btn_submmit)
    public void onBtnChangePassClick() {
        String[] passwords = {
                edPass.getText().toString(),
                edNewPass.getText().toString(),
                edReNewPass.getText().toString()
        };
        presenter.changePass(passwords);
    }

    @OnClick(R.id.userchangepass_btn_back)
    public void onBtnExitClick() {
        finish();
    }

}
