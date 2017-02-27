package com.vungtv.film.model;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vungtv.film.feature.home.HomeNavAdapter;
import com.vungtv.film.util.StringUtils;

public class NavItem {
    private int type = HomeNavAdapter.TYPE_NORMAL;
    @SerializedName("label")
    @Expose
    private String title;
    @SerializedName("link")
    @Expose
    public String link;
    private int iconLeftRes;

    public NavItem() {
    }

    public NavItem(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public NavItem(int type, String title, String link) {
        this.type = type;
        this.title = title;
        this.link = link;
    }

    public NavItem(int type, String title, String link, int iconLeftRes) {
        this.type = type;
        this.title = title;
        this.link = link;
        this.iconLeftRes = iconLeftRes;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconLeftRes() {
        return iconLeftRes;
    }

    public void setIconLeftRes(int iconLeftRes) {
        this.iconLeftRes = iconLeftRes;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUriLastPathSegment() {
        if (StringUtils.isEmpty(link))
            return null;

        try {
            Uri uri = Uri.parse(link);
            return uri.getLastPathSegment();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
