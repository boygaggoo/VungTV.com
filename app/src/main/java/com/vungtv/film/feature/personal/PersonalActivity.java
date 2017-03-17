package com.vungtv.film.feature.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.vungtv.film.App;
import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;
import com.vungtv.film.eventbus.AccountModifyEvent;
import com.vungtv.film.eventbus.FollowNotifyCountEvent;
import com.vungtv.film.feature.aboutcontact.AboutActivity;
import com.vungtv.film.feature.buyvip.BuyVipActivity;
import com.vungtv.film.feature.changepass.ChangePassActivity;
import com.vungtv.film.feature.login.LoginActivity;
import com.vungtv.film.feature.logout.LogOutActivity;
import com.vungtv.film.feature.recent.RecentActivity;
import com.vungtv.film.feature.setting.SettingActivity;
import com.vungtv.film.feature.userinfo.UserInfoActivity;
import com.vungtv.film.feature.usermovies.UserMoviesActivity;
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
    protected void onResume() {
        super.onResume();
        App.getInstance().trackScreenView("Personal Screen");
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

    @Subscribe
    public void onEventUpdateFollowNotifyCount(FollowNotifyCountEvent eventBus) {
        adapter.notifyFollowCountChange();
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
        showToast("Comming soon...");
    }

    @Override
    public void openActUserInfo() {
        startActivity(new Intent(this, UserInfoActivity.class));
    }

    @Override
    public void openActChangepass() {
        startActivity(new Intent(PersonalActivity.this, ChangePassActivity.class));
    }

    @Override
    public void openActRecent() {
        startActivity(new Intent(this, RecentActivity.class));
    }

    @Override
    public void openActFavorite() {
        startActivity(UserMoviesActivity.buildIntent(this, UserMoviesActivity.PAGE_FAVORITE));
    }

    @Override
    public void openActFollow() {
        startActivity(UserMoviesActivity.buildIntent(this, UserMoviesActivity.PAGE_FOLLOW));
    }

    @Override
    public void openActSetting() {
        startActivity(new Intent(this, SettingActivity.class));
    }

    @Override
    public void openActAbout() {
        startActivity(AboutActivity.buildIntentAbout(this));
    }

    @Override
    public void openActContact() {
        startActivity(AboutActivity.buildIntentContact(this));
    }

    @Override
    public void openActLogout() {
        startActivity(new Intent(this, LogOutActivity.class));
    }

    @Override
    public void setPresenter(PersonalContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    private void setUserPageHeader() {
        userPageHeader.setOnUserPageHeaderListener(new VtvUserInfoHeaderView.OnUserPageHeaderListener() {
            @Override
            public void onBtnLoginClick() {
                openActLogin();
            }

            @Override
            public void onBtnChangeAvatar() {
                showToast("Comming soon...");
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
                        openActUserInfo();
                        break;
                    case PersonalRecyclerAdapter.USERPAGE_ITEMID.CHANGE_PASS:
                        presenter.openActChangepass();
                        break;
                    case PersonalRecyclerAdapter.USERPAGE_ITEMID.INVITE_FRIENDS:
                        showToast(R.string.error_not_been_updated);
                        break;
                    case PersonalRecyclerAdapter.USERPAGE_ITEMID.FILM_VIEWED:
                        openActRecent();
                        break;
                    case PersonalRecyclerAdapter.USERPAGE_ITEMID.FAVORITE:
                        openActFavorite();
                        break;
                    case PersonalRecyclerAdapter.USERPAGE_ITEMID.FOLLOW:
                        openActFollow();
                        break;
                    case PersonalRecyclerAdapter.USERPAGE_ITEMID.SETTING:
                        openActSetting();
                        break;
                    case PersonalRecyclerAdapter.USERPAGE_ITEMID.APP_INFO:
                        openActAbout();
                        break;
                    case PersonalRecyclerAdapter.USERPAGE_ITEMID.CONTACT:
                        openActContact();
                        break;
                    case PersonalRecyclerAdapter.USERPAGE_ITEMID.LOGOUT:
                        openActLogout();
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
                openActNapVip();
            }
        });
        // Click btn GiftCode
        if (groupBtnVipCode.getChildAt(1) == null) return;
        groupBtnVipCode.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActGiftcode();
            }
        });
    }
}
