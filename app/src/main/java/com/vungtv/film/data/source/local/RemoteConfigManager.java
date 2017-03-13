package com.vungtv.film.data.source.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.vungtv.film.R;
import com.vungtv.film.model.Config;
import com.vungtv.film.util.LogUtils;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 3/9/2017.
 * Email: vancuong2941989@gmail.com
 */

public class RemoteConfigManager {

    private static final String TAG = RemoteConfigManager.class.getSimpleName();
    private static final String CONFIG_PREFS = "config_prefs";
    private static final String CONFIG_SESSION = "config_session_key";
    private static final String DEFAULT_SESSION_VALUE = "No config app";

    /**
     * Get config app;
     *
     * @param context The (@link Context)
     * @return The {@link Config}
     */
    public static Config getConfigs(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(CONFIG_PREFS, Context.MODE_PRIVATE);
        String sessionUser = preferences.getString(CONFIG_SESSION, DEFAULT_SESSION_VALUE);

        Config config = null;
        if(!sessionUser.equals(DEFAULT_SESSION_VALUE)) {
            try {
                config = new Gson().fromJson(sessionUser, Config.class);
            } catch (Exception e) {
                LogUtils.e(TAG, "Config: error = " + e.getMessage());
            }
        }

        if (config != null) return config;

        return getDefaultConfig(context);
    }

    /**
     * Save config app;
     *
     * @param context The {@link Context}
     * @param config The {@link Config}
     * @return {@link Boolean}
     */
    public static boolean setConfigs(Context context, Config config) {
        SharedPreferences preferences;
        SharedPreferences.Editor editor;

        if(config == null) {
            LogUtils.e(TAG, "setConfigs: error = configs is null.");
            return false;
        }

        try {
            preferences = context.getSharedPreferences(CONFIG_PREFS, Context.MODE_PRIVATE);
            editor = preferences.edit();

            String sessionConfig = new Gson().toJson(config);
            editor.putString(CONFIG_SESSION, sessionConfig);
            editor.apply();
            LogUtils.e(TAG, "setConfigs: success: " + sessionConfig);
            return true;
        } catch (Exception e) {
            LogUtils.e(TAG, "setUserSession: error = " + e.getMessage());
            return false;
        }
    }

    /**
     * Get default config app;
     *
     * @param context The (@link Context)
     * @return The {@link Config}
     */
    private static Config getDefaultConfig(Context context) {
        try {
            String json = context.getResources().getString(R.string.json_config_default);
            return new Gson().fromJson(json, Config.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
