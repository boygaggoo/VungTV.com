package com.vungtv.film.widget.player;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.provider.Settings.System;
import android.view.WindowManager;

public class LightnessController {

    // Lấy độ sáng hien tai tra ve gia tri 1-> 100;
    public static int getSystemBright(Context context) {

        float systemBright = 0;
        try {
            systemBright = System.getInt(
                    context.getContentResolver(),
                    System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        systemBright = (systemBright / 255) * 100;
        systemBright = Math.round(systemBright);
        return (int) systemBright;
    }

    // set Gia tri do sang gia tri set 1->100;
    public static void setScreenBright(Activity activity, int value) {
        value = value / 10;
        value = Math.round(value);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        switch (value) {
            case 0:
                lp.screenBrightness = 0.0f;
                break;
            case 1:
                lp.screenBrightness = 0.1f;
                break;
            case 2:
                lp.screenBrightness = 0.2f;
                break;
            case 3:
                lp.screenBrightness = 0.3f;
                break;
            case 4:
                lp.screenBrightness = 0.4f;
                break;
            case 5:
                lp.screenBrightness = 0.5f;
                break;
            case 6:
                lp.screenBrightness = 0.6f;
                break;
            case 7:
                lp.screenBrightness = 0.7f;
                break;
            case 8:
                lp.screenBrightness = 0.8f;
                break;
            case 9:
                lp.screenBrightness = 0.9f;
                break;
            case 10:
                lp.screenBrightness = 1.0f;
                break;
        }

        activity.getWindow().setAttributes(lp);
    }
}
