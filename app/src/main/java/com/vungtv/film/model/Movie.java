package com.vungtv.film.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Movie {
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
    @SerializedName("mov_summary")
    @Expose
    private String movSummary;
    @SerializedName("mov_last_update")
    @Expose
    private String movLastUpdate;
    @SerializedName("mov_name_accent")
    @Expose
    private String movNameAccent;
    @SerializedName("mov_director")
    @Expose
    private String movDirector;
    @SerializedName("mov_writer")
    @Expose
    private String movWriter;
    @SerializedName("mov_year")
    @Expose
    private String movYear;
    @SerializedName("mov_active")
    @Expose
    private int movActive;
    @SerializedName("mov_duration")
    @Expose
    private String movDuration;
    @SerializedName("mov_released_date")
    @Expose
    private long movReleasedDate;
    @SerializedName("mov_trailer")
    @Expose
    private String movTrailer;
    @SerializedName("mov_type_res")
    @Expose
    private String movTypeRes;
    @SerializedName("type_phim")
    @Expose
    private String typePhim;
    @SerializedName("actors")
    @Expose
    private String actors;
    @SerializedName("features")
    @Expose
    private String features;
    @SerializedName("countries")
    @Expose
    private String countries;
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

    public String getMovSummary() {
        return movSummary;
    }

    public void setMovSummary(String movSummary) {
        this.movSummary = movSummary;
    }

    public String getMovLastUpdate() {
        return movLastUpdate;
    }

    public void setMovLastUpdate(String movLastUpdate) {
        this.movLastUpdate = movLastUpdate;
    }

    public String getMovNameAccent() {
        return movNameAccent;
    }

    public void setMovNameAccent(String movNameAccent) {
        this.movNameAccent = movNameAccent;
    }

    public String getMovDirector() {
        return movDirector;
    }

    public void setMovDirector(String movDirector) {
        this.movDirector = movDirector;
    }

    public String getMovWriter() {
        return movWriter;
    }

    public void setMovWriter(String movWriter) {
        this.movWriter = movWriter;
    }

    public String getMovYear() {
        return movYear;
    }

    public void setMovYear(String movYear) {
        this.movYear = movYear;
    }

    public int getMovActive() {
        return movActive;
    }

    public void setMovActive(int movActive) {
        this.movActive = movActive;
    }

    public String getMovDuration() {
        return movDuration;
    }

    public void setMovDuration(String movDuration) {
        this.movDuration = movDuration;
    }

    public long getMovReleasedDate() {
        return movReleasedDate;
    }

    public void setMovReleasedDate(long movReleasedDate) {
        this.movReleasedDate = movReleasedDate;
    }

    public String getMovTrailer() {
        return movTrailer;
    }

    public void setMovTrailer(String movTrailer) {
        this.movTrailer = movTrailer;
    }

    public String getMovTypeRes() {
        return movTypeRes;
    }

    public void setMovTypeRes(String movTypeRes) {
        this.movTypeRes = movTypeRes;
    }

    public String getTypePhim() {
        return typePhim;
    }

    public void setTypePhim(String typePhim) {
        this.typePhim = typePhim;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getCountries() {
        return countries;
    }

    public void setCountries(String countries) {
        this.countries = countries;
    }
}
