package com.vungtv.film.data.source.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vungtv.film.model.HotTopic;
import com.vungtv.film.model.Movie;
import com.vungtv.film.model.Slider;

import java.util.ArrayList;

public class ApiHome extends ApiModel {
    @SerializedName("data")
    @Expose
    private DataHome dataHome;

    public DataHome getDataHome() {
        return dataHome;
    }

    public void setDataHome(DataHome dataHome) {
        this.dataHome = dataHome;
    }

    public class DataHome {
        @SerializedName("limit")
        @Expose
        private int limit;
        @SerializedName("offset")
        @Expose
        private int offset;
        @SerializedName("slider")
        @Expose
        private ArrayList<Slider> sliders = null;
        @SerializedName("hot-topic")
        @Expose
        private ArrayList<HotTopic> hotTopic = null;
        @SerializedName("items")
        @Expose
        private ArrayList<ModuleRow> moduleRows = null;

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

        public ArrayList<Slider> getSliders() {
            return sliders;
        }

        public void setSliders(ArrayList<Slider> sliders) {
            this.sliders = sliders;
        }

        public ArrayList<HotTopic> getHotTopic() {
            return hotTopic;
        }

        public void setHotTopic(ArrayList<HotTopic> hotTopic) {
            this.hotTopic = hotTopic;
        }

        public ArrayList<ModuleRow> getModuleRows() {
            return moduleRows;
        }

        public void setModuleRows(ArrayList<ModuleRow> moduleRows) {
            this.moduleRows = moduleRows;
        }
    }

    public class ModuleRow {
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("link")
        @Expose
        private String link;
        @SerializedName("items")
        @Expose
        private ArrayList<Movie> movies;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public ArrayList<Movie> getMovies() {
            return movies;
        }

        public void setMovies(ArrayList<Movie> movies) {
            this.movies = movies;
        }
    }
}
