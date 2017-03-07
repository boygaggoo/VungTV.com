package com.vungtv.film.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class MovieRecent extends RealmObject {
    @SerializedName("mov_id")
    @Expose
    @PrimaryKey
    private int movId;
    @SerializedName("mov_name")
    @Expose
    private String movName;
    @SerializedName("mov_frame_bg")
    @Expose
    private String movFrameBg;
    @SerializedName("mov_eps_hash")
    @Expose
    private String movEpsHash;
    @SerializedName("mov_duration")
    @Expose
    private long movDuration;
    @SerializedName("mov_last_play")
    @Expose
    private long movLastPlay;
    @SerializedName("mov_create_at")
    @Expose
    private long movCreateAt;

    public MovieRecent() {
    }

    public MovieRecent(int movId, String movName, String movFrameBg, String movEpsHash, long movDuration, long movLastPlay, long movCreateAt) {
        this.movId = movId;
        this.movName = movName;
        this.movFrameBg = movFrameBg;
        this.movEpsHash = movEpsHash;
        this.movDuration = movDuration;
        this.movLastPlay = movLastPlay;
        this.movCreateAt = movCreateAt;
    }

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

    public String getMovFrameBg() {
        return movFrameBg;
    }

    public void setMovFrameBg(String movFrameBg) {
        this.movFrameBg = movFrameBg;
    }

    public String getMovEpsHash() {
        return movEpsHash;
    }

    public void setMovEpsHash(String movEpsHash) {
        this.movEpsHash = movEpsHash;
    }

    public long getMovDuration() {
        return movDuration;
    }

    public void setMovDuration(long movDuration) {
        this.movDuration = movDuration;
    }

    public long getMovLastPlay() {
        return movLastPlay;
    }

    public void setMovLastPlay(long movLastPlay) {
        this.movLastPlay = movLastPlay;
    }

    public long getMovCreateAt() {
        return movCreateAt;
    }

    public void setMovCreateAt(long movCreateAt) {
        this.movCreateAt = movCreateAt;
    }
}
