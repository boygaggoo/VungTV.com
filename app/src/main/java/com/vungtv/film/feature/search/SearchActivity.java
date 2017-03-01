package com.vungtv.film.feature.search;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;
import com.vungtv.film.feature.filtermovies.FilterMoviesAdapter;
import com.vungtv.film.interfaces.OnItemClickListener;
import com.vungtv.film.model.Movie;
import com.vungtv.film.widget.GridSpacingItemDecoration;
import com.vungtv.film.widget.LoadmoreScrollListener;
import com.vungtv.film.widget.VtvTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class SearchActivity extends BaseActivity implements SearchContract.View {

    private static int TEXT_COLOR_BLUE;

    private static int TEXT_COLOR_DARK_3;

    private SearchContract.Presenter presenter;

    private FilterMoviesAdapter adapter;

    private LoadmoreScrollListener loadmoreScrollListener;

    @BindView(R.id.search_searview)
    EditText edSearchView;

    @BindView(R.id.search_btn_film_name)
    VtvTextView btnSearchWithFilmName;

    @BindView(R.id.search_btn_actor)
    VtvTextView btnSearchWithActor;

    @BindView(R.id.search_tv_error)
    TextView tvMsgError;

    @BindView(R.id.search_recyclerview)
    RecyclerView recyclerView;

    int curItemView = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        TEXT_COLOR_BLUE = ResourcesCompat.getColor(getResources(), R.color.green, null);
        TEXT_COLOR_DARK_3 = ResourcesCompat.getColor(getResources(), R.color.text_dark_3, null);

        edSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String query = edSearchView.getText().toString();
                    presenter.startSearch(query);
                    return true;
                }
                return false;
            }
        });

        new SearchPresenter(this, this);
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
    public void showLoadding(boolean show) {
        popupLoading.show(show);
    }

    @Override
    public void showMsgError(boolean show, String error) {
        tvMsgError.setVisibility(show ? View.VISIBLE : View.GONE);

        if (error != null) tvMsgError.setText(error);
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
                presenter.openActMovieDetail(adapter.getItemMovieId(pos));
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
    public void addItemMovie(ArrayList<Movie> movies) {
        adapter.addMultiItem(movies);

        if(adapter.getItemCount() == 0) {
            showMsgError(true, getString(R.string.search_error_msg_no_movie));
        }
    }

    @Override
    public void setListAdapter(ArrayList<Object> list) {
        adapter.setList(list);
        recyclerView.scrollToPosition(curItemView);
    }

    @Override
    public void clearSearchView() {
        edSearchView.setText("");
    }

    @Override
    public void clearListMovies() {
        adapter.clear();
    }

    @Override
    public void selectSearchTypeFilmName() {

        btnSearchWithActor.setBackgroundResource(R.drawable.ds_touchable_bg_gray2);
        btnSearchWithActor.setTextColor(TEXT_COLOR_DARK_3);
        btnSearchWithFilmName.setBackgroundResource(R.drawable.ds_touchable_bg_gray1);
        btnSearchWithFilmName.setTextColor(TEXT_COLOR_BLUE);
    }

    @Override
    public void selectSearchTypeActorName() {
        btnSearchWithFilmName.setBackgroundResource(R.drawable.ds_touchable_bg_gray2);
        btnSearchWithFilmName.setTextColor(TEXT_COLOR_DARK_3);
        btnSearchWithActor.setBackgroundResource(R.drawable.ds_touchable_bg_gray1);
        btnSearchWithActor.setTextColor(TEXT_COLOR_BLUE);
    }

    @Override
    public void openActMovieDetail(int movieId) {

    }

    @Override
    public void setPresenter(SearchContract.Presenter Presenter) {
        this.presenter = checkNotNull(Presenter);
    }

    @OnClick(R.id.search_btn_film_name)
    public void onBtnSearchWithFilmNameClick() {
        presenter.changeSearchType(0);
    }

    @OnClick(R.id.search_btn_actor)
    public void onBtnSearchWithActorClick() {
        presenter.changeSearchType(1);
    }

    @OnClick(R.id.search_btn_clear)
    public void onBtnClearSearchViewClick() {
        presenter.clearSearchView();
    }

    @OnClick(R.id.search_btn_exit)
    public void onBtnExitClick() {
        finish();
    }
}
