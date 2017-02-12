package com.tip.theboss.ui.classifications;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.theboss.app.App;
import com.tip.theboss.model.data.Classification;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cholo Mia on 2/11/2017.
 */

public class ClassificationsPresenter extends MvpNullObjectBasePresenter<ClassificationsView> {

    private Realm realm;
    private RealmResults<Classification> classificationRealmResults;

    void onStart() {
        realm = Realm.getDefaultInstance();
        classificationRealmResults = realm.where(Classification.class).findAllAsync();
        classificationRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Classification>>() {
            @Override
            public void onChange(RealmResults<Classification> element) {
                getView().setClassifications(realm.copyFromRealm(classificationRealmResults));
            }
        });
    }

    void onStop() {
        classificationRealmResults.removeChangeListeners();
        realm.close();
    }

    void refresh() {
        App.getInstance().getApiInterface().classifications()
                .enqueue(new Callback<List<Classification>>() {
                    @Override
                    public void onResponse(Call<List<Classification>> call,
                                           final Response<List<Classification>> response) {
                        getView().stopLoad();
                        if (response.isSuccessful()) {
                            try (Realm realm = Realm.getDefaultInstance()) {
                                realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.insertOrUpdate(response.body());
                                    }
                                }, new Realm.Transaction.OnError() {
                                    @Override
                                    public void onError(Throwable error) {
                                        error.printStackTrace();
                                        getView().showMessage("Error Saving to DB");
                                    }
                                });
                            }
                        } else {
                            try {
                                getView().showMessage(response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                                getView().showMessage(response.message() != null ?
                                        response.message() : "Unknown Error");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Classification>> call, Throwable t) {
                        t.printStackTrace();
                        getView().stopLoad();
                        getView().showMessage("Error Calling API");
                    }
                });
    }

}
