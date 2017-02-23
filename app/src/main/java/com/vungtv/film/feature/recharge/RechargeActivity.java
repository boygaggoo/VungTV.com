package com.vungtv.film.feature.recharge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;
import com.vungtv.film.data.source.remote.service.BillingServices;
import com.vungtv.film.feature.logout.LogOutActivity;
import com.vungtv.film.model.CardType;
import com.vungtv.film.model.CardValue;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.widget.VtvEditText;
import com.vungtv.film.widget.VtvRadioButton;
import com.vungtv.film.widget.VtvToolbarSetting;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class RechargeActivity extends BaseActivity implements RechargeContract.View {

    private static final String TAG = RechargeActivity.class.getSimpleName();

    private RechargeContract.Presenter presenter;

    private RechargeRecyclerAdapter adapter;

    @BindView(R.id.recharge_toolbar)
    VtvToolbarSetting toolbar;

    @BindView(R.id.recharge_radiogroup)
    RadioGroup radioGroup;

    @BindView(R.id.recharge_ed_card_number)
    VtvEditText edCardNumber;

    @BindView(R.id.recharge_ed_card_serial)
    VtvEditText edCardSerial;

    @BindView(R.id.recharge_recycler)
    RecyclerView recyclerView;

    private ArrayList<CardType> cardTypes = new ArrayList<>();

    private String[] cardInfo = {"", "", ""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        toolbar.setOnToolbarListener(new VtvToolbarSetting.OnToolbarListener() {
            @Override
            public void onBtnBackClick() {
                finish();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (cardTypes.get(i) != null) {
                    cardInfo[0] = cardTypes.get(i).getCode();
                    LogUtils.e(TAG, "onCheckedChanged: cardInfo[0] = " + cardInfo[0]);
                } else {
                    LogUtils.e(TAG, "onCheckedChanged: cardTypes.size = " + cardTypes.size());
                }
            }
        });

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new RechargeRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);

        new RechargePresenter(this, this, new BillingServices(this));
        presenter.start();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
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
    public void showMsgRechargeSuccess(String msg) {
        // nap vung thanh cong
        showToast(msg);
    }

    @Override
    public void addViewCardType(ArrayList<CardType> list) {
        cardTypes = list;
        radioGroup.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int textSize = getResources().getDimensionPixelSize(R.dimen.font_normal);
        for (int i = 0; i < cardTypes.size(); i++) {
            CardType cardType = cardTypes.get(i);
            VtvRadioButton radioButton = new VtvRadioButton(this);
            radioButton.setId(i);
            radioButton.setTextSize(textSize);
            radioButton.setText(cardType.getLabel());
            radioGroup.addView(radioButton, layoutParams);
        }
    }

    @Override
    public void addViewCardValue(ArrayList<CardValue> list) {
        adapter.setList(list);
    }

    @Override
    public void clearEdittexts() {
        edCardNumber.setText("");
        edCardSerial.setText("");
    }

    @Override
    public void logOutAccount() {
        startActivity(new Intent(this, LogOutActivity.class));
    }

    @Override
    public void setPresenter(RechargeContract.Presenter Presenter) {
        presenter = Presenter;
    }

    @OnClick(R.id.recharge_btn_submit)
    public void onBtnRechargeOnclick() {
        cardInfo[1] = edCardNumber.getText().toString();
        cardInfo[2] = edCardSerial.getText().toString();

        presenter.rechargeSubmit(cardInfo);
    }
}
