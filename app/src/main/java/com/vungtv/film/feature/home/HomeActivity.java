package com.vungtv.film.feature.home;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.vungtv.film.App;
import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;
import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.data.source.remote.ApiQuery;
import com.vungtv.film.data.source.remote.model.ApiHomeMenu;
import com.vungtv.film.data.source.remote.service.HomeServices;
import com.vungtv.film.eventbus.AccountModifyEvent;
import com.vungtv.film.eventbus.ConfigurationChangedEvent;
import com.vungtv.film.eventbus.FollowNotifyCountEvent;
import com.vungtv.film.eventbus.LoadingToHomeEvent;
import com.vungtv.film.feature.buyvip.BuyVipActivity;
import com.vungtv.film.feature.filtermovies.FilterMoviesActivity;
import com.vungtv.film.feature.home.HomeNavAdapter.OnNavItemSelectedListener;
import com.vungtv.film.feature.login.LoginActivity;
import com.vungtv.film.feature.menumovies.MenuMoviesActivity;
import com.vungtv.film.feature.moviedetail.MovieDetailActivity;
import com.vungtv.film.feature.personal.PersonalActivity;
import com.vungtv.film.feature.search.SearchActivity;
import com.vungtv.film.feature.usermovies.UserMoviesActivity;
import com.vungtv.film.model.NavItem;
import com.vungtv.film.popup.PopupMessenger;
import com.vungtv.film.util.ActivityUtils;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.util.UriPaser;
import com.vungtv.film.widget.VtvDrawerLayout;
import com.vungtv.film.widget.VtvToolbarHome;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

import static com.vungtv.film.data.source.remote.ApiQuery.PATH_FOLLOW;

public class HomeActivity extends BaseActivity implements OnNavItemSelectedListener, com.vungtv.film.data.source.remote.service.HomeServices.HomeMenuResultCallback, VtvToolbarHome.OnBtnClickListener {
    private static final String TAG = HomeActivity.class.getSimpleName();

    private HomeNavAdapter navAdapter;

    private HomeServices HomeServices;

    @BindView(R.id.home_toolbar)
    VtvToolbarHome toolbar;

    @BindView(R.id.home_drawer_layout)
    VtvDrawerLayout drawer;

    @BindView(R.id.home_nav_recycler)
    RecyclerView navRecycler;

    @BindView(R.id.home_content_layout)
    LinearLayout homeContentLayout;

    private float lastTranslate = 0.0f;

    public static boolean isCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        EventBus.getDefault().register(this);

        isCreated = true;
        isLoadingBackPressExit = false;

        toolbar.setOnBtnClickListener(this);
        setupNavigation();

        HomeFragment homeFragment = new HomeFragment();
        ActivityUtils.addFragmentToActivity(
                getSupportFragmentManager(), homeFragment, R.id.home_frameLayout);

