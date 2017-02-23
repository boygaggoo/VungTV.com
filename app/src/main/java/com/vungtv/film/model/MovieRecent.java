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
    @SerializedName("mov_episode_id")
    @Expose
    private int movEpisodeId;
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

    public int getMovEpisodeId() {
        return movEpisodeId;
    }

    public void setMovEpisodeId(int movEpisodeId) {
        this.movEpisodeId = movEpisodeId;
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
