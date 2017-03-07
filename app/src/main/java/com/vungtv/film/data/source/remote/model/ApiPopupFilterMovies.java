package com.vungtv.film.data.source.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vungtv.film.model.FilterMovieData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr Cuong on 2/24/2017.
 */

public class ApiPopupFilterMovies extends ApiModel {

    private Data data;

    public Data getData() {
        return data;
    }

    public class Data {

        @SerializedName("sap-xep")
        @Expose
        private List<FilterMovieData> sapXep = new ArrayList<>();
        @SerializedName("danh-muc")
        @Expose
        private List<FilterMovieData> danhMuc = new ArrayList<>();
        @SerializedName("quoc-gia")
        @Expose
        private List<FilterMovieData> quocGia = new ArrayList<>();
        @SerializedName("the-loai")
        @Expose
        private List<FilterMovieData> theLoai = new ArrayList<>();
        @SerializedName("nam")
        @Expose
        private List<FilterMovieData> nam = new ArrayList<>();

        public List<FilterMovieData> getSapXep() {
            return sapXep;
        }

        public List<FilterMovieData> getDanhMuc() {
            return danhMuc;
        }

        public List<FilterMovieData> getQuocGia() {
            return quocGia;
        }

        public List<FilterMovieData> getTheLoai() {
            return theLoai;
        }

        public List<FilterMovieData> getNam() {
            return nam;
        }
    }
}
