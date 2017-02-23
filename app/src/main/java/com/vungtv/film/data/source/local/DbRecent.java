package com.vungtv.film.data.source.local;

import com.vungtv.film.model.MovieRecent;

import java.util.ArrayList;

public class DbRecent {

    public ArrayList<MovieRecent> getLisRecent() {
        ArrayList<MovieRecent> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            MovieRecent movieRecent = new MovieRecent();
            movieRecent.setMovId(i);
            movieRecent.setMovName("Phim dang xem " + i);
            movieRecent.setMovFrameBg("http://img.vungtv.com/imgs/2016/12/14/swm.jpg");
            movieRecent.setMovDuration(100);
            movieRecent.setMovLastPlay(70);
            list.add(movieRecent);
        }

        return list;
    }

}
