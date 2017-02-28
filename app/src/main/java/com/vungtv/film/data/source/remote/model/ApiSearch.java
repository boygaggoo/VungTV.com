package com.vungtv.film.data.source.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vungtv.film.model.Movie;

import java.util.List;

/**
 *
 * Created by pc on 2/28/2017.
 */

public class ApiSearch extends ApiModel {

    @SerializedName("data")
    @Expose
    public Data data;

    public Data getData() {
        return data;
    }

    public class Data {

        @SerializedName("limit")
        @Expose
        public int limit;
        @SerializedName("offset")
        @Expose
        public int offset;
        @SerializedName("previous")
        @Expose
        public String previous;
        @SerializedName("next")
        @Expose
        public String next;
        @SerializedName("movies")
        @Expose
        public List<Movie> movies = null;

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public String getPrevious() {
            return previous;
        }

        public void setPrevious(String previous) {
            this.previous = previous;
        }

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public List<Movie> getMovies() {
            return movies;
        }

        public void setMovies(List<Movie> movies) {
            this.movies = movies;
        }
    }
}
