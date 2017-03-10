package com.vungtv.film.feature.usermovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;
import com.vungtv.film.feature.filtermovies.FilterMoviesAdapter;
import com.vungtv.film.feature.logout.LogOutActivity;
import com.vungtv.film.feature.moviedetail.MovieDetailActivity;
import com.vungtv.film.interfaces.OnItemClickListener;
import com.vungtv.film.model.Movie;
import com.vungtv.film.util.StringUtils;
import com.vungtv.film.widget.GridSpacingItemDecoration;
import com.vungtv.film.widget.LoadmoreScrollListener;
import com.vungtv.film.widget.VtvToolbarSetting;

import java.util.ArrayList;

import butterknife.BindView;

public class UserMoviesActivity extends BaseActivity implements UserMoviesContract.View, VtvToolbarSetting.OnToolbarListener {

    public static final String INTENT_PAGE = "INTENT_PAGE";
    public static final int PAGE_FAVORITE = 0;
    public static final int PAGE_FOLLOW = 1;

    public static Intent buildIntent(Context context, int pageType) {
        Intent intent = new Intent(context, UserMoviesActivity.class);
        intent.putExtra(INTENT_PAGE, pageType);
        return intent;
    }

    private UserMoviesContract.Presenter presenter;

    private FilterMoviesAdapter adapter;

    private LoadmoreScrollListener loadmoreScrollListener;

    @BindView(R.id.user_mov_toolbar)
    VtvToolbarSetting toolbar;

    @BindView(R.id.user_mov_refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.user_mov_recycler)
    RecyclerView recyclerView;

    @BindView(R.id.text_msg_error)
    TextView textMsgError;

    private int curItemView = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_movies);

        toolbar.setOnToolbarListener(this);

        refreshLayout.setColorSchemeResources(R.color.green);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.reloadData();
            }
        });

        textMsgError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.reloadData();
            }
        });

        new UserMoviesPresenter(this, this);
        presenter.getIntent(getIntent());
        presenter.start();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        curItemView = ((GridLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        presenter.configChange(isScreenLand);
    }

    @Override
    public void showLoading(boolean show) {
        if (popupLoading == null) return;
        if (!show) {
            popupLoading.dismiss();
            refreshLayout.setRefreshing(false);
            return;
        }

        if (adapter.getItemCount() > 5) {
            refreshLayout.setRefreshing(true);
        } else {
            popupLoading.show();
        }
    }

    @Override
    public void showMsgError(boolean show, String error) {

        if (show && !StringUtils.isEmpty(error)) {
            textMsgError.setText(error);
            textMsgError.setVisibility(View.VISIBLE);
            return;
        }

        textMsgError.setVisibility(View.GONE);
    }

    @Override
    public void showRecyclerView(final int columNumber, final int rowAdsNumber, float itemWidth, int itemSpace) {
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), columNumber);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(itemSpace));
        adapter = new FilterMoviesAdapter(this, itemWidth);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                openMovieDetails(adapter.getItemMovieId(pos));
            }
        });
        if (loadmoreScrollListener != null) {
            recyclerView.removeOnScrollListener(loadmoreScrollListener);
            loadmoreScrollListener = null;
        }

        loadmoreScrollListener = new LoadmoreScrollListener(layoutManager) {
            @Override
            public void onLoadmore() {
                presenter.loadMore();
            }
        };

        recyclerView.addOnScrollListener(loadmoreScrollListener);
    }

    @Override
    public void updateRecyclerView(final int columNumber, final int rowAdsNumber, float itemWidth) {
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), columNumber);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setItemWidth(itemWidth);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addItemMovie(ArrayList<Movie> movies) {
        adapter.addMultiItem(movies);
    }

    @Override
    public void setTitlePage(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void openMovieDetails(int movId) {
        startActivity(MovieDetailActivity.buildIntent(this, movId));
    }

    @Override
    public void clearData() {
        adapter.clear();
    }

    @Override
    public void disableRefresing() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void logOutAccount() {
        startActivity(new Intent(this, LogOutActivity.class));
    }

    @Override
    public void setPresenter(UserMoviesContract.Presenter Presenter) {
        presenter = Presenter;
    }

    @Override
    public void onBtnBackClick() {
        finish();
    }
}
