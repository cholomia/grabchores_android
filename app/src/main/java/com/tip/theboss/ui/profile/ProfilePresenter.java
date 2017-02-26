package com.tip.theboss.ui.profile;

import com.tip.theboss.model.data.User;
import com.tip.theboss.ui.base.UserPresenter;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;

/**
 * @author pocholomia
 * @since 27/01/2017
 */

class ProfilePresenter extends UserPresenter<ProfileView> {

    private Realm realm;

    private User user;

    void onStart() {
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirstAsync();
        user.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                if (user.isLoaded() && user.isValid()) getView().setUser(realm.copyFromRealm(user));
            }
        });
    }

    void onStop() {
        user.removeChangeListeners();
        realm.close();
    }

    void refresh() {
        if (user.isLoaded() && user.isValid())
            login(user.getUsername(), user.getPassword());
    }

}
