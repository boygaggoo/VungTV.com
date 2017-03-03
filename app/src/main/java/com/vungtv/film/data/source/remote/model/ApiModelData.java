package com.vungtv.film.data.source.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by pc on 3/3/2017.
 */

public class ApiModelData {
    @SerializedName("limit")
    @Expose
    public int limit;
    @SerializedName("offset")
    @Expose
    public int offset;
}
