package com.vungtv.film.feature.login;

import android.support.v4.app.FragmentManager;
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

public class RegisterFragment extends BaseFragment implements LoginContract.View {
    private static final String TAG = RegisterFragment.class.getSimpleName();

    private LoginContract.Presenter presenter;

    @BindView(R.id.register_ed_email)
    EditText edEmail;

    @BindView(R.id.register_ed_pass)
    EditText edPass;

    @BindView(R.id.register_ed_repass)
    EditText edRePass;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_register;
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getInstance().trackScreenView("Register Screen");
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
        presenter = checkNotNull(Presenters);
    }

    @OnClick(R.id.register_btn_register)
    public void startRegAcc() {
        presenter.registerAcc(edEmail.getText().toString(), edPass.getText().toString(), edRePass.getText().toString());
    }

    @OnClick(R.id.register_btn_loginfb)
    public void startLoginWithFacebook() {
        presenter.loginWithFacebook();
    }

    @OnClick(R.id.register_btn_logingg)
    public void startLoginWithGoogle() {
        presenter.loginWithGoogle();
    }

    @OnClick(R.id.register_btn_login)
    public void openFragLogin() {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        LoginFragment fragment = new LoginFragment();
        ActivityUtils.addFragmentToActivity(
                fragmentManager, fragment, R.id.login_framelayout);
        presenter.openFragLogin(fragment);
    }
}
