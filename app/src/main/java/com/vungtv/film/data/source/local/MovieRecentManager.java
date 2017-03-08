package com.vungtv.film.data.source.local;

import com.vungtv.film.eventbus.RecentModifyEvent;
import com.vungtv.film.model.MovieRecent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 *
 * Created by pc on 2/20/2017.
 */

public class MovieRecentManager extends RealmBaseManager{

    private static final String TAG = MovieRecentManager.class.getSimpleName();

    private RealmResults<MovieRecent> results;

    @Override
    public void registerCallback() {
        if (results != null) {
            results.addChangeListener(callback);
        }
    }

    @Override
    public void removeCallback() {
        if (results != null) {
            results.removeChangeListener(callback);
        }
    }

    /**
     * Add or update row movie recent;
     *
     * @param movieRecent (@link MovieRecent}
     */
    public void addOrUpdate(final MovieRecent movieRecent) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealm.copyToRealmOrUpdate(movieRecent);
                EventBus.getDefault().post(new RecentModifyEvent());
            }
        });
    }

    /**
     * Add multi row movies recent;
     *
     * @param movieRecents list (@link MovieRecent}
     */
    public void addAll(final List<MovieRecent> movieRecents) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                for (MovieRecent recent : movieRecents) {
                    mRealm.copyToRealm(recent);
                }
            }
        });
    }

    /**
     * Get a movie recent;
     *
     * @param movId the id of {@link MovieRecent}
     * @return {@link MovieRecent}
     */
    public MovieRecent get(int movId) {
        return mRealm.where(MovieRecent.class)
                .equalTo("movId", movId)
                .findFirst();
    }

    public ArrayList<MovieRecent> getAll() {
        ArrayList<MovieRecent> recents = new ArrayList<>();

        results = mRealm.where(MovieRecent.class)
                .findAllSorted("movCreateAt", Sort.DESCENDING);

        for (MovieRecent recent : results) {
            recents.add(recent);
        }

        return recents;
    }

    public ArrayList<MovieRecent> getAll(int limit) {
        ArrayList<MovieRecent> recents = new ArrayList<>();

        results = mRealm.where(MovieRecent.class)
                .findAllSorted("movCreateAt", Sort.DESCENDING);

        for (MovieRecent recent : results) {
            recents.add(recent);

            if (recents.size() == limit) {
                break;
            }
        }
        return recents;
    }

    public void delete(int movId) {
        final MovieRecent recent = get(movId);

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                recent.deleteFromRealm();
            }
        });
    }

    public void deleteAll() {
        final RealmResults<MovieRecent> results = mRealm.where(MovieRecent.class).findAll();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
    }
}
