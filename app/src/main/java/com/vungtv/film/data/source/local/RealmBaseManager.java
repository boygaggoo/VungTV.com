package com.vungtv.film.data.source.local;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 *
 * Created by pc on 2/20/2017.
 */

public class RealmBaseManager {

    protected Realm mRealm;

    protected RealmChangeListener callback;

    public RealmBaseManager() {
        mRealm = Realm.getDefaultInstance();
    }

    public void registerCallback() {

    }

    public void removeCallback(){

    }

    public void close() {
        mRealm.close();
    }
}
