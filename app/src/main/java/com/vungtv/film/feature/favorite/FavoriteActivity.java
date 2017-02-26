package com.vungtv.film.feature.favorite;

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
import com.vungtv.film.feature.filtermovies.FilterMoviesActivity;
import com.vungtv.film.feature.filtermovies.FilterMoviesAdapter;
import com.vungtv.film.feature.logout.LogOutActivity;
import com.vungtv.film.feature.search.SearchActivity;
import com.vungtv.film.interfaces.OnItemClickListener;
import com.vungtv.film.model.Movie;
import com.vungtv.film.util.StringUtils;
import com.vungtv.film.widget.GridSpacingItemDecoration;
import com.vungtv.film.widget.LoadmoreScrollListener;
import com.vungtv.film.widget.VtvToolbarPage;

import java.util.ArrayList;

import butterknife.BindView;

public class FavoriteActivity extends BaseActivity implements FavoriteContract.View {

    private FavoriteContract.Presenter presenter;

    private FilterMoviesAdapter adapter;

    private LoadmoreScrollListener loadmoreScrollListener;

    @BindView(R.id.favorite_toolbar)
    VtvToolbarPage toolbar;

    @BindView(R.id.favorite_refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.favorite_recycler)
    RecyclerView recyclerView;

    @BindView(R.id.text_msg_error)
    TextView textMsgError;

    private int curItemView = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        toolbar.setOnToolbarPageListener(new VtvToolbarPage.OnToolbarPageListener() {
            @Override
            public void onBtnBackClick() {
                finish();
            }

            @Override
            public void onSearchClick() {
                startActivity(new Intent(FavoriteActivity.this, SearchActivity.class));
            }

            @Override
            public void onBtnFilterClick() {
                startActivity(new Intent(FavoriteActivity.this, FilterMoviesActivity.class));
            }
        });

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

        new FavoritePresenter(this, this);

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
        presenter.configChange(isScreenLand, adapter.getList());
    }

    @Override
    public void showLoading(boolean show) {
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
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int x = position / (columNumber * rowAdsNumber);
                int y = position % (columNumber * rowAdsNumber);
                if (x == y) {
                    return 3;
                }
                return 1;
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(itemSpace));
        adapter = new FilterMoviesAdapter(this, itemWidth);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                presenter.openMovieDetails(adapter.getItemMovieId(pos));
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
    public void showActMovieDetails(int movieId) {

    }

    @Override
    public void addItemMovie(ArrayList<Movie> movies) {

        adapter.addMultiItem(movies);

        if (adapter.getItemCount() == 0) {
            showMsgError(true, getString(R.string.favorite_text_msg_not_yet));
        } else {
            showMsgError(false, null);
        }
    }

    @Override
    public void setListAdapter(ArrayList<Object> list) {
        adapter.setList(list);
        recyclerView.scrollToPosition(curItemView);
    }

    @Override
    public void addAdsNative() {
        adapter.addItem(null);
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
    public void setPresenter(FavoriteContract.Presenter Presenter) {
        presenter = Presenter;
    }
}
