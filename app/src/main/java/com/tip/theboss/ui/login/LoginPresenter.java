package com.tip.theboss.ui.login;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.theboss.app.App;
import com.tip.theboss.model.data.User;
import com.tip.theboss.model.response.LoginResponse;
import com.tip.theboss.ui.base.UserPresenter;

import java.io.IOException;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
