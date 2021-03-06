package com.vungtv.film.feature.logout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.firebase.iid.FirebaseInstanceId;
import com.vungtv.film.App;
import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;
import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.data.source.remote.service.AccountServices;
import com.vungtv.film.eventbus.AccountModifyEvent;
import com.vungtv.film.util.LoginGoogleUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

/**
 *
 * Created by pc on 2/22/2017.
 */

public class LogOutActivity extends BaseActivity implements AccountServices.OnLogoutListener {
    public static final int INTENT_RC = 111;

    private LoginGoogleUtils loginGoogleUtils;

    private AccountServices accountServices;

    private UnsubscribeAsync unsubscribeAsync;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        EventBus.getDefault().register(this);

        accountServices = new AccountServices(this);
        accountServices.setOnLogoutListener(this);
        accountServices.logout(UserSessionManager.getAccessToken(this));

        loginGoogleUtils = new LoginGoogleUtils(this);
        popupLoading.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getInstance().trackScreenView("Logout Screen");
    }

    @Override
    protected void onDestroy() {
        if (loginGoogleUtils != null) {
            loginGoogleUtils.disconect();
        }
        if (accountServices != null) {
            accountServices.cancel();
        }
        if (unsubscribeAsync != null && unsubscribeAsync.getStatus() == AsyncTask.Status.RUNNING) {
            unsubscribeAsync.cancel(true);
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void accountModifyEvent(AccountModifyEvent event) {
        popupLoading.dismiss();
        finish();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onFailure(int code, String error) {
        popupLoading.dismiss();
        showToast(error);
        finish();
    }

    @Override
    public void onLogoutSuccess() {
        unsubscribeAsync = new UnsubscribeAsync();
        unsubscribeAsync.execute();
    }

    private class UnsubscribeAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // unsubscribe all topic
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            UserSessionManager.logout(getApplicationContext(), loginGoogleUtils);
            showToast(R.string.logout_text_success);
        }
    }
}
