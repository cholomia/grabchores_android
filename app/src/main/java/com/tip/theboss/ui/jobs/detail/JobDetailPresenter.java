package com.tip.theboss.ui.jobs.detail;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.theboss.app.App;
import com.tip.theboss.app.Constants;
import com.tip.theboss.model.data.Applicant;
import com.tip.theboss.model.data.Job;
import com.tip.theboss.model.data.User;
import com.tip.theboss.model.response.JobApplicationResponse;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import okhttp3.Credentials;
import okhttp3.ResponseBody;
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

    void onStart(final int jobId) {
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        job = realm.where(Job.class).equalTo(Constants.ID, jobId).findFirstAsync();
        job.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                if (job.isLoaded() && job.isValid()) {
                    getView().setJob(realm.copyFromRealm(job));
                    getView().setOwner(user.getUsername().contentEquals(job.getUsername()));
                }
            }
        });
    }

    void onStop() {
        if (job != null) job.removeChangeListeners();
        if (realm != null) realm.close();
    }

    void refresh(int jobId) {
        App.getInstance().getApiInterface().getJob(jobId,
                Credentials.basic(user.getUsername(), user.getPassword()))
                .enqueue(new Callback<Job>() {
                    @Override
                    public void onResponse(Call<Job> call, final Response<Job> response) {
                        getView().stopPullLoading();
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
                    public void onFailure(Call<Job> call, Throwable t) {
                        t.printStackTrace();
                        getView().stopPullLoading();
                        getView().showMessage("Error Connecting to Server");
                    }
                });
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
                                        Applicant applicant = new Applicant();
                                        applicant.setId(response.body().getId());
                                        applicant.setUsername(response.body().getUsername());
                                        applicant.setJob(response.body().getJob());
                                        applicant.setCreated(response.body().getCreated());
                                        applicant.setAccept(response.body().isAccept());
                                        realm.insertOrUpdate(applicant);

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

    void deleteJob(final int jobId) {
        getView().startLoading();
        App.getInstance().getApiInterface().deleteJob(jobId,
                Credentials.basic(user.getUsername(), user.getPassword()))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        getView().stopLoading();
                        if (response.isSuccessful()) {
                            try (Realm realm = Realm.getDefaultInstance()) {
                                realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        Job job = realm.where(Job.class)
                                                .equalTo(Constants.ID, jobId).findFirst();
                                        if (job != null) job.deleteFromRealm();
                                    }
                                }, new Realm.Transaction.OnError() {
                                    @Override
                                    public void onError(Throwable error) {
                                        error.printStackTrace();
                                        getView().showMessage("Error Deleting on Cache DB");
                                    }
                                });
                            }
                            getView().onDeleteSuccess();
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
                        getView().stopLoading();
                        getView().showMessage("Error Connecting to Server");
                    }
                });
    }
}
