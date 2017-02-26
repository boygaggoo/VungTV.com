package com.vungtv.film.feature.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vungtv.film.R;
import com.vungtv.film.eventbus.ConfigurationChangedEvent;
import com.vungtv.film.feature.filtermovies.FilterMoviesActivity;
import com.vungtv.film.feature.moviedetail.MovieDetailActivity;
import com.vungtv.film.model.Slider;
import com.vungtv.film.popup.PopupLoading;
import com.vungtv.film.widget.VtvErrorMsgView;
import com.vungtv.film.widget.VtvFooterView;
import com.vungtv.film.widget.moviesrowview.MoviesRowAdapter;
import com.vungtv.film.widget.moviesrowview.VtvMoviesRowView;
import com.vungtv.film.widget.sliderview.VtvSliderView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomeContract.View {
    private static final String TAG = HomeFragment.class.getSimpleName();

    private HomeContract.Presenter presenter;

    private PopupLoading popupLoading;

    private VtvErrorMsgView tvErrorMsg;

    private VtvSliderView sliderView;

    private VtvFooterView footerView;

    @BindView(R.id.frag_home_root) LinearLayout containerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);
        EventBus.getDefault().register(this);
        presenter.start();
        return v;
    }

    @Subscribe
    public void onEventBusConfigChange(ConfigurationChangedEvent eventBus) {
        presenter.configChange();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sliderView != null)
            sliderView.startSlider();
    }

    @Override
    public void onPause() {
        if (sliderView != null)
            sliderView.stopSlider();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setPresenter(HomeContract.Presenter Presenters) {
        presenter = checkNotNull(Presenters);;
    }

    @Override
    public void openActMovieDetails(int movieId) {
        Intent intent = new Intent(HomeFragment.this.getActivity(), MovieDetailActivity.class);
        intent = MovieDetailActivity.getIntentData(intent, movieId);
        getActivity().startActivity(intent);
    }

    @Override
    public void openActFilterMovies(String url) {
        if (url == null || url.length() < 5) {
            return;
        }
        Intent intent = new Intent(
                HomeFragment.this.getActivity(), FilterMoviesActivity.class);
        intent.putExtras(FilterMoviesActivity.getBundleData(url));
        getActivity().startActivity(intent);
    }

    @Override
    public void showLoading(boolean show) {
        if (popupLoading == null) {
            popupLoading = new PopupLoading(getActivity());
            popupLoading.setOnPopupLoadingListener(new PopupLoading.OnPopupLoadingListener() {
                @Override
                public void onBackPressed() {
                    getActivity().finish();
                }
            });
        }
        if (show) {
            popupLoading.show();
        } else {
            popupLoading.dismiss();
        }
    }

    @Override
    public void showMsgError(boolean show, String error) {
        if (!show) return;
        if (tvErrorMsg == null) {
            tvErrorMsg = new VtvErrorMsgView(getActivity());
            tvErrorMsg.setTextColorWhite();
            tvErrorMsg.setWidthHeight((ViewGroup) containerLayout.getParent());
            tvErrorMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.reloadData();
                }
            });
        }
        containerLayout.removeAllViews();
        containerLayout.addView(tvErrorMsg, 0);
        tvErrorMsg.setText(error);
    }

    @Override
    public void addSliderView(ArrayList<Slider> listSlider) {
        if (sliderView != null) sliderView.stopSlider();
        sliderView = new VtvSliderView(getActivity());
        sliderView.setupAdapter(getActivity().getSupportFragmentManager());
        sliderView.changeConfigOrientation();
        sliderView.setData(listSlider);
        containerLayout.addView(sliderView, 0);
        sliderView.startSlider();
    }

    @Override
    public void addReCentView(int style, int itemType, int iconRes, String title, ArrayList<Object> list) {
        VtvMoviesRowView moviesRecent =
                new VtvMoviesRowView.Builder(getActivity())
                        .setStyle(style)
                        .setIconTitle(iconRes)
                        .setTitle(title)
                        .setListData(itemType, list)
                        .build();

        moviesRecent.setOnRecentInfoClick(new MoviesRowAdapter.OnRecentInfoClick() {
            @Override
            public void onClickBtnInfo(int movId) {

            }
        });

        containerLayout.addView(moviesRecent, 2);
    }

    @Override
    public void addHotTopicView(int style, int itemType, final String url, ArrayList<Object> list) {
        VtvMoviesRowView hotTopic =
                new VtvMoviesRowView.Builder(getActivity())
                        .setStyle(style)
                        .setListData(itemType, list)
                        .addOnVtvMoviesRowListener(new VtvMoviesRowView.OnVtvMoviesRowListener() {
                            @Override
                            public void onClickViewMore() {
                                presenter.openActFilterMovies(url);
                            }
                        })
                        .build();
        containerLayout.addView(hotTopic, 7);
    }

    @Override
    public void addMoviesView(int style, int itemType, int iconRes, String title, final String url, ArrayList<Object> list) {
        VtvMoviesRowView moviesRow =
                new VtvMoviesRowView.Builder(getActivity())
                        .setStyle(style)
                        .setIconTitle(iconRes)
                        .setTitle(title)
                        .setListData(itemType, list)
                        .addOnVtvMoviesRowListener(new VtvMoviesRowView.OnVtvMoviesRowListener() {
                            @Override
                            public void onClickViewMore() {
                                presenter.openActFilterMovies(url);
                            }
                        })
                        .addOnItemClickListener(new MoviesRowAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int movieId) {
                                presenter.openActMovieDetails(movieId);
                            }
                        })
                        .build();
        containerLayout.addView(moviesRow);
    }

    @Override
    public void addFooterView() {
        if (footerView == null) {
            footerView = new VtvFooterView(getActivity());
        }
        containerLayout.addView(footerView);
    }

    @Override
    public void removeAllViews() {
        containerLayout.removeAllViews();
    }


}
