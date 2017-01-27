package com.tip.theboss.ui.jobs.list;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.theboss.app.App;
import com.tip.theboss.model.data.Job;
import com.tip.theboss.model.data.User;
import com.tip.theboss.model.response.JobListResponse;

import java.io.IOException;
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

public class JobListPresenter extends MvpNullObjectBasePresenter<JobListView> {

    private static final String TAG = JobListPresenter.class.getSimpleName();

    private Realm realm;
    private RealmResults<Job> jobRealmResults;
    private User user;

    private String query;

    void onStart() {
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        jobRealmResults = realm.where(Job.class).findAllSortedAsync("created", Sort.DESCENDING);
        jobRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Job>>() {
            @Override
            public void onChange(RealmResults<Job> element) {
                filterList();
            }
        });
    }

    public void setQuery(String query) {
        this.query = query;
        filterList();
    }

    private void filterList() {
        if (jobRealmResults.isLoaded() && jobRealmResults.isValid()) {
            List<Job> jobs;
            if (query !=null && !query.isEmpty()) {
                jobs = realm.copyFromRealm(jobRealmResults.where()
                        .contains("title", query, Case.INSENSITIVE)
                        .or()
                        .contains("description", query, Case.INSENSITIVE)
                        .findAll());
            } else {
                jobs = realm.copyFromRealm(jobRealmResults);
            }
            getView().setJobs(jobs);
        }
    }

    void onStop() {
        jobRealmResults.removeChangeListeners();
        realm.close();
    }

    void refresh() {
        load(App.getInstance().getApiInterface().jobs(Credentials.basic(user.getUsername(), user.getPassword())));
    }

    void refresh(Map<String, String> params) {
        load(App.getInstance().getApiInterface().jobs(Credentials.basic(user.getUsername(), user.getPassword()), params));
    }

    void load(Call<JobListResponse> jobListResponseCall) {
        jobListResponseCall.enqueue(new Callback<JobListResponse>() {
            @Override
            public void onResponse(Call<JobListResponse> call, final Response<JobListResponse> response) {
                getView().stopLoading();
                if (response.isSuccessful()) {
                    try (Realm realm = Realm.getDefaultInstance()) {
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                if (response.body().getPrevious() == null
                                        || response.body().getPrevious().isEmpty())
                                    realm.delete(Job.class);
                                realm.insertOrUpdate(response.body().getResults());
                            }
                        }, new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                Log.e(TAG, "onError: Error Saving Job List", error);
                                getView().showMessage("Error Saving Job List");
                            }
                        });
                        if (response.body().getCount() <= 0)
                            getView().showMessage("No Forums Retrieved");
                        getView().addNext(response.body().getNext());
                    }
                } else {
                    try {
                        getView().showMessage(response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse: Error parsing error body", e);
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
