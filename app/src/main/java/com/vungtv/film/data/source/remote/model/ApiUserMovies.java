package com.vungtv.film.data.source.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vungtv.film.model.Movie;

import java.util.ArrayList;

/**
 *
 * Created by pc on 3/7/2017.
 */

public class ApiUserMovies extends ApiModel {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data extends ApiModelData {
        @Expose
        private int total;
        @SerializedName("previous")
        @Expose
        private String previous;
        @SerializedName("next")
        @Expose
        private String next;
        @SerializedName("movies")
        @Expose
        private ArrayList<Movie> movies = null;

        public ArrayList<Movie> getMovies() {
            return movies;
        }

        public int getTotal() {
            return total;
        }
    }
}
