package com.vungtv.film.data.source.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.vungtv.film.R;

/**
 *
 * Created by pc on 2/6/2017.
 */

public class SettingsManager {
    private static final String TAG = SettingsManager.class.getSimpleName();
    private static final String SETTING_PREFS = "user_prefs";
    private static final String LANGUAGE_KEY = "language_key";
    private static final String DOWN_ON_WIFI_KEY = "download_on_wifi";

    public static final String LANG_VI = "vi";
    public static final String LANG_EN = "en";

    public static String getSettingLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SETTING_PREFS, Context.MODE_PRIVATE);
        return preferences.getString(LANGUAGE_KEY, LANG_VI);
    }

    public static String getTextSettingLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SETTING_PREFS, Context.MODE_PRIVATE);
        String s = preferences.getString(LANGUAGE_KEY, LANG_VI);
        switch (s) {
            case LANG_VI:
                return context.getString(R.string.setting_lang_vi);
            case LANG_EN:
                return context.getString(R.string.setting_lang_en);
            default: return context.getString(R.string.setting_lang_vi);
        }
    }

    public static void saveSettingLanguage(Context context, String lang) {
        SharedPreferences preferences = context.getSharedPreferences(SETTING_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LANGUAGE_KEY, lang);
        editor.apply();
    }

    public static boolean isDownLoadOnWifi(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SETTING_PREFS, Context.MODE_PRIVATE);
        return preferences.getBoolean(DOWN_ON_WIFI_KEY, true);
    }

    public static void saveDownLoadOnWifi(Context context, boolean checked) {
        SharedPreferences preferences = context.getSharedPreferences(SETTING_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(DOWN_ON_WIFI_KEY, checked);
        editor.apply();
    }
}
