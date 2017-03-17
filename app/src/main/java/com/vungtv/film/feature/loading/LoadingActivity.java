package com.vungtv.film.feature.loading;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import com.vungtv.film.App;
import com.vungtv.film.R;
import com.vungtv.film.eventbus.LoadingToHomeEvent;
import com.vungtv.film.feature.home.HomeActivity;
import com.vungtv.film.popup.PopupUpdate;
import com.vungtv.film.util.IntentUtils;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.util.LoginGoogleUtils;
import com.vungtv.film.widget.VtvTextView;

import org.greenrobot.eventbus.EventBus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadingActivity extends AppCompatActivity implements LoadingContract.View {

    public static final String INTENT_MOV_ID_NOTIFY = "INTENT_MOV_ID_NOTIFY";

    private LoadingContract.Presenter presenter;

    @BindView(R.id.loading_version)
    VtvTextView tvVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ButterKnife.bind(this);

        new LoadingPresenter(this, this, new LoginGoogleUtils(this));

        presenter.getIntent(getIntent());

        getHashKeyfacebook();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getInstance().trackScreenView("Loading Screen");
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void setVersionName(String version) {
        tvVersionName.setText(version);
    }

    @Override
    public void openActHome(int movId) {
        if (movId > 0) {
            EventBus.getDefault().postSticky(new LoadingToHomeEvent(movId));
        }
        Intent intent = new Intent(LoadingActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void resumeActHome(int movId) {
        if (movId > 0) {
            EventBus.getDefault().postSticky(new LoadingToHomeEvent(movId));
        }
        finish();
    }

    @Override
    public void showPopupUpdate(String content, boolean forceUpdate) {
        PopupUpdate popupUpdate = new PopupUpdate(this);
        popupUpdate.setTextContentFromHtml(content);
        popupUpdate.setOnPopupUpdateListener(new PopupUpdate.OnPopupUpdateListener() {
            @Override
            public void onPopupUpdateBtnConfirmClick() {
                presenter.confirmUpdate();
            }

            @Override
            public void onPopupUpdateBtnCancelClick() {
                presenter.cancelUpdate();
            }
        });
        popupUpdate.setBtnCancelVisible(!forceUpdate);
        popupUpdate.show();
    }

    @Override
    public void openAppOnPlayStore() {
        startActivity(IntentUtils.updateAppFromStore());
    }

    @Override
    public void openAppOnWebsite(String url) {
        startActivity(IntentUtils.openUrlOnWebsite(url));
    }

    @Override
    public void closeApp() {
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
