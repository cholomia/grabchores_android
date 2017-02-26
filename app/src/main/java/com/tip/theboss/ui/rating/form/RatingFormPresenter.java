package com.tip.theboss.ui.rating.form;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.theboss.app.App;
import com.tip.theboss.model.data.Rating;
import com.tip.theboss.model.data.User;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cholo Mia on 2/26/2017.
 */

public class RatingFormPresenter extends MvpNullObjectBasePresenter<RatingFormView> {

    private Realm realm;
    private User user;

    void onStart() {
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
    }

    void onStop() {
        realm.close();
    }

    void newRating(String rateUsername, int rate, int type, String comment) {
        if (comment.isEmpty()) {
            getView().showMessage("Comment fields is required");
            return;
        }
        getView().startLoad();
        App.getInstance().getApiInterface().createRating(
                Credentials.basic(user.getUsername(), user.getPassword()),
                rateUsername, rate, type, comment)
                .enqueue(new Callback<Rating>() {
                    @Override
                    public void onResponse(Call<Rating> call, final Response<Rating> response) {
                        getView().stopLoad();
                        if (response.isSuccessful()) {
                            try (Realm realm = Realm.getDefaultInstance()) {
                                realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.insertOrUpdate(response.body());
                                    }
                                }, new Realm.Transaction.OnSuccess() {
                                    @Override
                                    public void onSuccess() {
                                        getView().createRatingSuccess();
                                    }
                                }, new Realm.Transaction.OnError() {
                                    @Override
                                    public void onError(Throwable error) {
                                        error.printStackTrace();
                                        getView().showMessage("Error Creating New Rating");
                                    }
                                });
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
                    public void onFailure(Call<Rating> call, Throwable t) {
                        getView().stopLoad();
                        t.printStackTrace();
                        getView().showMessage("Error Creating New Rating");
                    }
                });

    }
}
