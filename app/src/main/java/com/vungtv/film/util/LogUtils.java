package com.vungtv.film.util;


import com.vungtv.film.BuildConfig;

public class LogUtils {

    public static void i(String TAG, String mes) {
        if (!BuildConfig.DEBUG) return;
        android.util.Log.i(TAG, "--| " + mes);
    }

    public static void d(String TAG, String mes) {
        if (!BuildConfig.DEBUG) return;
        android.util.Log.d(TAG, "--| " + mes);
    }

    public static void e(String TAG, String mes) {
        if (!BuildConfig.DEBUG) return;
        android.util.Log.e(TAG, "--| " + mes);
    }
}
