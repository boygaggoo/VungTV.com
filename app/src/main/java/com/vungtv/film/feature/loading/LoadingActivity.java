package com.vungtv.film.feature.loading;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import com.vungtv.film.R;
import com.vungtv.film.eventbus.AccountModifyEvent;
import com.vungtv.film.feature.home.HomeActivity;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.util.LoginGoogleUtils;
import com.vungtv.film.widget.VtvTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadingActivity extends AppCompatActivity implements LoadingContract.View {

    private LoadingContract.Presenter presenter;

    @BindView(R.id.loading_version)
    VtvTextView tvVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        new LoadingPresenter(this, this, new LoginGoogleUtils(this));

        getHashKeyfacebook();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void accountModifyEvent(AccountModifyEvent event) {
        openActHome();
    }

    @Override
    public void setVersionName(String version) {
        tvVersionName.setText(version);
    }

    @Override
    public void openActHome() {
        Intent intent = new Intent(LoadingActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void setPresenter(LoadingContract.Presenter Presenter) {
        presenter = Presenter;
    }

    /**
     * Get hash key for facebook sdk;
     */
    private void getHashKeyfacebook() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.vungtv.film",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                LogUtils.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            LogUtils.e("KeyHash: ", "Not found ! " + e.getMessage());
        }
    }
}
