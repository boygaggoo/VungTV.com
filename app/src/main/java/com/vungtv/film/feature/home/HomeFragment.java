package com.vungtv.film.feature.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vungtv.film.R;
import com.vungtv.film.data.source.local.RemoteConfigManager;
import com.vungtv.film.eventbus.ConfigurationChangedEvent;
import com.vungtv.film.eventbus.RecentModifyEvent;
import com.vungtv.film.feature.filtermovies.FilterMoviesActivity;
import com.vungtv.film.feature.moviedetail.MovieDetailActivity;
import com.vungtv.film.feature.player.PlayerActivity;
import com.vungtv.film.feature.recent.RecentActivity;
import com.vungtv.film.feature.request.RequestActivity;
import com.vungtv.film.interfaces.OnRecentInfoClickListener;
import com.vungtv.film.model.Config;
import com.vungtv.film.model.MovieRecent;
import com.vungtv.film.model.Slider;
import com.vungtv.film.popup.PopupLoading;
import com.vungtv.film.util.IntentUtils;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.widget.VtvErrorMsgView;
import com.vungtv.film.widget.VtvFooterView;
import com.vungtv.film.widget.moviesrowview.MoviesRowAdapter;
import com.vungtv.film.widget.moviesrowview.VtvMovieRowView;
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

    private VtvMovieRowView moviesRecent;

    @BindView(R.id.frag_home_root) LinearLayout containerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);
        EventBus.getDefault().register(this);
        return v;
    }

    @Subscribe
    public void onEventBusConfigChange(ConfigurationChangedEvent eventBus) {
        presenter.configChange();
    }

    @Subscribe
    public void onEventRecentModify(RecentModifyEvent eventBus) {
        presenter.reloadRecentMovies();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (containerLayout.getChildCount() == 0) {
            presenter.start();
        }
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
        EventBus.getDefault().unregister(this);
        presenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void setPresenter(HomeContract.Presenter Presenter) {
        presenter = checkNotNull(Presenter);;
    }

    @Override
    public void openActMovieDetail(int movieId) {
        getActivity().startActivity(MovieDetailActivity.buildIntent(this.getActivity(), movieId));
    }

    @Override
    public void openActFilterMovies(String url) {
        if (url == null || url.length() < 5) {
            return;
        }

        getActivity().startActivity(FilterMoviesActivity.buildIntent(getActivity(), url));
    }

    @Override
    public void openActRecentMovies() {
        getActivity().startActivity(RecentActivity.buildItent(HomeFragment.this.getActivity()));
    }

    @Override
    public void openActPlayer(MovieRecent mov) {
        getActivity().startActivity(PlayerActivity.buildIntentRecent(
                getActivity(), mov.getMovId(), mov.getMovName(), mov.getMovFrameBg(), mov.getMovEpsHash()));
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
    public void addReCentView(final ArrayList<Object> list) {
        moviesRecent = new VtvMovieRowView(
                getActivity(), VtvMovieRowView.STYLE_DEFAULT, MoviesRowAdapter.ITEM_RECENT);
        moviesRecent.setTitle(R.string.recent_text_title);
        moviesRecent.setIconTitle(R.drawable.icon_lavung);
        moviesRecent.setListAdapter(list);
        moviesRecent.setOnViewMoreListener(new VtvMovieRowView.OnViewMoreListener() {
            @Override
            public void onViewMoreClick() {
                openActRecentMovies();
            }
        });
        moviesRecent.setOnItemClickListener(new MoviesRowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                openActPlayer((MovieRecent) list.get(pos));
            }
        });
        moviesRecent.setOnRecentInfoListener(new OnRecentInfoClickListener() {
            @Override
            public void onButtonInfoClick(int movId) {
                openActMovieDetail(movId);
            }
        });
        containerLayout.addView(moviesRecent, 2);
    }

    @Override
    public void addHotTopicView(int style, int itemType, final String url, ArrayList<Object> list) {
        VtvMovieRowView hotTopic = new VtvMovieRowView(getActivity(), style, itemType);
        hotTopic.setListAdapter(list);
        hotTopic.setOnItemClickListener(new MoviesRowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int movieId) {
                openActMovieDetail(movieId);
            }
        });
        containerLayout.addView(hotTopic, 7);
    }

    @Override
    public void addMoviesView(int style, int itemType, int iconRes, String title, final String url, ArrayList<Object> list) {

        VtvMovieRowView moviesRow = new VtvMovieRowView(getActivity(), style, itemType);
        moviesRow.setTitle(title);
        moviesRow.setIconTitle(iconRes);
        moviesRow.setListAdapter(list);
        moviesRow.setOnViewMoreListener(new VtvMovieRowView.OnViewMoreListener() {
            @Override
            public void onViewMoreClick() {
                openActFilterMovies(url);
            }
        });
        moviesRow.setOnItemClickListener(new MoviesRowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int movieId) {
                openActMovieDetail(movieId);
            }
        });

        containerLayout.addView(moviesRow);
    }

    @Override
    public void addFooterView() {
        if (footerView == null) {
            footerView = new VtvFooterView(getActivity());
            footerView.setOnFooterViewListener(new VtvFooterView.OnFooterViewListener() {
                @Override
                public void onSendMessenge() {
                    Config config = RemoteConfigManager.getConfigs(getActivity());
                    if (config != null) {
                        Intent intent = IntentUtils.sendFbMessenger(
                                getContext().getPackageManager(),
                                config.getFanPageId(),
                                config.getFanPage()
                        );
                        startActivity(intent);
                    }
                }

                @Override
                public void onOpenFanpage() {
                    Config config = RemoteConfigManager.getConfigs(getActivity());
                    if (config != null) {
                        Intent intent = IntentUtils.openFacebook(
                                getContext().getPackageManager(),
                                config.getFanPageId(),
                                config.getFanPage()
                        );
                        startActivity(intent);
                    }
                }

                @Override
                public void onSendRequest() {
                    startActivity(new Intent(HomeFragment.this.getActivity(), RequestActivity.class));
                }

                @Override
                public void onSendReport() {
                    Config config = RemoteConfigManager.getConfigs(getActivity());
                    if (config != null) {
                        Intent intent = IntentUtils.sendEmail(
                                config.getEmail()
                        );
                        startActivity(intent);
                    }
                }
            });
        }
        containerLayout.addView(footerView);
    }

    @Override
    public void updateRecentView(ArrayList<Object> list) {
        LogUtils.d(TAG, "updateRecentView");
        if (containerLayout.getChildAt(2) == moviesRecent) {
            if (list.size() == 0) {
                containerLayout.removeView(moviesRecent);
            } else {
                moviesRecent.setListAdapter(list);
            }
        } else {
            addReCentView(list);
        }

    }

    @Override
    public void removeAllViews() {
        containerLayout.removeAllViews();
    }


}
