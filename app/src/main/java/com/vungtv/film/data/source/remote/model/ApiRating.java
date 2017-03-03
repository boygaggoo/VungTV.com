package com.vungtv.film.data.source.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vungtv.film.model.RatingMovie;

/**
 * Created by pc on 3/3/2017.
 */

public class ApiRating extends ApiModel {
    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data {
        @SerializedName("result")
        @Expose
        private RatingMovie rating;

        public RatingMovie getRating() {
            return rating;
        }
    }
}
