package com.vungtv.film.data.source.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vungtv.film.model.Video;

import java.util.ArrayList;

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
        public ArrayList<Player> player = null;
        @SerializedName("eps_title")
        @Expose
        public String epsTitle;
        @SerializedName("next")
        @Expose
        public String next;
        @SerializedName("previous")
        @Expose
        public String previous;
        @SerializedName("share_link")
        @Expose
        public String shareLink;
    }

    public class Player {
        @SerializedName("label")
        @Expose
        public String label;
        @SerializedName("files")
        @Expose
        public ArrayList<Video> files = null;

    }
}
