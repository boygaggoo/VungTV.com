package com.vungtv.film.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 *
 *
 * Created by pc on 2/20/2017.
 */

public class MovieHistory extends RealmObject {
    @SerializedName("mov_id")
    @Expose
    @PrimaryKey
    private int movId;
    @SerializedName("mov_name")
    @Expose
    private String movName;
    @SerializedName("mov_poster")
    @Expose
    private String movPoster;
    @SerializedName("mov_create_at")
    @Expose
    private long movCreateAt;

    public int getMovId() {
        return movId;
    }

    public void setMovId(int movId) {
        this.movId = movId;
    }

    public String getMovName() {
        return movName;
    }

    public void setMovName(String movName) {
        this.movName = movName;
    }

    public String getMovPoster() {
        return movPoster;
    }

    public void setMovPoster(String movPoster) {
        this.movPoster = movPoster;
    }

    public long getMovCreateAt() {
        return movCreateAt;
    }

    public void setMovCreateAt(long movCreateAt) {
        this.movCreateAt = movCreateAt;
    }
}
