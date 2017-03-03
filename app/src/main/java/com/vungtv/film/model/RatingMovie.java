package com.vungtv.film.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pc on 3/3/2017.
 */

public class RatingMovie {
    @SerializedName("total")
    @Expose
    public int total;
    @SerializedName("avg")
    @Expose
    public float avg;
}
