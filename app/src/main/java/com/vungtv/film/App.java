package com.vungtv.film;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by pc on 2/17/2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LeakCanary.install(this);

        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("vungtv.realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(configuration);
    }
}
