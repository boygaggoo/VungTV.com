package com.vungtv.film.data.source.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vungtv.film.model.Video;

import java.util.List;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 3/4/2017.
 * Email: vancuong2941989@gmail.com
 */

public class ApiMoviePlayer extends ApiModel {

    @SerializedName("data")
    @Expose
    public Data data;

    public class Data {

        @SerializedName("player")
        @Expose
        public Player player;
        @SerializedName("next")
        @Expose
        public String next;
        @SerializedName("previous")
        @Expose
        public String previous;

    }

    public class Player {
        @SerializedName("phu-de")
        @Expose
        public List<Video> phuDe = null;
        @SerializedName("thuyet-minh")
        @Expose
        public List<Video> thuyetMinh = null;

    }
}
