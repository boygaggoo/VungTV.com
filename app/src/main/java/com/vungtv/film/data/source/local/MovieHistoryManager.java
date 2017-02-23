package com.vungtv.film.data.source.local;

import com.vungtv.film.model.MovieHistory;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.vungtv.film.R.id.recent;

/**
 *
 * Created by pc on 2/20/2017.
 */

public class MovieHistoryManager extends RealmBaseManager{

    private static final String TAG = MovieHistoryManager.class.getSimpleName();

    public void addOrUpdate(final MovieHistory movieHistory) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealm.copyToRealmOrUpdate(movieHistory);
            }
        });
    }

    public void addAll(final List<MovieHistory> movieHistories) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                for (MovieHistory recent : movieHistories) {
                    mRealm.copyToRealm(recent);
                }
            }
        });
    }

    public MovieHistory get(int movId) {
        return mRealm.where(MovieHistory.class)
                .equalTo("movId", movId)
                .findFirst();
    }

    public ArrayList<MovieHistory> getAll() {
        ArrayList<MovieHistory> histories = new ArrayList<>();

        RealmResults<MovieHistory> results = mRealm.where(MovieHistory.class)
                .findAllSorted("movCreateAt", Sort.DESCENDING);

        for (MovieHistory history : results) {
            histories.add(history);
        }

        return histories;
    }

    public void delete(int movId) {
        final MovieHistory history = get(movId);

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                history.deleteFromRealm();
            }
        });
    }

    public void deleteAll() {
        final RealmResults<MovieHistory> results = mRealm.where(MovieHistory.class).findAll();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
    }
}
