package com.vungtv.film.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.firebase.messaging.FirebaseMessaging;
import com.vungtv.film.R;
import com.vungtv.film.data.source.local.UserSessionManager;
import com.vungtv.film.data.source.remote.model.ApiUserMovies;
import com.vungtv.film.data.source.remote.service.UserMoviesServices;
import com.vungtv.film.feature.usermovies.UserMoviesActivity;
import com.vungtv.film.model.Movie;
import com.vungtv.film.util.LogUtils;

import java.util.List;

import retrofit2.Call;

public class RegisterFCMTopicService extends Service implements UserMoviesServices.ResultListener {
    private static final String TAG = RegisterFCMTopicService.class.getSimpleName();

    private Call<ApiUserMovies> call;

    private UserMoviesServices userMoviesServices;

    public RegisterFCMTopicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        userMoviesServices = new UserMoviesServices(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userMoviesServices.setLimit(1000);
        userMoviesServices.setOffset(0);
        userMoviesServices.setAccessToken(UserSessionManager.getAccessToken(getApplicationContext()));
        userMoviesServices.setResultListener(this);
        userMoviesServices.loadMovies(UserMoviesActivity.PAGE_FOLLOW);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        LogUtils.d(TAG, "onDestroy");
        if (call != null && call.isExecuted()) call.cancel();
        super.onDestroy();
    }

    @Override
    public void onFailure(int code, String error) {
        stopSelf();
    }

    @Override
    public void onUserMoviesResultSuccess(ApiUserMovies.Data data) {
        List<Movie> list = data.getMovies();
        if (list != null && list.size() > 0) {
            for (Movie movie : list) {
                String topic = String.format(getString(
                        R.string.prefix_fcm_topic_follow),
                        String.valueOf(movie.getMovId()));

                FirebaseMessaging.getInstance().subscribeToTopic(topic);
                LogUtils.d(TAG, "register topic: " + topic);
            }
        }

        stopSelf();
    }
}
