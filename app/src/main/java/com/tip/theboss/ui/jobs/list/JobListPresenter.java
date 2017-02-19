package com.tip.theboss.ui.jobs.list;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.theboss.app.App;
import com.tip.theboss.app.Constants;
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

    private Realm realm;
    private RealmResults<Job> jobRealmResults;
    private User user;

    private String query;
    private int classification;
    private String username;
    private boolean open;

    void onStart(int classification, String username, boolean open, boolean apply) {
        this.classification = classification;
        this.username = username;
        this.open = open;
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        jobRealmResults = realm.where(Job.class).findAllSorted(Constants.CREATED, Sort.DESCENDING);
        if (classification != -1)
            jobRealmResults = jobRealmResults.where().equalTo(Constants.CLASSIFICATION, classification)
                    .findAllSorted(Constants.CREATED, Sort.DESCENDING);
        if (username != null && !username.isEmpty())
            jobRealmResults = jobRealmResults.where().equalTo(Constants.USERNAME, username)
                    .findAllSorted(Constants.CREATED, Sort.DESCENDING);
        if (open) jobRealmResults = jobRealmResults.where().equalTo(Constants.OPEN, true)
                .findAllSorted(Constants.CREATED, Sort.DESCENDING);
        if (apply) jobRealmResults = jobRealmResults.where().equalTo(Constants.APPLY, true)
                .findAllSorted(Constants.CREATED, Sort.DESCENDING);
        filterList();
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

    void setQuery(String query, boolean refresh) {
        this.query = query;
        filterList();
        if (refresh) refresh();
    }

    private void filterList() {
        if (jobRealmResults.isLoaded() && jobRealmResults.isValid()) {
            RealmResults<Job> jobFilterRealmResults = jobRealmResults;
            if (query != null && !query.isEmpty())
                jobFilterRealmResults = jobFilterRealmResults.where()
                        .contains(Constants.TITLE, query, Case.INSENSITIVE)
                        .or()
                        .contains(Constants.DESCRIPTION, query, Case.INSENSITIVE)
                        .findAll();
            List<Job> jobs = realm.copyFromRealm(jobFilterRealmResults);
            getView().setJobs(jobs);
        }
    }

    void refresh() {
        Map<String, String> parameters = new HashMap<>();
        if (query != null && !query.isEmpty()) parameters.put(Constants.SEARCH, query);
        if (classification != -1) parameters.put(Constants.CLASSIFICATION, classification + "");
        if (username != null && !username.isEmpty()) parameters.put(Constants.USERNAME, username);
        if (open) parameters.put(Constants.OPEN, String.valueOf(true));
        if (parameters.isEmpty())
            load(App.getInstance().getApiInterface().jobs(Credentials.basic(user.getUsername(),
                    user.getPassword())));
        else refresh(parameters);
    }

    void refresh(Map<String, String> params) {
        load(App.getInstance().getApiInterface().jobs(Credentials.basic(user.getUsername(),
                user.getPassword()), params));
    }

    private void load(final Call<JobListResponse> jobListResponseCall) {
        jobListResponseCall.enqueue(new Callback<JobListResponse>() {
            @Override
            public void onResponse(Call<JobListResponse> call, final Response<JobListResponse> response) {
                getView().stopLoading();
                if (response.isSuccessful()) {

                    final String search = call.request().url().queryParameter(Constants.SEARCH);
                    final String classification = call.request().url().queryParameter(Constants.CLASSIFICATION);
                    final String username = call.request().url().queryParameter(Constants.USERNAME);
                    final String open = call.request().url().queryParameter(Constants.OPEN);

                    try (Realm realm = Realm.getDefaultInstance()) {
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                if (response.body().getPrevious() == null
                                        || response.body().getPrevious().isEmpty()) {
                                    RealmResults<Job> jobRealmResults = realm.where(Job.class)
                                            .findAll();
                                    if (search != null && !search.isEmpty())
                                        jobRealmResults = jobRealmResults.where()
                                                .contains(Constants.TITLE, search, Case.INSENSITIVE)
                                                .or()
                                                .contains(Constants.DESCRIPTION, search, Case.INSENSITIVE)
                                                .findAll();
                                    if (classification != null)
                                        jobRealmResults = jobRealmResults.where()
                                                .equalTo(Constants.CLASSIFICATION,
                                                        Integer.parseInt(classification))
                                                .findAll();
                                    if (username != null) jobRealmResults = jobRealmResults.where()
                                            .equalTo(Constants.USERNAME, username).findAll();
                                    if (open != null) jobRealmResults = jobRealmResults.where()
                                            .equalTo(Constants.OPEN, true).findAll();
                                    jobRealmResults.deleteAllFromRealm();
                                }
                                realm.insertOrUpdate(response.body().getResults());
                            }
                        }, new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                error.printStackTrace();
                                getView().showMessage("Error Saving Job List");
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
            public void onFailure(Call<JobListResponse> call, Throwable t) {
                t.printStackTrace();
                getView().stopLoading();
                getView().showMessage("Error Retrieving Job List");
            }
        });
    }


}
