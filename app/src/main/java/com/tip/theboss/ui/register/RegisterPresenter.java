package com.tip.theboss.ui.register;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.theboss.app.App;
import com.tip.theboss.model.data.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author pocholomia
 * @since 24/01/2017
 */

class RegisterPresenter extends MvpNullObjectBasePresenter<RegisterView> {

    void register(String username, String email, String firstName, String lastName,
                  String mobileNumber, String password, String repeatPassword) {
        if (username.isEmpty() || email.isEmpty() || firstName.isEmpty() || lastName.isEmpty()
                || mobileNumber.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            getView().showMessage("Fill-up all fields");
        } else if (!password.contentEquals(repeatPassword)) {
            getView().showMessage("Password does not match");
        } else {
            getView().startLoading();
            App.getInstance().getApiInterface()
                    .register(username, email, firstName, lastName, mobileNumber, password)
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            getView().stopLoading();
                            if (response.isSuccessful()) {
                                getView().onRegisterSuccess();
                            } else {
                                try {
                                    String errorBody = response.errorBody().string();
                                    getView().showMessage(errorBody);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    getView().showMessage(response.message() != null ?
                                            response.message() : "Unknown Exception");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            t.printStackTrace();
                            getView().stopLoading();
                            getView().showMessage("Error Connecting to Server");
                        }
                    });
        }
    }
}
