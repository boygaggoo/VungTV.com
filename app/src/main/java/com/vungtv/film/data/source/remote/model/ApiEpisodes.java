package com.vungtv.film.data.source.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vungtv.film.model.Episode;

import java.util.ArrayList;

/**
 * Created by pc on 3/3/2017.
 */

public class ApiEpisodes extends ApiModel {
    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data extends ApiModelData {
        @SerializedName("episodes")
        @Expose
        private ArrayList<Episode> episodes = null;

        public ArrayList<Episode> getEpisodes() {
            return episodes;
        }
    }
}
