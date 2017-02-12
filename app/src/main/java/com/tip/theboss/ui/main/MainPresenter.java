package com.tip.theboss.ui.main;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.theboss.model.data.Classification;
import com.tip.theboss.model.data.User;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmResults;

/**
 * Created by Cholo Mia on 2/11/2017.
 */

class MainPresenter extends MvpNullObjectBasePresenter<MainView> {

    private Realm realm;
    private User user;
    private RealmResults<Classification> classificationRealmResults;

    void onStart() {
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirstAsync();
        user.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                getView().setUser(realm.copyFromRealm(user));
            }
        });
        classificationRealmResults = realm.where(Classification.class).findAllAsync();
        classificationRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Classification>>() {
            @Override
            public void onChange(RealmResults<Classification> element) {
                getView().setClassifications(realm.copyFromRealm(classificationRealmResults));
            }
        });
    }

    void onStop() {
        user.removeChangeListeners();
        classificationRealmResults.removeChangeListeners();
        realm.close();
    }

    void logout() {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                getView().onLogout();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                getView().showMessage("Error Deleting All Data");
            }
        });
    }

    public String getUser() {
        if (user.isLoaded() && user.isValid())
            return user.getUsername();
        else return null;
    }
}
