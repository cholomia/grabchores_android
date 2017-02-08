package com.tip.theboss.ui.jobs.detail;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.theboss.app.App;
import com.tip.theboss.model.data.Job;
import com.tip.theboss.model.data.User;
import com.tip.theboss.model.response.JobApplicationResponse;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author pocholomia
 * @since 26/01/2017
 */

class JobDetailPresenter extends MvpNullObjectBasePresenter<JobDetailView> {

    private Realm realm;
    private Job job;
    private User user;

    void onStart(int jobId) {
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        job = realm.where(Job.class).equalTo("id", jobId).findFirstAsync();
        job.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                if (job.isLoaded() && job.isValid()) getView().setJob(realm.copyFromRealm(job));
            }
        });
    }

    void onStop() {
        job.removeChangeListeners();
        realm.close();
    }

    void applyJob(Job job) {
        getView().startLoading();
        App.getInstance().getApiInterface().apply(
                Credentials.basic(user.getUsername(), user.getPassword()), job.getId())
                .enqueue(new Callback<JobApplicationResponse>() {
                    @Override
                    public void onResponse(Call<JobApplicationResponse> call,
                                           final Response<JobApplicationResponse> response) {
                        getView().stopLoading();
                        if (response.isSuccessful()) {
                            try (Realm realm = Realm.getDefaultInstance()) {
                                realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.insertOrUpdate(response.body().getJobObj());
                                    }
                                }, new Realm.Transaction.OnSuccess() {
                                    @Override
                                    public void onSuccess() {
                                        getView().onApplySuccess();
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
                    public void onFailure(Call<JobApplicationResponse> call, Throwable t) {
                        t.printStackTrace();
                        getView().stopLoading();
                        getView().showMessage("Error Connecting to Server");
                    }
                });
    }
}
