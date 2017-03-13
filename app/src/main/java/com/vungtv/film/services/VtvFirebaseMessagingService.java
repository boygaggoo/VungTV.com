package com.vungtv.film.services;

import android.app.LauncherActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vungtv.film.R;
import com.vungtv.film.feature.loading.LoadingActivity;
import com.vungtv.film.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class VtvFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    public static final int NOTIFICATION_ID = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        LogUtils.i(TAG, "onMessageReceived From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            if (remoteMessage.getFrom().equals("/topics/animeviet_news")) {

                String mes = remoteMessage.getData().get("mes");
                sendNewsNotification(mes);

                LogUtils.d(TAG, "onMessageReceived Message: " +mes);
            } else if (remoteMessage.getData().get("mov_id") != null){

                sendMovieNotification(
                        Integer.parseInt(remoteMessage.getData().get("mov_id")),
                        remoteMessage.getData().get("title"),
                        remoteMessage.getData().get("mov_poster"),
                        remoteMessage.getData().get("mes")
                );

                if (remoteMessage.getFrom().equals("/topics/follow_" + remoteMessage.getData().get("mov_id"))) {
                    //new DbTableFollow(getApplicationContext())
                    //        .updateStatus(Integer.parseInt(remoteMessage.getData().get("m_id")), 1);
                }
            }
            Log.d(TAG, "onMessageReceived Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, " * Message Notification Body: " + remoteMessage.getNotification().getBody());
            String messengerBody = remoteMessage.getNotification().getBody();
            sendNewsNotification(messengerBody);
        }

    }

    /**
     * Create a new notification;
     *
     * @param message {@link String}
     */
    private void sendNewsNotification(String message) {
        //if (!RemoteConfig.getNotificationOn(getApplicationContext())) return;

        Intent intent = new Intent(this, LauncherActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(getBitmapLageIcon())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param message string
     */
    private void sendMovieNotification(int mId, String title, String imgUrl, String message) {

        //if (!RemoteConfig.getNotificationOn(getApplicationContext())) return;

        Intent intent = new Intent(this, LauncherActivity.class);
        intent.putExtra(LoadingActivity.INTENT_MOV_ID_NOTIFY, mId);
        Log.d(TAG, " * sendMovieNotification: mId = " + mId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(getBitmapFromURL(imgUrl))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat
                        .BigTextStyle()
                        .bigText(message))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(mId, notificationBuilder.build());
    }

    /**
     * Get Bitmap from drawable resouce;
     *
     * @return bitmap
     */
    private Bitmap getBitmapLageIcon() {
        return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }

    /**
     * Get bitmap from url;
     *
     * @param src url;
     * @return bitmap
     */
    private Bitmap getBitmapFromURL(String src) {
        if (src == null || src.length() < 5) return getBitmapLageIcon();
        try {
            src = src.replace(" ", "");
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            return getBitmapLageIcon();
        }
    }
}
