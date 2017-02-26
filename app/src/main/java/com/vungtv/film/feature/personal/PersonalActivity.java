package com.vungtv.film.feature.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;
import com.vungtv.film.eventbus.AccountModifyEvent;
import com.vungtv.film.feature.buyvip.BuyVipActivity;
import com.vungtv.film.feature.changepass.ChangePassActivity;
import com.vungtv.film.feature.favorite.FavoriteActivity;
import com.vungtv.film.feature.login.LoginActivity;
import com.vungtv.film.feature.logout.LogOutActivity;
import com.vungtv.film.feature.setting.SettingActivity;
import com.vungtv.film.feature.userinfo.UserInfoActivity;
import com.vungtv.film.interfaces.OnItemClickListener;
import com.vungtv.film.model.User;
import com.vungtv.film.util.LoginGoogleUtils;
import com.vungtv.film.widget.VtvUserInfoHeaderView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class PersonalActivity extends BaseActivity implements PersonalContract.View {
    private static final String TAG = PersonalActivity.class.getSimpleName();

    private PersonalContract.Presenter presenter;

    private PersonalRecyclerAdapter adapter;

    @BindView(R.id.userpage_header)
    VtvUserInfoHeaderView userPageHeader;

    @BindView(R.id.userpage_ll_btn_vip_code)
    ViewGroup groupBtnVipCode;

    @BindView(R.id.userpage_recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        EventBus.getDefault().register(this);

        setUserPageHeader();
        setRecyclerView();
        setGruopBtnVipCode();

        new PersonalPresenter(this, this, new LoginGoogleUtils(this));
        presenter.start();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        presenter.onDestroy();
        super.onDestroy();
    }

    @Subscribe
    public void accountModifyEvent(AccountModifyEvent event) {
        presenter.loginStatusChange();
    }

    @OnClick(R.id.userpage_btn_back)
    public void onClickExit() {
        finish();
    }

    @Override
    public void updateUserInfoView(User user) {
        userPageHeader.setLogin(user);
    }

    @Override
    public void updateRecylerViewItems(boolean isLogin) {
        adapter.notifyLoginStatusChange(isLogin);
    }

    @Override
    public void setVisibleBtnGiftCodeAndVip(boolean show) {
        groupBtnVipCode.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setNumberMoviesFollow(int num) {
        adapter.notifyChangeLabelItemFollow(num);
    }

    @Override
    public void openActLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void openActNapVip() {
        startActivity(new Intent(this, BuyVipActivity.class));
    }

    @Override
    public void openActGiftcode() {
        Toast.makeText(getApplicationContext(), "Open Activity Giftcode", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(PersonalContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    private void setUserPageHeader() {
        userPageHeader.setOnUserPageHeaderListener(new VtvUserInfoHeaderView.OnUserPageHeaderListener() {
            @Override
            public void onBtnLoginClick() {
                presenter.openActLogin();
            }

            @Override
            public void onBtnChangeAvatar() {

            }
        });
    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new PersonalRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                int itemId = (int) adapter.getItemId(pos);
                switch (itemId) {
                    case PersonalRecyclerAdapter.USERPAGE_ITEMID.USER_INFO:
                        startActivity(new Intent(PersonalActivity.this, UserInfoActivity.class));
                        break;
                    case PersonalRecyclerAdapter.USERPAGE_ITEMID.CHANGE_PASS:
                        startActivity(new Intent(PersonalActivity.this, ChangePassActivity.class));
                        break;
                    case PersonalRecyclerAdapter.USERPAGE_ITEMID.INVITE_FRIENDS:

                        break;
                    case PersonalRecyclerAdapter.USERPAGE_ITEMID.FILM_VIEWED:

                        break;
                    case PersonalRecyclerAdapter.USERPAGE_ITEMID.FAVORITE:
                        startActivity(new Intent(PersonalActivity.this, FavoriteActivity.class));
                        break;
                    case PersonalRecyclerAdapter.USERPAGE_ITEMID.FOLLOW:

                        break;
                    case PersonalRecyclerAdapter.USERPAGE_ITEMID.SETTING:
                        // Open activity Setting;
                        startActivity(new Intent(PersonalActivity.this, SettingActivity.class));
                        break;
                    case PersonalRecyclerAdapter.USERPAGE_ITEMID.APP_INFO:

                        break;
                    case PersonalRecyclerAdapter.USERPAGE_ITEMID.CONTACT:

                        break;
                    case PersonalRecyclerAdapter.USERPAGE_ITEMID.LOGOUT:
                        startActivity(new Intent(PersonalActivity.this, LogOutActivity.class));
                        break;
                }
            }
        });
    }

    private void setGruopBtnVipCode() {
        //Click btn Nap VIP
        if (groupBtnVipCode.getChildAt(0) == null) return;
        groupBtnVipCode.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openActNapVip();
            }
        });
        // Click btn GiftCode
        if (groupBtnVipCode.getChildAt(1) == null) return;
        groupBtnVipCode.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openActGiftcode();
            }
        });
    }
}
