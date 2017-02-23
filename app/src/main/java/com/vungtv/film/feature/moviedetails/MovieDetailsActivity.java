package com.vungtv.film.feature.moviedetails;

import android.content.Intent;
import android.os.Bundle;

import com.vungtv.film.BaseActivity;
import com.vungtv.film.R;

public class MovieDetailsActivity extends BaseActivity {
    private static final String INTENT_MOVIE_ID = "INTENT_MOVIE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
    }

    /**
     * set intent data;
     *
     * @param intent Intent start activity;
     * @param movieId id of movie;
     * @return Intent;
     */
    public static Intent getIntentData(Intent intent, int movieId) {
        intent.putExtra(INTENT_MOVIE_ID, movieId);
        return intent;
    }
}
