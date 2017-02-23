package com.vungtv.film.feature.filtermovies;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;
import com.vungtv.film.data.source.remote.service.FilterMoviesServices;
import com.vungtv.film.interfaces.OnItemClickListener;
import com.vungtv.film.model.Movie;
import com.vungtv.film.popup.PopupMenuSort;
import com.vungtv.film.util.UriPaser;
import com.vungtv.film.widget.GridSpacingItemDecoration;
import com.vungtv.film.widget.LoadmoreScrollListener;
import com.vungtv.film.widget.VtvToolbarPage;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class FilterMoviesActivity extends BaseActivity implements FilterMoviesContract.View{
    private static final String TAG = FilterMoviesActivity.class.getSimpleName();
    public static final String INTENT_DANHMUC = "INTENT_DANHMUC";
    public static final String INTENT_THELOAI = "INTENT_THELOAI";
    public static final String INTENT_QUOCGIA = "INTENT_QUOCGIA";
    public static final String INTENT_NAM = "INTENT_NAM";
    public static final String INTENT_SAPXEP = "INTENT_SAPXEP";
    public static final String INTENT_TOP = "INTENT_TOP";

    private FilterMoviesContract.Presenter presenter;

    private FilterMoviesAdapter adapter;

    private LoadmoreScrollListener loadmoreScrollListener;

    @BindView(R.id.filter_movies_toolbar)
    VtvToolbarPage toolbarPage;

    @BindView(R.id.filter_movies_refresh)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.filter_movies_recycler)
    RecyclerView recyclerView;

    @BindView(R.id.filter_movies_errormsg)
    TextView tvErrorMsg;

    private PopupMenuSort popupMenuSort;

    private int curItemView = 0;

    public static Bundle getBundleData(String uri) {
        Bundle bundle = new Bundle();
        bundle.putString(INTENT_DANHMUC, UriPaser.getDanhMuc(uri));
        bundle.putString(INTENT_THELOAI, UriPaser.getTheLoai(uri));
        bundle.putString(INTENT_QUOCGIA, UriPaser.getQuocGia(uri));
        bundle.putString(INTENT_NAM, UriPaser.getNam(uri));
        bundle.putString(INTENT_SAPXEP, UriPaser.getSapXep(uri));
        bundle.putString(INTENT_TOP, UriPaser.getTop(uri));
        return bundle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_movies);
        ButterKnife.bind(this);
        setViewListener();

        new FilterMoviesPresenter(this, this, new FilterMoviesServices(this));

        presenter.start();
        presenter.getIntentData(getIntent());
        presenter.loadData();
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

    /**
     * Set toolbar event
     */
    private void setViewListener() {
        toolbarPage.setOnToolbarPageListener(new VtvToolbarPage.OnToolbarPageListener() {
            @Override
            public void onBtnBackClick() {
                finish();
            }

            @Override
            public void onSearchClick() {

            }

            @Override
            public void onBtnFilterClick() {
                presenter.filterMoviesClick();
            }
        });
        refreshLayout.setColorSchemeResources(R.color.green);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.reloadData();
            }
        });
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
        if (!show) {
            tvErrorMsg.setVisibility(View.GONE);
            return;
        }
        tvErrorMsg.setText(error);
        tvErrorMsg.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToolbarTitle(String title) {
        toolbarPage.setTitle(title);
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
    public void showPopupFilter(boolean show) {

    }

    @Override
    public void showPopupSort(View view) {
        if (popupMenuSort == null) {
            popupMenuSort = new PopupMenuSort(this, view);
            popupMenuSort.setOnMenuSortListener(new PopupMenuSort.OnMenuSortListener() {
                @Override
                public void onMenuSortItemSelected(String sapXep) {
                    presenter.sapXepMoviesSubmit(sapXep);
                }
            });
        }

        if (popupMenuSort.isShowing()) {
            popupMenuSort.dismiss();
        }else {
            popupMenuSort.show();
        }
    }

    @Override
    public void showActMovieDetails(int movieId) {

    }

    @Override
    public void addItemMovie(ArrayList<Movie> movies) {
        adapter.addMultiItem(movies);
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
    public void addAdsNative(int pos) {
        adapter.addItem(pos, null);
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
    public void setPresenter(FilterMoviesContract.Presenter mPresenter) {
        presenter = checkNotNull(mPresenter);
    }

    @OnClick(R.id.filter_movies_btn_sort)
    public void btnSortClicked(View view) {
        presenter.sapXepMoviesClick(view);
    }
}
