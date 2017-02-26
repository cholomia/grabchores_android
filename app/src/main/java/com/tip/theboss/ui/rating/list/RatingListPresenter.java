package com.tip.theboss.ui.rating.list;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.theboss.app.App;
import com.tip.theboss.app.Constants;
import com.tip.theboss.model.data.Rating;
import com.tip.theboss.model.data.User;
import com.tip.theboss.model.response.RatingListResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.Credentials;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cholo Mia on 2/25/2017.
 */

public class RatingListPresenter extends MvpNullObjectBasePresenter<RatingListView> {

    private static final String TAG = RatingListPresenter.class.getSimpleName();
    private Realm realm;
    private User user;
    private RealmResults<Rating> ratingRealmResults;

    void onStart(int type, String rateUsername) {
        Log.d(TAG, "onStart: " + type + " " + rateUsername);
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        ratingRealmResults = realm.where(Rating.class)
                .equalTo(Constants.TYPE, type)
                .equalTo(Rating.RATE_USERNAME, rateUsername)
                .findAllSortedAsync(Constants.CREATED, Sort.DESCENDING);
        ratingRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Rating>>() {
            @Override
            public void onChange(RealmResults<Rating> element) {
                Log.d(TAG, "onChange: " + ratingRealmResults.size());
                if (ratingRealmResults.isLoaded() && ratingRealmResults.isValid())
                    getView().setRatingList(realm.copyFromRealm(ratingRealmResults));
            }
        });
    }

    void onStop() {
        if (ratingRealmResults != null)
            ratingRealmResults.removeChangeListeners();
        if (realm != null) realm.close();
    }

    void refresh(int type, String rateUsername) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(Constants.TYPE, String.valueOf(type));
        parameters.put(Constants.RATE_USERNAME, rateUsername);
        refresh(parameters);
    }

    void refresh(Map<String, String> parameters) {
        if (user == null) {
            getView().stopLoad();
            return;
        }
        App.getInstance().getApiInterface().ratings(
                Credentials.basic(user.getUsername(), user.getPassword()),
                parameters)
                .enqueue(new Callback<RatingListResponse>() {
                    @Override
                    public void onResponse(Call<RatingListResponse> call,
                                           final Response<RatingListResponse> response) {
                        getView().stopLoad();
                        if (response.isSuccessful()) {

                            final String type = call.request().url().queryParameter(Constants.TYPE);
                            final String rateUsername = call.request().url()
                                    .queryParameter(Constants.RATE_USERNAME);

                            try (Realm realm = Realm.getDefaultInstance()) {
                                realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        if (response.body().getPrevious() == null
                                                || response.body().getPrevious().isEmpty()) {
                                            RealmResults<Rating> ratingRealmResults = realm
                                                    .where(Rating.class).findAll();
                                            if (type != null) {
                                                ratingRealmResults = ratingRealmResults.where()
                                                        .equalTo(Constants.TYPE,
                                                                Integer.parseInt(type))
                                                        .findAll();
                                            }
                                            if (rateUsername != null) {
                                                ratingRealmResults = ratingRealmResults.where()
                                                        .equalTo(Rating.RATE_USERNAME, rateUsername)
                                                        .findAll();
                                            }
                                            ratingRealmResults.deleteAllFromRealm();
                                        }
                                        realm.insertOrUpdate(response.body().getResults());
                                    }
                                }, new Realm.Transaction.OnError() {
                                    @Override
                                    public void onError(Throwable error) {
                                        error.printStackTrace();
                                        getView().showMessage("Error Saving Rate List");
                                    }
                                });
                                getView().addNext(response.body().getNext());
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
                    public void onFailure(Call<RatingListResponse> call, Throwable t) {
                        t.printStackTrace();
                        getView().stopLoad();
                        getView().showMessage("Error Retrieving Rate List");
                    }
                });
    }

    void deleteRating(final int ratingId) {
        getView().startProgressLoad();
        App.getInstance().getApiInterface().deleteRating(ratingId,
                Credentials.basic(user.getUsername(), user.getPassword()))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        getView().stopProgressLoad();
                        if (response.isSuccessful()) {
                            try (Realm realm = Realm.getDefaultInstance()) {
                                realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        Rating rating = realm.where(Rating.class)
                                                .equalTo(Constants.ID, ratingId).findFirst();
                                        if (rating != null) rating.deleteFromRealm();
                                    }
                                }, new Realm.Transaction.OnError() {
                                    @Override
                                    public void onError(Throwable error) {
                                        error.printStackTrace();
                                        getView().showMessage("Error Deleting Rating");
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
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                        getView().stopProgressLoad();
                        getView().showMessage("Error Deleting Rating");
                    }
                });
    }

}
