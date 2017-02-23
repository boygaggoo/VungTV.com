package com.vungtv.film.model;

import com.vungtv.film.feature.home.HomeNavAdapter;

public class NavItem {
    private int type = HomeNavAdapter.TYPE_NORMAL;
    private int id;
    private String title;
    private int iconLeftRes;

    public NavItem() {
    }

    public NavItem(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public NavItem(int type, int id, String title) {
        this.type = type;
        this.id = id;
        this.title = title;
    }

    public NavItem(int type, int id, String title, int iconLeftRes) {
        this.type = type;
        this.id = id;
        this.title = title;
        this.iconLeftRes = iconLeftRes;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
