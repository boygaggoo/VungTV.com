package com.vungtv.film.util;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 3/9/2017.
 * Email: vancuong2941989@gmail.com
 */

public class IntentUtils {

    public static Intent openFacebook(PackageManager pm, String url) {
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
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
        return openFacebook(pm, url);
    }

}
