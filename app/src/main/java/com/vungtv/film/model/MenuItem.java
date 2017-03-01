package com.vungtv.film.model;

import com.vungtv.film.feature.home.HomeNavAdapter;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 2/28/2017.
 * Email: vancuong2941989@gmail.com
 */

public class MenuItem {
    private int type = HomeNavAdapter.TYPE_NORMAL;
    private int id;
    private String title;
    private int iconLeftRes;

    public MenuItem() {
    }

    public MenuItem(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public MenuItem(int type, int id, String title) {
        this.type = type;
        this.id = id;
        this.title = title;
    }

    public MenuItem(int type, int id, String title, int iconLeftRes) {
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
