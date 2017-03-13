package com.vungtv.film.feature.sendrequest;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.vungtv.film.App;
import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;
import com.vungtv.film.widget.VtvEditText;
import com.vungtv.film.widget.VtvToolbarSetting;

import butterknife.BindView;
import butterknife.OnClick;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class RequestActivity extends BaseActivity implements RequestContract.View, VtvToolbarSetting.OnToolbarListener {

    @BindView(R.id.request_toolbar)
    VtvToolbarSetting toolbar;

    @BindView(R.id.request_layout_content)
    LinearLayout layoutContent;

    @BindView(R.id.request_ed_title)
    VtvEditText edTitle;

    @BindView(R.id.request_ed_content)
    VtvEditText edContent;

    private RequestContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        toolbar.setOnToolbarListener(this);

        if (isScreenLand) {
            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) layoutContent.getLayoutParams();
            layoutParams.width = getResources()
                    .getDimensionPixelSize(R.dimen.content_size_sreen_land);
            layoutParams.topMargin = getResources()
                    .getDimensionPixelSize(R.dimen.space_16);
            int padding = getResources().getDimensionPixelSize(R.dimen.space_16);
            layoutContent.setPadding(padding, padding, padding, padding);
            layoutContent.setBackgroundResource(R.drawable.ds_bg_white_boder_radius);
            layoutContent.setLayoutParams(layoutParams);
        }

        new RequestPresenter(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getInstance().trackScreenView("Request Screen");
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        LinearLayout.LayoutParams layoutParams =
                (LinearLayout.LayoutParams) layoutContent.getLayoutParams();
        if (isScreenLand) {
            layoutParams.width = getResources()
                    .getDimensionPixelSize(R.dimen.content_size_sreen_land);
            layoutParams.topMargin = getResources()
                    .getDimensionPixelSize(R.dimen.space_16);
            int padding = getResources().getDimensionPixelSize(R.dimen.space_16);
            layoutContent.setPadding(padding, padding, padding, padding);
            layoutContent.setBackgroundResource(R.drawable.ds_bg_white_boder_radius);
        } else {
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            layoutParams.topMargin = 0;
            layoutContent.setPadding(0, 0, 0, 0);
            layoutContent.setBackgroundResource(R.color.gray_1);
        }

        layoutContent.setLayoutParams(layoutParams);
    }

    @OnClick(R.id.request_btn_submit)
    public void onBtnSubmitClick() {
        presenter.sendRequest(edTitle.getText().toString(), edContent.getText().toString());
    }

    @Override
    public void setPresenter(RequestContract.Presenter Presenter) {
        presenter = checkNotNull(Presenter);
    }

    @Override
    public void showLoading(boolean show) {
        popupLoading.show(show);
    }

    @Override
    public void showMsgError(String msg) {
        showToast(msg);
    }

    @Override
    public void resetEdittext() {
        edTitle.setText("");
        edContent.setText("");
    }

    @Override
    public void onBtnBackClick() {
        finish();
    }
}
