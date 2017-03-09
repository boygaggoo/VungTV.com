package com.vungtv.film.feature.recent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;
import com.vungtv.film.feature.moviedetail.MovieDetailActivity;
import com.vungtv.film.feature.player.PlayerActivity;
import com.vungtv.film.interfaces.OnItemClickListener;
import com.vungtv.film.interfaces.OnRecentInfoClickListener;
import com.vungtv.film.model.MovieRecent;
import com.vungtv.film.util.StringUtils;
import com.vungtv.film.widget.VtvAutofitMarginDecoration;
import com.vungtv.film.widget.VtvToolbarSetting;

import java.util.ArrayList;

import butterknife.BindView;

public class RecentActivity extends BaseActivity implements RecentContract.View{

    @BindView(R.id.recent_toolbar)
    VtvToolbarSetting toolBar;

    @BindView(R.id.recent_refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recent_recycler)
    RecyclerView recyclerView;

    @BindView(R.id.text_msg_error)
    TextView tvMsgError;

    private RecentContract.Presenter presenter;

    private RecentRecyclerAdapter adapter;

    public static Intent buildItent(Context context) {
        return new Intent(context, RecentActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);

        toolBar.setOnToolbarListener(new VtvToolbarSetting.OnToolbarListener() {
            @Override
            public void onBtnBackClick() {
                finish();
            }
        });

        refreshLayout.setColorSchemeResources(R.color.green);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.reload();
            }
        });

        recyclerView.addItemDecoration(new VtvAutofitMarginDecoration(this, R.dimen.space_4));
        recyclerView.setHasFixedSize(true);
        adapter = new RecentRecyclerAdapter();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                openActPlayerRecent(adapter.getMovieRecent(pos));
            }
        });
        adapter.setOnRecentInfoClickListener(new OnRecentInfoClickListener() {
            @Override
            public void onButtonInfoClick(int movId) {
                openActMovieDetail(movId);
            }
        });
        recyclerView.setAdapter(adapter);

        new RecentPresenter(this, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void setPresenter(RecentContract.Presenter Presenter) {
        presenter = Presenter;
    }

    @Override
    public void showLoading(boolean show) {
        refreshLayout.setRefreshing(show);
    }

    @Override
    public void showMsgError(boolean show, String error) {

        if (show && !StringUtils.isEmpty(error)) {
            tvMsgError.setText(error);
            tvMsgError.setVisibility(View.VISIBLE);
            return;
        }

        tvMsgError.setVisibility(View.GONE);
    }

    @Override
    public void setListAdapter(ArrayList<MovieRecent> list) {
        adapter.setList(list);
    }

    @Override
    public void openActMovieDetail(int movId) {
        startActivity(MovieDetailActivity.buildIntent(this, movId));
    }

    @Override
    public void openActPlayerRecent(MovieRecent mov) {
        startActivity(PlayerActivity.buildIntentRecent(this, mov.getMovId(), mov.getMovName(), mov.getMovEpsHash()));
    }

    @Override
    public void clearData() {
        adapter.clear();
    }
}
