package com.vungtv.film.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Movie implements Parcelable {
    @SerializedName("mov_id")
    @Expose
    private int movId;
    @SerializedName("mov_name")
    @Expose
    private String movName;
    @SerializedName("mov_score")
    @Expose
    private String movScore;
    @SerializedName("mov_poster")
    @Expose
    private String movPoster;
    @SerializedName("mov_frame_bg")
    @Expose
    private String movFrameBg;
    @SerializedName("mov_number_episode")
    @Expose
    private int movNumberEpisode;
    @SerializedName("mov_count_episoder")
    @Expose
    private int movCountEpisoder;
    @SerializedName("mov_series")
    @Expose
    private int movSeries;
    @SerializedName("mov_cover")
    @Expose
    private String movCover;
    @SerializedName("tm")
    @Expose
    private String tm;
    @SerializedName("lt")
    @Expose
    private String lt;
    @SerializedName("res")
    @Expose
    private String res;

    public Movie() {
    }

    protected Movie(Parcel in) {
        movName = in.readString();
        movId = in.readInt();
        movScore = in.readString();
        movPoster = in.readString();
        movFrameBg = in.readString();
        movNumberEpisode = in.readInt();
        movCountEpisoder = in.readInt();
        movSeries = in.readInt();
        movCover = in.readString();
        tm = in.readString();
        lt = in.readString();
        res = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(movName);
        parcel.writeInt(movId);
        parcel.writeString(movScore);
        parcel.writeString(movPoster);
        parcel.writeString(movFrameBg);
        parcel.writeInt(movNumberEpisode);
        parcel.writeInt(movCountEpisoder);
        parcel.writeInt(movSeries);
        parcel.writeString(movCover);
        parcel.writeString(tm);
        parcel.writeString(lt);
        parcel.writeString(res);
    }

    public String getMovName() {
        return movName;
    }

    public void setMovName(String movName) {
        this.movName = movName;
    }

    public int getMovId() {
        return movId;
    }

    public void setMovId(int movId) {
        this.movId = movId;
    }

    public String getMovScore() {
        return movScore;
    }

    public void setMovScore(String movScore) {
        this.movScore = movScore;
    }

    public String getMovPoster() {
        return movPoster;
    }

    public void setMovPoster(String movPoster) {
        this.movPoster = movPoster;
    }

    public String getMovFrameBg() {
        return movFrameBg;
    }

    public void setMovFrameBg(String movFrameBg) {
        this.movFrameBg = movFrameBg;
    }

    public int getMovNumberEpisode() {
        return movNumberEpisode;
    }

    public void setMovNumberEpisode(int movNumberEpisode) {
        this.movNumberEpisode = movNumberEpisode;
    }

    public int getMovCountEpisoder() {
        return movCountEpisoder;
    }

    public void setMovCountEpisoder(int movCountEpisoder) {
        this.movCountEpisoder = movCountEpisoder;
    }

    public int getMovSeries() {
        return movSeries;
    }

    public void setMovSeries(int movSeries) {
        this.movSeries = movSeries;
    }

    public String getMovCover() {
        return movCover;
    }

    public void setMovCover(String movCover) {
        this.movCover = movCover;
    }

    public String getTm() {
        return tm;
    }

    public void setTm(String tm) {
        this.tm = tm;
    }

    public String getLt() {
        return lt;
    }

    public void setLt(String lt) {
        this.lt = lt;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }
}
