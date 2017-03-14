package com.vungtv.film.util;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.vungtv.film.BuildConfig;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 3/9/2017.
 * Email: vancuong2941989@gmail.com
 */

public class IntentUtils {

    public static Intent openFacebook(PackageManager pm, String fbId, String url) {
        Uri uri = null;
        try {
            // Facebook App
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            //ApplicationInfo applicationInfo2 = pm.getApplicationInfo("com.facebook.lite", 0);
            if (applicationInfo.enabled ) {
                uri = Uri.parse("fb://page/" + fbId);
                return new Intent(Intent.ACTION_VIEW, uri);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        try {
            //Facebook lite;
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.lite", 0);
            if (applicationInfo.enabled ) {
                uri = Uri.parse("fb://page/" + fbId);
                return new Intent(Intent.ACTION_VIEW, uri);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        // Facebook web
        return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    }

    public static Intent sendEmail(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        return intent;
    }

    public static Intent sendFbMessenger(PackageManager pm, String fbId, String url) {
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.orca", 0);
            if (applicationInfo.enabled) {
                Uri uri = Uri.parse("fb-messenger://user/" + fbId);
                return new Intent(Intent.ACTION_VIEW, uri);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return openFacebook(pm, fbId, url);
    }

    public static Intent updateAppFromStore() {
        return new Intent(Intent.ACTION_VIEW, Uri.parse
                ("market://details?id=" + BuildConfig.APPLICATION_ID));
    }

    public static Intent share(String shareLink) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareLink);
        return intent;
    }

}
