package com.vungtv.film.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pc on 3/3/2017.
 */

public class Episode implements Parcelable {
    @SerializedName("eps_title")
    @Expose
    private String epsTitle;
    @SerializedName("eps_order")
    @Expose
    private int epsOrder;
    @SerializedName("eps_hash")
    @Expose
    private String epsHash;
    @SerializedName("eps_preview")
    @Expose
    private Boolean epsPreview;

    public Episode() {
    }

    protected Episode(Parcel in) {
        epsTitle = in.readString();
        epsOrder = in.readInt();
        epsHash = in.readString();
    }

    public static final Creator<Episode> CREATOR = new Creator<Episode>() {
        @Override
        public Episode createFromParcel(Parcel in) {
            return new Episode(in);
        }

        @Override
        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(epsTitle);
        parcel.writeInt(epsOrder);
        parcel.writeString(epsHash);
    }

    public String getEpsTitle() {
        return epsTitle;
    }

    public void setEpsTitle(String epsTitle) {
        this.epsTitle = epsTitle;
    }

    public int getEpsOrder() {
        return epsOrder;
    }

    public void setEpsOrder(int epsOrder) {
        this.epsOrder = epsOrder;
    }

    public String getEpsHash() {
        return epsHash;
    }

    public void setEpsHash(String epsHash) {
        this.epsHash = epsHash;
    }

    public Boolean getEpsPreview() {
        return epsPreview;
    }

    public void setEpsPreview(Boolean epsPreview) {
        this.epsPreview = epsPreview;
    }
}
