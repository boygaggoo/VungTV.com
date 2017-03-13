package com.vungtv.film.util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DeviceUtil {

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    public synchronized static String getId(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = getDeviceId(context);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.apply();
            }
        }
        return uniqueID;
    }

    private static String getDeviceId(Context context) {
        @SuppressLint("HardwareIds")
        String m_szAndroidID = Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID);

        String deviceId =  m_szAndroidID +
                Build.BOARD.length()%10+ Build.BRAND.length()%10 +
                Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
                Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 + Build.TYPE.length()%10 +
                Build.USER.length()%10;

        return hashMd5(deviceId);
    }

    /* Lấy địa chỉ IMEI của phone
    @SuppressLint("HardwareIds")
    public static String getIMEI(Context context){
        TelephonyManager mngr =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return mngr.getDeviceId();
    }*/

    private static String hashMd5(String string) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(string.getBytes(), 0, string.length());
            string = new BigInteger(1, mdEnc.digest()).toString(16);
            while (string.length() < 32) {
                string = "0" + string;
            }
            password = string;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return password;
    }
}
