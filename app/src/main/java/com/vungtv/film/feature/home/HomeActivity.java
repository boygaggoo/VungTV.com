package com.vungtv.film.feature.home;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;
import com.vungtv.film.data.source.remote.service.HomeServices;
import com.vungtv.film.eventbus.AccountModifyEvent;
import com.vungtv.film.eventbus.ConfigurationChangedEvent;
import com.vungtv.film.feature.buyvip.BuyVipActivity;
import com.vungtv.film.feature.filtermovies.FilterMoviesActivity;
import com.vungtv.film.feature.home.HomeNavAdapter.OnNavItemSelectedListener;
import com.vungtv.film.feature.login.LoginActivity;
import com.vungtv.film.feature.personal.PersonalActivity;
import com.vungtv.film.feature.search.SearchActivity;
import com.vungtv.film.util.ActivityUtils;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.util.UriPaser;
import com.vungtv.film.widget.VtvDrawerLayout;
import com.vungtv.film.widget.VtvToolbarHome;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

public class HomeActivity extends BaseActivity implements OnNavItemSelectedListener {
    private static final String TAG = HomeActivity.class.getSimpleName();

    private HomeNavAdapter navAdapter;

    @BindView(R.id.home_toolbar)
    VtvToolbarHome toolbar;

    @BindView(R.id.home_drawer_layout)
    VtvDrawerLayout drawer;

    @BindView(R.id.home_nav_recycler)
    RecyclerView navRecycler;

    @BindView(R.id.home_content_layout)
    LinearLayout homeContentLayout;

    private float lastTranslate = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        isLoadingBackPressExit = false;

        EventBus.getDefault().register(this);
        setToolbar();
        setupNavigation();

        HomeFragment homeFragment = new HomeFragment();
        ActivityUtils.addFragmentToActivity(
                getSupportFragmentManager(), homeFragment, R.id.home_frameLayout);

        new HomePresenter(this, homeFragment, new HomeServices(this));
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        configNavTabetLand(isScreenLand);
        EventBus.getDefault().post(new ConfigurationChangedEvent(isScreenLand));
    }

    @Override
    public void onNavigationItemSelected(int position, String url) {
        Bundle bundle = new Bundle();

        if (position == 0) {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        String pathSegment = UriPaser.getNavItemPathSegment(url);
        if (pathSegment == null) return;

        drawer.closeDrawer(navRecycler);


    }

    @Subscribe
    public void onEventLoginSuccess(AccountModifyEvent eventBus) {
        navAdapter.notifyAccountChange();
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
     * set event home toolbar
     */
    private void setToolbar() {
        toolbar.setOnBtnClickListener(new VtvToolbarHome.OnBtnClickListener() {
            @Override
            public void onBtnNavClick() {
                if (drawer.isDrawerOpen(navRecycler)) {
                    drawer.closeDrawer(navRecycler);
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
                startActivity(new Intent(HomeActivity.this, BuyVipActivity.class));
            }

            @Override
            public void onBtnUserClick() {
                startActivity(new Intent(HomeActivity.this, PersonalActivity.class));
            }
        });
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    homeContentLayout.setTranslationX(moveFactor);
                } else {
                    TranslateAnimation anim = new TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f);

                    anim.setDuration(0);
                    anim.setFillAfter(true);
                    homeContentLayout.startAnimation(anim);

                    lastTranslate = moveFactor;
                }
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
}
