package com.vungtv.film.data.source.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vungtv.film.model.NavItem;

import java.util.ArrayList;

/**
 *
 * Created by pc on 3/1/2017.
 */

public class ApiHomeMenu extends ApiModel {

    @SerializedName("data")
    @Expose
    private Data data;


    public Data getData() {
        return data;
    }

    public class Data {

        @SerializedName("limit")
        @Expose
        private int limit;
        @SerializedName("offset")
        @Expose
        private int offset;
        @SerializedName("menus")
        @Expose
        private ArrayList<NavItem> menus = null;


        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public ArrayList<NavItem> getMenus() {
            return menus;
        }

        public void setMenus(ArrayList<NavItem> menus) {
            this.menus = menus;
        }
    }
}
