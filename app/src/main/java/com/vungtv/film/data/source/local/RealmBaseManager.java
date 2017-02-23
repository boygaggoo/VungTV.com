package com.vungtv.film.data.source.local;

import io.realm.Realm;

/**
 *
 * Created by pc on 2/20/2017.
 */

public class RealmBaseManager {

    protected Realm mRealm;

    public RealmBaseManager() {
        mRealm = Realm.getDefaultInstance();
    }

    public void close() {
        mRealm.close();
    }
}
