package com.vungtv.film.data.source.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vungtv.film.model.Movie;

import java.util.ArrayList;

public class ApiFilterMovies extends ApiModel {
    @SerializedName("data")
    @Expose
    private DataPage dataPage;

    public DataPage getDataPage() {
        return dataPage;
    }

    public void setDataPage(DataPage dataPage) {
        this.dataPage = dataPage;
    }

    public class DataPage {

        @SerializedName("limit")
        @Expose
        private int limit;
        @SerializedName("offset")
        @Expose
        private int offset;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("total")
        @Expose
        private int total;
        @SerializedName("link")
        @Expose
        private String link;
        @SerializedName("previous")
        @Expose
        private String previous;
        @SerializedName("next")
        @Expose
        private String next;
        @SerializedName("items")
        @Expose
        private ArrayList<Movie> movies = null;

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
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

        public ArrayList<Movie> getMovies() {
            return movies;
        }

        public void setMovies(ArrayList<Movie> movies) {
            this.movies = movies;
        }
    }
}
