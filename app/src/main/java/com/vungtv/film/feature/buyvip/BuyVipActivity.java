package com.vungtv.film.feature.buyvip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vungtv.film.App;
import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;
import com.vungtv.film.data.source.remote.service.BillingServices;
import com.vungtv.film.eventbus.AccountModifyEvent;
import com.vungtv.film.feature.logout.LogOutActivity;
import com.vungtv.film.feature.recharge.RechargeActivity;
import com.vungtv.film.interfaces.OnItemClickListener;
import com.vungtv.film.model.PackageVip;
import com.vungtv.film.model.User;
import com.vungtv.film.util.TextUtils;
import com.vungtv.film.widget.VtvTextView;
import com.vungtv.film.widget.VtvUserInfoHeaderView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class BuyVipActivity extends BaseActivity implements BuyVipContract.View {
    private static final String TAG = BuyVipActivity.class.getSimpleName();

    private BuyVipContract.Presenter presenter;

    private BuyVipRecyclerAdapter adapter;

    @BindView(R.id.buyvip_header)
    VtvUserInfoHeaderView userInfoView;

    @BindView(R.id.buyvip_tv_msg)
    VtvTextView tvMsgVung;

    @BindView(R.id.buyvip_recyclerview)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_vip);
        EventBus.getDefault().register(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new BuyVipRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                presenter.buyVip(pos);
                App.getInstance().trackEvent("Personal", "Buy vip", "Click buy vip");
            }
        });

        new BuyVipPresenter(this, this, new BillingServices(this));

        presenter.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getInstance().trackScreenView("Buy Vip Screen");
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        presenter.onDestroy();
        super.onDestroy();
    }

    @Subscribe
    public void accountModifyEvent(AccountModifyEvent event) {
        presenter.accountModify();
    }

    @OnClick(R.id.buyvip_btn_back)
    public void onBtnExitClick() {
        finish();
    }

    @OnClick(R.id.buyvip_btn_nap_mobilecard)
    public void onBtnRechageMobiCard() {
        presenter.openActRechargeCard();
    }

    @OnClick(R.id.buyvip_btn_nap_bankcard)
    public void onBtnRechageBankCard() {
        presenter.openActRechargebank();
    }

    @Override
    public void showLoading(boolean show) {
        popupLoading.show(show);
    }

    @Override
    public void showMsgError(String error) {
        showToast(error);
    }

    @Override
    public void showMsgBuyVipSuccess(String msg) {
        showToast(msg);
    }

    @Override
    public void updateUserInfoView(User user) {
        userInfoView.setLogin(user);
    }

    @Override
    public void setTextMsgVung(int point) {
        String text = String.format(
                getString(R.string.buyvip_text_buy_vung),
                String.valueOf(point)
        );
        tvMsgVung.setText(TextUtils.styleTextHtml(text));
    }

    @Override
    public void addPackageVip(ArrayList<PackageVip> packageVips) {
        adapter.setList(packageVips);
    }

    @Override
    public void openActRechargeCard() {
        startActivity(new Intent(this, RechargeActivity.class));
    }

    @Override
    public void openActRechargebank() {

    }

    @Override
    public void logOutAccount() {
        startActivity(new Intent(this, LogOutActivity.class));
    }

    @Override
    public void setPresenter(BuyVipContract.Presenter Presenter) {
        presenter = Presenter;
    }
}
