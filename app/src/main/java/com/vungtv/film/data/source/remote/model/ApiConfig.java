package com.vungtv.film.data.source.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vungtv.film.model.Config;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 3/13/2017.
 * Email: vancuong2941989@gmail.com
 */

public class ApiConfig extends ApiModel{

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data {
        @SerializedName("config")
        @Expose
        private Config config;

        public Config getConfig() {
            return config;
        }
    }
}
