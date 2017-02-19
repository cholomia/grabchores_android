package com.tip.theboss.ui.jobs.form;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.theboss.app.App;
import com.tip.theboss.app.Constants;
import com.tip.theboss.model.data.Classification;
import com.tip.theboss.model.data.Job;
import com.tip.theboss.model.data.User;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author pocholomia
 * @since 27/01/2017
 */

class JobFormPresenter extends MvpNullObjectBasePresenter<JobFormView> {

    private Realm realm;
    private User user;
    private RealmResults<Classification> classificationRealmResults;
    private Job job;

    void onStart(int jobId) {
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        if (jobId != -1) {
            job = realm.where(Job.class).equalTo(Constants.ID, jobId).findFirst();
        }
        classificationRealmResults = realm.where(Classification.class).findAllAsync();
        classificationRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Classification>>() {
            @Override
            public void onChange(RealmResults<Classification> element) {
                if (classificationRealmResults.isLoaded() && classificationRealmResults.isValid()) {
                    if (classificationRealmResults.size() > 0) {
                        getView().setClassifications(realm.copyFromRealm(classificationRealmResults));
                        if (job != null)
                            getView().setJob(realm.copyFromRealm(job));
                    } else {
                        loadClassification();
                    }
                }
            }
        });

    }

    void onStop() {
        classificationRealmResults.removeChangeListeners();
        realm.close();
    }

    private void loadClassification() {
        getView().startLoading();
        App.getInstance().getApiInterface().classifications().enqueue(
                new Callback<List<Classification>>() {
                    @Override
                    public void onResponse(Call<List<Classification>> call,
                                           final Response<List<Classification>> response) {
                        getView().stopLoading();
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
                                        getView().showMessage("Error Saving API Response");
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
                    public void onFailure(Call<List<Classification>> call, Throwable t) {
                        t.printStackTrace();
                        getView().stopLoading();
                        getView().showMessage("Error Connecting to Server");
                    }
                });
    }

    void addJob(int id, String title, String description, int classification, String location, String feeStr,
                String dateStart, String dateEnd) {
        double feeDbl;
        try {
            feeDbl = Double.parseDouble(feeStr);
        } catch (Exception e) {
            e.printStackTrace();
            getView().showMessage("Fee not valid amount!");
            return;
        }
        if (title.isEmpty() || description.isEmpty() || location.isEmpty() || feeStr.isEmpty()
                || dateStart.isEmpty() || dateEnd.isEmpty()) {
            getView().showMessage("Fill-up all fields");
        } else if (feeDbl <= 0) {
            getView().showMessage("Fee must be greater than zero (0)");
        } else {
            getView().startLoading();
            addOrUpdateJob(id == -1 ?
                    App.getInstance().getApiInterface().createJob(
                            Credentials.basic(user.getUsername(), user.getPassword()), title,
                            description, classification, location, feeDbl, dateStart, dateEnd) :
                    App.getInstance().getApiInterface().updateJob(id,
                            Credentials.basic(user.getUsername(), user.getPassword()), title,
                            description, classification, location, feeDbl, dateStart, dateEnd));
        }
    }

    void addOrUpdateJob(Call<Job> call) {
        call.enqueue(new Callback<Job>() {
            @Override
            public void onResponse(Call<Job> call, final Response<Job> response) {
                getView().stopLoading();
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
                                getView().onCreateSuccess();
                            }
                        }, new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                error.printStackTrace();
                                getView().showMessage("Error Saving API Response");
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
            public void onFailure(Call<Job> call, Throwable t) {
                t.printStackTrace();
                getView().stopLoading();
                getView().showMessage("Error Connecting to Server");
            }
        });
    }

}
