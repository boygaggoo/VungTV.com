package com.vungtv.film.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LogoutService extends Service {


    public LogoutService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
