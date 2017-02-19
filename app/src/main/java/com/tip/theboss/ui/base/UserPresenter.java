package com.tip.theboss.ui.base;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.theboss.app.App;
import com.tip.theboss.model.data.User;
import com.tip.theboss.model.response.LoginResponse;

import java.io.IOException;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author pocholomia
 * @since 27/01/2017
 */

public class UserPresenter<V extends UserView> extends MvpNullObjectBasePresenter<V> {

    private static final String TAG = UserPresenter.class.getSimpleName();

    public void login(String username, final String password) {
        if (username.isEmpty() || password.isEmpty()) {
            getView().showMessage("Please fill-up all fields");
        } else {
            getView().startLoading();
            App.getInstance().getApiInterface().login(username, password)
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                            getView().stopLoading();
                            if (response.isSuccessful()) {
                                if (response.body().isSuccess()) {
                                    final Realm realm = Realm.getDefaultInstance();
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            User user = response.body().getUser();
                                            user.setPassword(password);
                                            // TODO: 12/4/2016 Add Encryption
                                            realm.copyToRealmOrUpdate(user);
                                        }
                                    }, new Realm.Transaction.OnSuccess() {
                                        @Override
                                        public void onSuccess() {
                                            realm.close();
                                            getView().onLoginSuccess();
                                        }
                                    }, new Realm.Transaction.OnError() {
                                        @Override
                                        public void onError(Throwable error) {
                                            realm.close();
                                            error.printStackTrace();
                                            getView().showMessage("Error Saving API Response");
                                        }
                                    });
                                } else {
                                    getView().showMessage(response.body().getMessage());
                                }
                            } else {
                                try {
                                    getView().showMessage(response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    getView().showMessage(response.message() != null ? response.message()
                                            : "Unknown Error");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            t.printStackTrace();
                            getView().stopLoading();
                            getView().showMessage("Error Connecting to Server");
                        }
                    });
        }
    }
}
