package com.vungtv.film.feature.menumovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;
import com.vungtv.film.feature.filtermovies.FilterMoviesActivity;
import com.vungtv.film.feature.moviedetail.MovieDetailActivity;
import com.vungtv.film.feature.search.SearchActivity;
import com.vungtv.film.model.Movie;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.util.StringUtils;
import com.vungtv.film.widget.VtvFooterView;
import com.vungtv.film.widget.VtvToolbarPage;
import com.vungtv.film.widget.moviesrowview.MoviesRowAdapter;
import com.vungtv.film.widget.moviesrowview.VtvMoviesRowView;

import java.util.ArrayList;

import butterknife.BindView;

import static com.vungtv.film.R.attr.itemType;

public class MenuMoviesActivity extends BaseActivity implements MenuMoviesContract.View, VtvToolbarPage.OnToolbarPageListener {
    public static final String INTENT_DANHMUC = "INTENT_DANHMUC";
    public static final String INTENT_TITLE = "INTENT_TITLE";
    private static final String TAG = MenuMoviesActivity.class.getSimpleName();

    public static Intent buildIntent(Context context, String danhMuc, String title) {
        Intent intent = new Intent(context, MenuMoviesActivity.class);
        intent.putExtra(INTENT_DANHMUC, danhMuc);
        intent.putExtra(INTENT_TITLE, title);
        return intent;
    }

    private MenuMoviesContract.Presenter presenter;

    @BindView(R.id.menumovies_toolbar)
    VtvToolbarPage toolbar;

    @BindView(R.id.menumovies_layout_content)
    LinearLayout layoutContent;

    @BindView(R.id.text_msg_error)
    TextView textMsgError;

    private VtvFooterView footerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_movies);

        toolbar.setOnToolbarPageListener(this);
        new MenuMoviesPresenter(this, this);
        presenter.getIntent(getIntent());
        presenter.loadContent();
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
    public void showMsgError(boolean show, String err) {
        if (show && !StringUtils.isEmpty(err)) {
            textMsgError.setText(err);
            textMsgError.setVisibility(View.VISIBLE);

            return;
        }

        textMsgError.setVisibility(View.GONE);
    }

    @Override
    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void addAdView(int position) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
        View view = new View(this);
        view.setBackgroundResource(R.color.black_80);
        layoutContent.addView(view, position, layoutParams);
    }

    @Override
    public void addRowMoviesView(int position, String title, ArrayList<Movie> movies, final String urlMore) {
        LogUtils.d(TAG, "addRowMoviesView: urlMore = " + urlMore);
        VtvMoviesRowView moviesRow =
                new VtvMoviesRowView.Builder(this)
                        .setTitle(title)
                        .setListData(itemType, new ArrayList<Object>(movies))
                        .addOnVtvMoviesRowListener(new VtvMoviesRowView.OnVtvMoviesRowListener() {
                            @Override
                            public void onClickViewMore() {
                                openActFilterMovies(urlMore);
                            }
                        })
                        .addOnItemClickListener(new MoviesRowAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int movieId) {
                                openActMovieDetail(movieId);
                            }
                        })
                        .build();
        layoutContent.addView(moviesRow, position);
    }

    @Override
    public void addFooterView() {
        if (footerView == null) {
            footerView = new VtvFooterView(this);
        }
        layoutContent.addView(footerView);
    }

    @Override
    public void openActFilterMovies(String url) {
        startActivity(FilterMoviesActivity.buildIntent(this, url));
    }

    @Override
    public void openActSearch() {
        startActivity(new Intent(this, SearchActivity.class));
    }

    @Override
    public void openActMovieDetail(int movId) {
        startActivity(MovieDetailActivity.buildIntent(this, movId));
    }

    @Override
    public void removeAllViews() {
        layoutContent.removeAllViews();
    }

    @Override
    public void setPresenter(MenuMoviesContract.Presenter Presenter) {
        presenter = Presenter;
    }

    @Override
    public void onBtnBackClick() {
        finish();
    }

    @Override
    public void onSearchClick() {
        openActSearch();
    }

    @Override
    public void onBtnFilterClick() {
        openActFilterMovies(null);
    }
}
