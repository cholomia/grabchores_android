package com.tip.theboss.ui.jobs.list;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.theboss.app.App;
import com.tip.theboss.model.data.Job;
import com.tip.theboss.model.data.User;
import com.tip.theboss.model.response.JobListResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author pocholomia
 * @since 26/01/2017
 */

class JobListPresenter extends MvpNullObjectBasePresenter<JobListView> {

    private static final String TAG = JobListPresenter.class.getSimpleName();

    private Realm realm;
    private RealmResults<Job> jobRealmResults;
    private User user;

    private String query;
    private int classification;
    private String username;

    void onStart(int classification, String username, boolean open, boolean apply) {
        this.classification = classification;
        this.username = username;
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        jobRealmResults = realm.where(Job.class).findAllSortedAsync("created", Sort.DESCENDING);
        if (classification != -1) {
            jobRealmResults = jobRealmResults.where().equalTo("classificationId", classification)
                    .findAllSortedAsync("created", Sort.DESCENDING);
        }
        if (username != null && !username.isEmpty()) {
            jobRealmResults = jobRealmResults.where().equalTo("username", username)
                    .findAllSortedAsync("created", Sort.DESCENDING);
        }
        if (open) {
            jobRealmResults = jobRealmResults.where().equalTo("open", open)
                    .findAllSortedAsync("created", Sort.DESCENDING);
        }
        if (apply) {
            jobRealmResults = jobRealmResults.where().equalTo("apply", apply)
                    .findAllSortedAsync("created", Sort.DESCENDING);
        }
        jobRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Job>>() {
            @Override
            public void onChange(RealmResults<Job> element) {
                filterList();
            }
        });
    }

    void onStop() {
        jobRealmResults.removeChangeListeners();
        realm.close();
    }

    void setQuery(String query) {
        this.query = query;
        filterList();
        refresh();
    }

    private void filterList() {
        if (jobRealmResults.isLoaded() && jobRealmResults.isValid()) {
            RealmResults<Job> jobFilterRealmResults = jobRealmResults;
            if (query != null && !query.isEmpty()) {
                jobFilterRealmResults = jobFilterRealmResults.where()
                        .contains("title", query, Case.INSENSITIVE)
                        .or()
                        .contains("description", query, Case.INSENSITIVE)
                        .findAll();
            }
            List<Job> jobs = realm.copyFromRealm(jobFilterRealmResults);
            getView().setJobs(jobs);
        }
    }

    void refresh() {
        Map<String, String> parameters = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            parameters.put("title", query);
            parameters.put("description", query);
        }
        if (classification != -1) {
            parameters.put("classification_id", classification + "");
        }
        if (username != null && !username.isEmpty()) {
            parameters.put("username", username);
        }
        if (parameters.isEmpty())
            load(App.getInstance().getApiInterface().jobs(Credentials.basic(user.getUsername(), user.getPassword())));
        else refresh(parameters);
    }

    void refresh(Map<String, String> params) {
        load(App.getInstance().getApiInterface().jobs(Credentials.basic(user.getUsername(), user.getPassword()), params));
    }

    private void load(final Call<JobListResponse> jobListResponseCall) {
        jobListResponseCall.enqueue(new Callback<JobListResponse>() {
            @Override
            public void onResponse(Call<JobListResponse> call, final Response<JobListResponse> response) {
                getView().stopLoading();
                if (response.isSuccessful()) {

                    final String title = call.request().url().queryParameter("title");
                    final String description = call.request().url().queryParameter("description");
                    final String classification = call.request().url().queryParameter("classification_id");
                    final String username = call.request().url().queryParameter("username");

                    try (Realm realm = Realm.getDefaultInstance()) {
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                if (response.body().getPrevious() == null
                                        || response.body().getPrevious().isEmpty()) {
                                    RealmResults<Job> jobRealmResults = realm.where(Job.class)
                                            .findAll();
                                    if (title != null && description != null) {
                                        jobRealmResults = jobRealmResults.where()
                                                .contains("title", title, Case.INSENSITIVE)
                                                .or()
                                                .contains("description", description, Case.INSENSITIVE)
                                                .findAll();
                                    }
                                    if (classification != null) {
                                        int classificationId = Integer.parseInt(classification);
                                        jobRealmResults = jobRealmResults.where()
                                                .equalTo("classification_id", classificationId)
                                                .findAll();
                                    }
                                    if (username != null) {
                                        jobRealmResults = jobRealmResults.where()
                                                .equalTo("username", username).findAll();
                                    }
                                    jobRealmResults.deleteAllFromRealm();
                                }

                                realm.insertOrUpdate(response.body().getResults());
                            }
                        }, new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                Log.e(TAG, "onError: Error Saving Job List", error);
                                getView().showMessage("Error Saving Job List");
                            }
                        });
                        getView().addNext(response.body().getNext());
                    }
                } else {
                    try {
                        getView().showMessage(response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, "onClassificationReturn: Error parsing error body", e);
                        getView().showMessage(response.message() != null ? response.message()
                                : "Unknown Error");
                    }
                }
            }

            @Override
            public void onFailure(Call<JobListResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: Job List Failed", t);
                getView().stopLoading();
                getView().showMessage("Error Retrieving Job List");
            }
        });
    }


}
