package com.vungtv.film.feature.search;

import android.os.Bundle;
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
import com.vungtv.film.widget.VtvTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class SearchActivity extends BaseActivity implements SearchContract.View {

    private SearchContract.Presenter presenter;

    private FilterMoviesAdapter adapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        edSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String query = edSearchView.getText().toString();
                    presenter.loadData(query);
                    return true;
                }
                return false;
            }
        });

        new SearchPresenter(this, this);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
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
    public void clearSearchView() {
        edSearchView.setText("");
    }

    @Override
    public void setRecyclerView(final int columNumber, float itemWidth, int itemSpace) {
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
    }

    @Override
    public void showListMovies(boolean show, ArrayList<Movie> listMovies) {
        if (!show) {
            recyclerView.setVisibility(View.GONE);
            return;
        }

        recyclerView.setVisibility(View.VISIBLE);
        adapter.addMultiItem(listMovies);
    }

    @Override
    public void clearListMovies() {
        adapter.clear();
    }

    @Override
    public void selectSearchTypeFilmName() {
        btnSearchWithActor.setPressed(false);
        btnSearchWithFilmName.setPressed(true);
    }

    @Override
    public void selectSearchTypeActorName() {
        btnSearchWithActor.setPressed(true);
        btnSearchWithFilmName.setPressed(false);
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
