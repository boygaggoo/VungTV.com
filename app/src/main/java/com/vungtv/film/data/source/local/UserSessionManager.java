package com.vungtv.film.data.source.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.vungtv.film.eventbus.AccountModifyEvent;
import com.vungtv.film.model.User;
import com.vungtv.film.util.LogUtils;
import com.vungtv.film.util.LoginFacebookUtils;
import com.vungtv.film.util.LoginGoogleUtils;
import com.vungtv.film.util.StringUtils;

import org.greenrobot.eventbus.EventBus;

public class UserSessionManager {

    private static final String TAG = UserSessionManager.class.getSimpleName();
    private static final String USER_PREFS = "user_prefs";
    private static final String USER_SESSION = "user_session_key";
    private static final String USER_PROVIDER = "user_provider_key";
    private static final String USER_TOKEN = "user_token_key";
    private static final String DEFAULT_SESSION_VALUE = "No logged in user";
    public static final String PROVIDER_EMAIL = "email";
    public static final String PROVIDER_GOOGLE = "google";
    public static final String PROVIDER_FACE = "facebook";

    /**
     * Kiểm tra đã đăng nhập chưa ?
     *
     * @param context /
     * @return bool
     */
    public static boolean isLogin(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        String provider = preferences.getString(USER_PROVIDER, null);
        return provider != null;
    }

    /**
     * Lấy thông tin user hiện tại;
     *
     * @param context //
     * @return = null => chua dang nhap;
     */
    public static User getCurrentUser(Context context) {
        User user = null;
        SharedPreferences preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        String sessionUser = preferences.getString(USER_SESSION, DEFAULT_SESSION_VALUE);

        if(!sessionUser.equals(DEFAULT_SESSION_VALUE)) {
            try {
                user = new Gson().fromJson(sessionUser, User.class);
            } catch (Exception e) {
                LogUtils.e(TAG, "getCurrentUser: error = " + e.getMessage());
            }
        }

        return user;
    }

    /**
     * Lấy provider của user hiện tại;
     *
     * @param context context;
     * @return string provaider;
     */
    public static String getUserProvider(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        return preferences.getString(USER_PROVIDER, null);
    }

    /**
     * Lấy access token đăng nhập hiện tại;
     *
     * @param context //
     * @return token
     */
    public static String getAccessToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
        return preferences.getString(USER_TOKEN, null);
    }

    /**
     * Set user session when login success;
     *
     * @param context context
     * @param user user object
     * @return success
     */
    public static boolean setUserSession(Context context, User user, String token) {
        SharedPreferences preferences;
        SharedPreferences.Editor editor;

        if(user == null) {
            LogUtils.e(TAG, "setUserSession: error = user is null.");
            return false;
        }

        try {
            preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
            editor = preferences.edit();

            if (StringUtils.isEmpty(user.getProvider())) {
                user.setProvider(PROVIDER_EMAIL);
            }
            editor.putString(USER_TOKEN, token);
            editor.putString(USER_PROVIDER, user.getProvider());

            user.setUserPassword(null);

            String sessionUser = new Gson().toJson(user);
            LogUtils.d(TAG, "setUserSession: session = " + sessionUser);
            editor.putString(USER_SESSION, sessionUser);
            editor.apply();

            return true;
        } catch (Exception e) {
            LogUtils.e(TAG, "setUserSession: error = " + e.getMessage());
            return false;
        }
    }

    /**
     * Lưu lại token sau khi làm mới;
     *
     * @param context /
     * @param token new token
     * @return success;
     */
    public static boolean setAccessToken(Context context, String token) {
        try {
            SharedPreferences preferences =
                    context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(USER_TOKEN, token);
            editor.apply();
            return true;
        } catch (Exception e) {
            LogUtils.e(TAG, "getCurrentUser: error = " + e.getMessage());
        }
        return false;
    }

    /**
     * Cập nhật thông tin user hiện tại;
     *
     * @param context //
     * @param user new info
     * @return success
     */
    public static boolean updateUserSession(Context context, User user) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            user.setUserPassword("");
            String sessionJson = new Gson().toJson(user);

            LogUtils.d(TAG, "updateUserSession: session = " + sessionJson);

            editor.putString(USER_SESSION, sessionJson);
            editor.apply();
            // Put event user modify;
            EventBus.getDefault().post(new AccountModifyEvent());
            return true;
        } catch (Exception e) {
            LogUtils.e(TAG, "updateUserSession: error = " + e.getMessage());
        }

        return false;
    }

    /**
     * Cập nhật tên hiển thị;
     *
     * @param context /
     * @param newName /
     * @return success
     */
    public static boolean updateDisplayName(Context context, String newName) {
        SharedPreferences preferences =
                context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);

        String sessionUser = preferences.getString(USER_SESSION, DEFAULT_SESSION_VALUE);

        if(!sessionUser.equals(DEFAULT_SESSION_VALUE)) {
            try {
                SharedPreferences.Editor editor = preferences.edit();

                User user = new Gson().fromJson(sessionUser, User.class);
                user.setUserDisplayName(newName);
                String newSessionUser = new Gson().toJson(user);

                LogUtils.d(TAG, "setUserSession: session = " + newSessionUser);

                editor.putString(USER_SESSION, newSessionUser);
                editor.apply();
                // Put event user modify;
                EventBus.getDefault().post(new AccountModifyEvent());
                return true;
            } catch (Exception e) {
                LogUtils.e(TAG, "getCurrentUser: error = " + e.getMessage());
            }
        }

        return false;
    }

    public static void logout(Context context, @NonNull LoginGoogleUtils loginGoogleUtils) {

        switch (UserSessionManager.getUserProvider(context.getApplicationContext())) {

            case UserSessionManager.PROVIDER_FACE:
                LoginFacebookUtils.logout(context);
                break;
            case UserSessionManager.PROVIDER_GOOGLE:
                loginGoogleUtils.logout();
                break;
            default:
                UserSessionManager.clearUserSession(context);
                break;
        }
    }

    /**
     * Xóa dữ liệu user khi đăng xuất;
     *
     * @param context /
     * @return success;
     */
    public static boolean clearUserSession(Context context) {
        SharedPreferences preferences;
        SharedPreferences.Editor editor;
        try {
            preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
            editor = preferences.edit();

            editor.remove(USER_PROVIDER);
            editor.remove(USER_TOKEN);
            editor.remove(USER_SESSION);
            editor.apply();

            EventBus.getDefault().post(new AccountModifyEvent());
            return true;
        } catch (Exception e) {
            LogUtils.e(TAG, "clearUserSession: error = " + e.getMessage());
            return false;
        }
    }
}
