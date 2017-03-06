package com.vungtv.film.widget.sliderview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.vungtv.film.R;
import com.vungtv.film.feature.moviedetail.MovieDetailActivity;
import com.vungtv.film.model.Slider;

public class SliderFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_SLIDER = "ARG_SLIDER";

    private static final String BANNER_MOVIE = "movie";
    private static final String BANNER_ADS = "ads";

    private Slider slider;

    public static SliderFragment instance(Slider slider) {
        SliderFragment fragment = new SliderFragment();
        Bundle bundle = new Bundle();
        if (slider != null) {
            bundle.putParcelable(ARG_SLIDER, slider);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_widget_slider_view, container, false);

        Bundle bundle = getArguments();
        if (bundle == null) {
            return v;
        }
        slider = bundle.getParcelable(ARG_SLIDER);
        if (slider == null) return v;

        ImageView cover = (ImageView) v.findViewById(R.id.item_home_row_slider_cover);
        String urlCover = slider.getBanner();

        if (urlCover != null && urlCover.length() > 5) {
            urlCover = urlCover.replace(" ", "");
            Picasso.with(getActivity().getApplicationContext())
                    .load(urlCover)
                    .fit()
                    .into(cover);
        }

        cover.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        if (slider.getType().equalsIgnoreCase(BANNER_MOVIE)) {
            Intent intent = MovieDetailActivity.getIntentData(getActivity(), slider.getMovId());
            startActivity(intent);
        } else {
            if (slider.getLink() == null || slider.getLink().length() < 5)
                return;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(slider.getLink()));
            startActivity(intent);
        }
    }
}
