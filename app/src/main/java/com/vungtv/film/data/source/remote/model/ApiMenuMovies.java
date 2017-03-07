package com.vungtv.film.data.source.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vungtv.film.model.Movie;

import java.util.ArrayList;

/**
 * @link {MenuMoviesServices.class}
 *
 * Created by pc on 3/7/2017.
 */

public class ApiMenuMovies extends ApiModel {

    @SerializedName("data")
    @Expose
    public Data data;

    public class Data {
        @SerializedName("items")
        @Expose
        public ArrayList<Item> items = null;

    }

    public class Item {

        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("link")
        @Expose
        public String link;
        @SerializedName("items")
        @Expose
        public ArrayList<Movie> movies = null;

    }
}
