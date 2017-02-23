package com.vungtv.film.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Slider implements Parcelable {
    @SerializedName("mov_id")
    @Expose
    private Integer movId;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("banner")
    @Expose
    private String banner;

    public Slider() {
    }

    public Slider(Integer movId, String link, String type, String banner) {
        this.movId = movId;
        this.link = link;
        this.type = type;
        this.banner = banner;
    }

    protected Slider(Parcel in) {
        link = in.readString();
        type = in.readString();
        banner = in.readString();
    }

    public static final Creator<Slider> CREATOR = new Creator<Slider>() {
        @Override
        public Slider createFromParcel(Parcel in) {
            return new Slider(in);
        }

        @Override
        public Slider[] newArray(int size) {
            return new Slider[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(link);
        parcel.writeString(type);
        parcel.writeString(banner);
    }

    public Integer getMovId() {
        return movId;
    }

    public void setMovId(Integer movId) {
        this.movId = movId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }
}