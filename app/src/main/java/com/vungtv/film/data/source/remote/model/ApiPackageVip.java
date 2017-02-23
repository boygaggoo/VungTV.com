package com.vungtv.film.data.source.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vungtv.film.model.PackageVip;

import java.util.ArrayList;

/**
 *
 * Created by pc on 2/21/2017.
 */

public class ApiPackageVip extends ApiModel {

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
        @SerializedName("user_balance")
        @Expose
        private int userBalance;
        @SerializedName("packages")
        @Expose
        private ArrayList<PackageVip> packages = null;

        public int getLimit() {
            return limit;
        }

        public int getOffset() {
            return offset;
        }

        public int getUserBalance() {
            return userBalance;
        }

        public ArrayList<PackageVip> getPackages() {
            return packages;
        }
    }
}
