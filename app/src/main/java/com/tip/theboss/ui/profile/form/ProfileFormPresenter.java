package com.tip.theboss.ui.profile.form;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.theboss.app.App;
import com.tip.theboss.model.data.User;
import com.tip.theboss.model.response.ProfileUpdateResponse;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cholo Mia on 2/26/2017.
 */

public class ProfileFormPresenter extends MvpNullObjectBasePresenter<ProfileFormView> {

    private Realm realm;
    private User user;

    void onStart() {
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        getView().setUser(realm.copyFromRealm(user));
    }

    void onStop() {
        realm.close();
    }

    void submit(String firstName, String lastName, String mobileNumber) {
        getView().startLoad();
        App.getInstance().getApiInterface().updateProfile(
                Credentials.basic(user.getUsername(), user.getPassword()),
                firstName, lastName, mobileNumber)
                .enqueue(new Callback<ProfileUpdateResponse>() {
                    @Override
                    public void onResponse(Call<ProfileUpdateResponse> call,
                                           final Response<ProfileUpdateResponse> response) {
                        getView().stopLoad();
                        if (response.isSuccessful()) {
                            if (response.body().isSuccess()) {
                                try (Realm realm = Realm.getDefaultInstance()) {
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            User user = realm.where(User.class).findFirst();
                                            String password = user.getPassword();
                                            User newUser = response.body().getUser();
                                            newUser.setPassword(password);
                                            realm.insertOrUpdate(newUser);
                                        }
                                    }, new Realm.Transaction.OnSuccess() {
                                        @Override
                                        public void onSuccess() {
                                            getView().updateSuccess();
                                        }
                                    }, new Realm.Transaction.OnError() {
                                        @Override
                                        public void onError(Throwable error) {
                                            error.printStackTrace();
                                            getView().showMessage("Error Updating Profile");
                                        }
                                    });
                                }
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
                    public void onFailure(Call<ProfileUpdateResponse> call, Throwable t) {
                        getView().stopLoad();
                        t.printStackTrace();
                        getView().showMessage("Error Updating Profile");
                    }
                });
    }


}
