package com.tip.theboss.ui.login;

import com.tip.theboss.model.data.User;
import com.tip.theboss.ui.base.UserPresenter;

import io.realm.Realm;

/**
 * @author pocholomia
 * @since 24/01/2017
 */
class LoginPresenter extends UserPresenter<LoginView> {
    private Realm realm;

    void onStart() {
        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        if (user != null) getView().onLoginSuccess();
    }

    void onStop() {
        realm.close();
    }

}