        HomeServices = new HomeServices(this);
        HomeServices.setHomeMenuResultCallback(this);
        HomeServices.loadHomeMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getInstance().trackScreenView("Home Screen");
    }

    @Override
    protected void onDestroy() {
        HomeServices.cancelLoadMenu();
        EventBus.getDefault().unregister(this);
        isCreated = false;
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        configNavTabetLand(isScreenLand);
        EventBus.getDefault().post(new ConfigurationChangedEvent(isScreenLand));
    }

    @Override
    public void onBackPressed() {
        if (!isScreenLand && drawer.isDrawerOpen(navRecycler)) {
            drawer.closeDrawer(navRecycler);
            return;
        }

        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
            return;
        }

        PopupMessenger popupMessenger = new PopupMessenger(this);
        popupMessenger.setTextContent(R.string.popup_text_exit_app);
        popupMessenger.setTextBtnConfirm(R.string.popup_action_exit);
        popupMessenger.setTextBtnCancel(R.string.popup_action_cancel_exit);
        popupMessenger.setOnPopupMessengerListener(new PopupMessenger.OnPopupMessengerListener() {
            @Override
            public void onPopupMsgBtnConfirmClick() {
                finish();
            }

            @Override
            public void onPopupMsgBtnCancelClick() {

            }
        });
        popupMessenger.show();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventBusFromLoadingAct(LoadingToHomeEvent eventBus) {
        int movId = eventBus.movId;

        if (movId > 0) {
            startActivity(MovieDetailActivity.buildIntent(this, movId));
        }
        Log.d(TAG, "movId = " + movId);
        EventBus.getDefault().removeStickyEvent(LoadingToHomeEvent.class);
    }

    @Subscribe
    public void onEventLoginSuccess(AccountModifyEvent eventBus) {
        navAdapter.notifyAccountChange();
        navAdapter.notifyFollowCountChange();
        toolbar.notifyUserlabelChange();
    }

    @Subscribe
    public void onEventUpdateFollowNotifyCount(FollowNotifyCountEvent eventBus) {
        navAdapter.notifyFollowCountChange();
        toolbar.notifyUserlabelChange();
    }

    @Override
    public void onBtnNavClick() {
        if (drawer.isDrawerOpen(navRecycler)) {
            if (!isScreenLand) drawer.closeDrawer(navRecycler);
        } else {
            drawer.openDrawer(navRecycler);
        }
    }

    @Override
    public void onBtnSearchClick() {
        startActivity(new Intent(HomeActivity.this, SearchActivity.class));
    }

    @Override
    public void onBtnVipClick() {
        if (UserSessionManager.isLogin(getApplicationContext())) {
            startActivity(new Intent(HomeActivity.this, BuyVipActivity.class));
        } else {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        }
    }

    @Override
    public void onBtnUserClick() {
        startActivity(new Intent(HomeActivity.this, PersonalActivity.class));
    }

    @Override
    public void onNavigationItemSelected(int position, String url) {

        if (position == 0) {
            if (!UserSessionManager.isLogin(getApplicationContext())) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            return;
        }

        String p = UriPaser.getNavItemPathSegment(url);
        if (p == null) return;
        if (!isScreenLand) drawer.closeDrawer(navRecycler);

        if (p.equalsIgnoreCase(ApiQuery.PATH_FAVORITE)) {
            // Movies favorite
            LogUtils.d(TAG, "onNavigationItemSelected: path : PATH_FAVORITE");
            startActivity(UserMoviesActivity.buildIntent(this, UserMoviesActivity.PAGE_FAVORITE));
        } else if (p.equalsIgnoreCase(PATH_FOLLOW)) {
            // Movies follow
            LogUtils.d(TAG, "onNavigationItemSelected: path : PATH_FOLLOW");
            startActivity(UserMoviesActivity.buildIntent(this, UserMoviesActivity.PAGE_FOLLOW));
        } else if (p.equalsIgnoreCase("v1")) {
            // Movies Dowload
            showToast(R.string.error_not_been_updated);
        } else if (p.equalsIgnoreCase(ApiQuery.PATH_PHIM_LE) || p.equalsIgnoreCase(ApiQuery.PATH_PHIM_BO)
                || p.equalsIgnoreCase(ApiQuery.PATH_ANIME)) {

            startActivity(MenuMoviesActivity.buildIntent(HomeActivity.this, p, navAdapter.getItemTitle(position)));
        } else {
            startActivity(FilterMoviesActivity.buildIntent(this, url));
        }
    }

    /**
     * Open activity
     *
     * @param bundle data transfer
     */
    private void openFilterMoviesActivity(Bundle bundle) {
        Intent intent = new Intent(HomeActivity.this, FilterMoviesActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * setup navigation menu;
     */
    private void setupNavigation() {
        navRecycler.setHasFixedSize(true);
        navRecycler.setLayoutManager(new LinearLayoutManager(this));
        navAdapter = new HomeNavAdapter(getApplicationContext());
        navAdapter.setOnNavItemSelectedListener(this);
        navRecycler.setAdapter(navAdapter);

        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                if (isScreenLand) return;

                float moveFactor = (navRecycler.getWidth() * slideOffset);
                homeContentLayout.setTranslationX(moveFactor);
            }
        });

        configNavTabetLand(isScreenLand);
    }

    /**
     * setup navigation on tablet;
     *
     * @param isLand is tablet;
     */
    private void configNavTabetLand(boolean isLand) {
        if (!getResources().getBoolean(R.bool.isTablet)) return;
        LogUtils.d(TAG, "configNavTabetLand: isLand = " + isLand);
        if (isLand) {
            int padding = getResources().getDimensionPixelSize(R.dimen.home_nav_size_w);
            drawer.setModeLockOpen(navRecycler);
            homeContentLayout.setPadding(padding, 0, 0, 0);
        } else {
            drawer.disableModeLockOpen(navRecycler);
            homeContentLayout.setPadding(0, 0, 0, 0);
        }
    }

    @Override
    public void onHomeMenuResultSuccess(ApiHomeMenu.Data data) {
        if (data.getMenus() == null || data.getMenus().size() == 0) {
            return;
        }

        for (int i = 0; i < data.getMenus().size(); i++) {
            NavItem navItem = data.getMenus().get(i);
            String p = navItem.getUriLastPathSegment();

            if (p == null) {
            } else if (p.equalsIgnoreCase(ApiQuery.PATH_FAVORITE)) {
                navItem.setType(HomeNavAdapter.TYPE_WITH_ICON);
                navItem.setIconLeftRes(R.drawable.icon_heart3);
            } else if (p.equalsIgnoreCase(PATH_FOLLOW)) {
                navItem.setType(HomeNavAdapter.TYPE_WITH_ICON_LABEL);
                navItem.setIconLeftRes(R.drawable.icon_bell3);
            } else if (p.equalsIgnoreCase("v1")) {
                navItem.setType(HomeNavAdapter.TYPE_WITH_ICON);
                navItem.setIconLeftRes(R.drawable.icon_download2);
            }

            navAdapter.addItem(navItem);

            if ( i == 7 ) {
                navAdapter.addItem(new NavItem(HomeNavAdapter.TYPE_LINE, "", ""));
            }
        }
    }
}