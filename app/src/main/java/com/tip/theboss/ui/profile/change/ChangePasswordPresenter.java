package com.tip.theboss.ui.profile.change;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.theboss.app.App;
import com.tip.theboss.model.data.User;
import com.tip.theboss.model.response.BasicResponse;

import java.io.IOException;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cholo Mia on 2/26/2017.
 */

public class ChangePasswordPresenter extends MvpNullObjectBasePresenter<ChangePasswordView> {

    private Realm realm;
    private User user;

    void onStart() {
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
    }

    void onStop() {
        realm.close();
    }


    public void submit(String currentPassword, final String newPassword, String repeatPassword) {
        if (currentPassword.isEmpty() || newPassword.isEmpty() || repeatPassword.isEmpty()) {
            getView().showMessage("Please fill-up all fields");
        } else if (!newPassword.contentEquals(repeatPassword)) {
            getView().showMessage("Passwords does not match");
        } else {
            getView().startLoad();
            App.getInstance().getApiInterface()
                    .changePassword(user.getUsername(), currentPassword, newPassword)
                    .enqueue(new Callback<BasicResponse>() {
                        @Override
                        public void onResponse(Call<BasicResponse> call,
                                               Response<BasicResponse> response) {
                            getView().stopLoad();
                            if (response.isSuccessful()) {
                                if (response.body().isSuccess()) {
                                    try (Realm realm = Realm.getDefaultInstance()) {
                                        realm.executeTransactionAsync(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm realm) {
                                                User user = realm.where(User.class).findFirst();
                                                user.setPassword(newPassword);
                                            }
                                        }, new Realm.Transaction.OnSuccess() {
                                            @Override
                                            public void onSuccess() {
                                                getView().changePasswordSuccess();
                                            }
                                        }, new Realm.Transaction.OnError() {
                                            @Override
                                            public void onError(Throwable error) {
                                                error.printStackTrace();
                                                getView().showMessage("Error parsing data");
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
                        public void onFailure(Call<BasicResponse> call, Throwable t) {
                            t.printStackTrace();
                            getView().stopLoad();
                            getView().showMessage("Unable to Change Password");
                        }
                    });
        }
    }
}
