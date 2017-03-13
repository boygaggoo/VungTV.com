package com.vungtv.film.feature.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.EditText;

import com.vungtv.film.App;
import com.vungtv.film.BaseFragment;
import com.vungtv.film.R;
import com.vungtv.film.util.ActivityUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 *
 *
 * Created by pc on 2/3/2017.
 */

public class LoginFragment extends BaseFragment implements LoginContract.View {
    private static final String TAG = LoginFragment.class.getSimpleName();

    private LoginContract.Presenter presenter;

    @BindView(R.id.login_ed_email)
    EditText edEmail;

    @BindView(R.id.login_ed_pass)
    EditText edPass;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_login;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getInstance().trackScreenView("Login Screen");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
    public void setPresenter(LoginContract.Presenter Presenters) {
        presenter = checkNotNull(Presenters);;
    }

    @OnClick(R.id.login_btn_login)
    public void startLoginWithEmail() {
        presenter.loginWithEmail(edEmail.getText().toString(), edPass.getText().toString());
    }

    @OnClick(R.id.login_btn_loginfb)
    public void startLoginWithFacebook() {
        presenter.loginWithFacebook();
    }

    @OnClick(R.id.login_btn_logingg)
    public void startLoginWithGoogle() {
        presenter.loginWithGoogle();
    }

    @OnClick(R.id.login_btn_forgetpass)
    public void fogetPass() {
        presenter.fogetPass();
    }

    @OnClick(R.id.login_btn_register)
    public void openFragRegister() {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }

        RegisterFragment fragment = new RegisterFragment();
        ActivityUtils.addFragmentToActivity(
                fragmentManager, fragment, R.id.login_framelayout);

        presenter.openFragRegister(fragment);
    }
}
