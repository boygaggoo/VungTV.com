package com.vungtv.film.data.source.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vungtv.film.model.Movie;
import com.vungtv.film.model.RatingMovie;

import java.util.ArrayList;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 3/1/2017.
 * Email: vancuong2941989@gmail.com
 */

public class ApiMovieDetail extends ApiModel{
    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data {
        @SerializedName("movie")
        @Expose
        public Movie movie;
        @SerializedName("relate_movies")
        @Expose
        public ArrayList<Movie> relateMovies = null;
        @SerializedName("rating")
        @Expose
        public RatingMovie rating;
        @SerializedName("movie_user_status")
        @Expose
        public MovieUserStatus movieUserStatus;
    }

    public class MovieUserStatus {
        @SerializedName("follow")
        @Expose
        public Boolean follow;
        @SerializedName("like")
        @Expose
        public Boolean like;

    }
}
