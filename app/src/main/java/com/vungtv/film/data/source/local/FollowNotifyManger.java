package com.vungtv.film.data.source.local;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 * Created by pc on 3/15/2017.
 */

public class FollowNotifyManger {

    private static final String TAG = FollowNotifyManger.class.getSimpleName();
    private static final String FOLLOW_PREFS = "follow_prefs";
    private static final String FOLLOW_NOTIFY_NUM = "follow_nnotify_number";


    /**
     *  Get count notify
     *
     * @param context The {@link Context}
     * @return The number notify;
     */
    public static int get(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(FOLLOW_PREFS, Context.MODE_PRIVATE);
        return preferences.getInt(FOLLOW_NOTIFY_NUM, 0);
    }

    /**
     * plus counter notify
     *
     * @param context The {@link Context}
     */
    public static void update(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(FOLLOW_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        int count = get(context) + 1;
        editor.putInt(FOLLOW_NOTIFY_NUM, count);
        editor.apply();
    }

    /**
     * Set count notify = 0;
     *
     * @param context The {@link Context}
     */
    public static void reset(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(FOLLOW_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(FOLLOW_NOTIFY_NUM, 0);
        editor.apply();
    }
}
