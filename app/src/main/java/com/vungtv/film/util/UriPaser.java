package com.vungtv.film.util;

import android.net.Uri;

import com.vungtv.film.data.source.remote.ApiQuery;

public class UriPaser {

    public static String getDanhMuc(String url) {
        if (url == null) return "";

        Uri uri = Uri.parse(url);
        return uri.getQueryParameter(ApiQuery.Q_DANH_MUC);
    }

    public static String getTheLoai(String url) {
        if (url == null) return "";

        Uri uri = Uri.parse(url);
        return uri.getQueryParameter(ApiQuery.Q_THE_LOAI);
    }

    public static String getQuocGia(String url) {
        if (url == null) return "";

        Uri uri = Uri.parse(url);
        return uri.getQueryParameter(ApiQuery.Q_QUOC_GIA);
    }

    public static String getNam(String url) {
        if (url == null) return "";

        Uri uri = Uri.parse(url);
        return uri.getQueryParameter(ApiQuery.Q_NAM);
    }

    public static String getSapXep(String url) {
        if (url == null) return "";

        Uri uri = Uri.parse(url);
        return uri.getQueryParameter(ApiQuery.Q_SAP_XEP);
    }

    public static String getTop(String url) {
        if (url == null) return "";

        Uri uri = Uri.parse(url);
        return uri.getQueryParameter(ApiQuery.Q_TOP);
    }
}
