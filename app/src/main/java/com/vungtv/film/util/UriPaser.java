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

    public static String getNavItemPathSegment(String url) {
        if (StringUtils.isEmpty(url)) return null;

        try {
            Uri uri = Uri.parse(url);
            return uri.getLastPathSegment();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getYoutubeVideoId(String url) {
        if (url == null || url.length() < 5) return null;

        Uri uri = Uri.parse(url);
        return uri.getQueryParameter("v");
    }
}
