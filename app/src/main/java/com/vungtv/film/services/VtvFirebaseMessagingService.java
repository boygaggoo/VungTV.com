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

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vungtv.film.R;
import com.vungtv.film.data.source.local.FollowNotifyManger;
import com.vungtv.film.feature.loading.LoadingActivity;
import com.vungtv.film.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class VtvFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = VtvFirebaseMessagingService.class.getSimpleName();

    public static final int NOTIFICATION_ID = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {

            // Check if message contains a data payload.
            if (remoteMessage.getData().get("mov_id") != null){

                // Send notification film follow;
                sendMovieNotification(
                        Integer.parseInt(remoteMessage.getData().get("mov_id")),
                        remoteMessage.getData().get("title"),
                        remoteMessage.getData().get("mov_poster"),
                        remoteMessage.getData().get("message")
                );
                FollowNotifyManger.update(getApplicationContext());
            }

            LogUtils.d(TAG, "onMessageReceived Message data payload: " + remoteMessage.getData());
        } else if (remoteMessage.getNotification() != null) {

            // Check if message contains a notification payload.
            String messengerBody = remoteMessage.getNotification().getBody();
            sendNewsNotification(messengerBody);

            LogUtils.d(TAG, " * Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        LogUtils.i(TAG, "onMessageReceived From: " + remoteMessage.getFrom());
    }

    /**
     * Tạo 1 notification thông báo tin tức;
     *
     * @param message {@link String}
     */
    private void sendNewsNotification(String message) {
        //if (!RemoteConfig.getNotificationOn(getApplicationContext())) return;

        Intent intent = new Intent(this, LoadingActivity.class);
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
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    /**
     * Tạo 1 notification thông báo cập nhật phim đang theo dõi;
     *
     * @param movId The ID of film
     * @param title The title notification
     * @param movPoster the image poster film
     * @param message The content notification.
     */
    private void sendMovieNotification(int movId, String title, String movPoster, String message) {

        //if (!RemoteConfig.getNotificationOn(getApplicationContext())) return;

        Intent intent = new Intent(this, LauncherActivity.class);
        intent.putExtra(LoadingActivity.INTENT_MOV_ID_NOTIFY, movId);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(getBitmapFromURL(movPoster))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat
                        .BigTextStyle()
                        .setBigContentTitle(title)
                        .bigText(message))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(movId, notificationBuilder.build());
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
     * @param src The url image;
     * @return The {@link Bitmap}
     */
    private Bitmap getBitmapFromURL(String src) {
        if (src == null || src.length() < 5) {
            return getBitmapLageIcon();
        }

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
