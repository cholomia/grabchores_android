package com.tip.theboss.ui.applicants.list;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.theboss.app.App;
import com.tip.theboss.app.Constants;
import com.tip.theboss.model.data.Applicant;
import com.tip.theboss.model.data.User;
import com.tip.theboss.model.response.JobApplicantListResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
 * @since 20/02/2017
 */

class ApplicantsPresenter extends MvpNullObjectBasePresenter<ApplicantsView> {

    private Realm realm;
    private User user;
    private RealmResults<Applicant> applicantRealmResults;
    private int jobId;

    void onStart(int jobId) {
        this.jobId = jobId;
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        applicantRealmResults = realm.where(Applicant.class).equalTo(Constants.JOB, jobId)
                .findAllSortedAsync(Constants.ACCEPT, Sort.DESCENDING);
        applicantRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Applicant>>() {
            @Override
            public void onChange(RealmResults<Applicant> element) {
                if (applicantRealmResults.isLoaded() && applicantRealmResults.isValid())
                    getView().setApplicantList(realm.copyFromRealm(applicantRealmResults));
            }
        });
    }

    void onStop() {
        applicantRealmResults.removeChangeListeners();
        realm.close();
    }

    void refresh() {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.JOB, String.valueOf(jobId));
        refresh(params);
    }

    void refresh(Map<String, String> params) {
        App.getInstance().getApiInterface().applicants(
                Credentials.basic(user.getUsername(), user.getPassword()), params)
                .enqueue(new Callback<JobApplicantListResponse>() {
                    @Override
                    public void onResponse(Call<JobApplicantListResponse> call,
                                           final Response<JobApplicantListResponse> response) {
                        getView().stopLoading();
                        final String jobId = call.request().url().queryParameter(Constants.JOB);
                        if (response.isSuccessful()) {
                            try (Realm realm = Realm.getDefaultInstance()) {
                                realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        if (response.body().getPrevious() == null
                                                || response.body().getPrevious().isEmpty()) {
                                            if (jobId != null && !jobId.isEmpty()) {
                                                RealmResults<Applicant> applicantRealmResults = realm
                                                        .where(Applicant.class)
                                                        .equalTo(Constants.JOB, Integer.parseInt(jobId))
                                                        .findAll();
                                                applicantRealmResults.deleteAllFromRealm();
                                            }
                                        }
                                        realm.insertOrUpdate(response.body().getResults());
                                    }
                                }, new Realm.Transaction.OnError() {
                                    @Override
                                    public void onError(Throwable error) {
                                        error.printStackTrace();
                                        getView().showMessage("Error Saving Applicant List on DB");
                                    }
                                });
                            }
                            getView().addNext(response.body().getNext());
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
                    public void onFailure(Call<JobApplicantListResponse> call, Throwable t) {
                        t.printStackTrace();
                        getView().stopLoading();
                        getView().showMessage("Error Retrieving Applicant List");
                    }
                });
    }

}
