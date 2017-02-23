package com.vungtv.film.data.source.remote;

import android.content.Context;
import android.support.annotation.StringRes;

import com.vungtv.film.R;

public final class ApiError {
    public static final int OFF_INTERNET = R.string.error_off_network;
    public static final int NO_INTERNET = R.string.error_no_connection;
    public static final int GET_DATA_ERROR = R.string.error_get_data_failed;
    public static final int NO_DATA = R.string.error_no_data;
    public static final int SERVICE_ERROR = R.string.error_service;

    public static String toString(Context context, @StringRes int error) {
        return context.getResources().getString(error);
    }
}
