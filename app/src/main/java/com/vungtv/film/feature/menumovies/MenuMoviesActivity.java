package com.vungtv.film.feature.menumovies;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;
import com.vungtv.film.model.Movie;
import com.vungtv.film.util.StringUtils;
import com.vungtv.film.widget.VtvFooterView;
import com.vungtv.film.widget.VtvToolbarPage;
import com.vungtv.film.widget.moviesrowview.MoviesRowAdapter;
import com.vungtv.film.widget.moviesrowview.VtvMoviesRowView;

import java.util.ArrayList;

import butterknife.BindView;

import static com.vungtv.film.R.attr.itemType;

public class MenuMoviesActivity extends BaseActivity implements MenuMoviesContract.View {
    public static final String INTENT_DANHMUC = "INTENT_DANHMUC";

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
    }

    @Override
    protected void onDestroy() {
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
    public void addAdView(int position) {

    }

    @Override
    public void addRowMoviesView(int position, String title, ArrayList<Movie> movies) {

        VtvMoviesRowView moviesRow =
                new VtvMoviesRowView.Builder(this)
                        .setTitle(title)
                        .setListData(itemType, new ArrayList<Object>(movies))
                        .addOnVtvMoviesRowListener(new VtvMoviesRowView.OnVtvMoviesRowListener() {
                            @Override
                            public void onClickViewMore() {
                                //presenter.openActFilterMovies(url);
                            }
                        })
                        .addOnItemClickListener(new MoviesRowAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int movieId) {
                                //presenter.openActMovieDetail(movieId);
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
    public void openActFilterMovies() {

    }

    @Override
    public void openActSearch() {

    }

    @Override
    public void setPresenter(MenuMoviesContract.Presenter Presenter) {

    }
}
