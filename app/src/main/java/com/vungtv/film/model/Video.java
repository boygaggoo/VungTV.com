package com.vungtv.film.model;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 3/4/2017.
 * Email: vancuong2941989@gmail.com
 */

public class Video {
    @SerializedName("file")
    @Expose
    private String uri;
    @SerializedName("label")
    @Expose
    private int label;
    @SerializedName("extension")
    @Expose
    private String extension;

    public String getUriString() {
        return uri;
    }

    public Uri getUri() {
        if (uri == null) return null;

        try {
            return Uri.parse(uri);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public String getExtension() {
        if (extension == null || extension.length() < 2) return null;

        switch (extension) {
            case "application/x-mpegURL":
                return "m3u8";
            case "video/mp4":
                return "mp4";
            default: return null;
        }
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
