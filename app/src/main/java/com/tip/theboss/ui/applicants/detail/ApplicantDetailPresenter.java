package com.tip.theboss.ui.applicants.detail;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.theboss.app.App;
import com.tip.theboss.app.Constants;
import com.tip.theboss.model.data.Applicant;
import com.tip.theboss.model.data.User;
import com.tip.theboss.model.response.ApplicantAcceptResponse;

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
 * @since 23/02/2017
 */

public class ApplicantDetailPresenter extends MvpNullObjectBasePresenter<ApplicantDetailView> {

    private Realm realm;
    private Applicant applicant;
    private User user;

    public void onStart(int applicantId) {
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        applicant = realm.where(Applicant.class).equalTo(Constants.ID, applicantId).findFirstAsync();
        applicant.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                if (applicant.isLoaded() && applicant.isValid())
                    getView().setApplicant(realm.copyFromRealm(applicant));
            }
        });
    }

    public void onStop() {
        applicant.removeChangeListeners();
        realm.close();
    }

    public void refresh(int applicantId) {
        App.getInstance().getApiInterface().getApplicant(applicantId,
                Credentials.basic(user.getUsername(), user.getPassword()))
                .enqueue(new Callback<Applicant>() {
                    @Override
                    public void onResponse(Call<Applicant> call, final Response<Applicant> response) {
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
                                        getView().showMessage("Error Updating Applicant Detail");
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
                    public void onFailure(Call<Applicant> call, Throwable t) {
                        t.printStackTrace();
                        getView().showMessage("Error to Connect to Server");
                    }
                });
    }

    public void update(int id, boolean accept) {
        getView().startLoad();
        App.getInstance().getApiInterface().updateApplicant(
                Credentials.basic(user.getUsername(), user.getPassword()), id, accept)
                .enqueue(new Callback<ApplicantAcceptResponse>() {
                    @Override
                    public void onResponse(Call<ApplicantAcceptResponse> call,
                                           final Response<ApplicantAcceptResponse> response) {
                        getView().stopLoad();
                        if (response.isSuccessful()) {
                            if (response.body().isSuccess()) {
                                try (Realm realm = Realm.getDefaultInstance()) {
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            realm.insertOrUpdate(response.body().getApplicant());
                                        }
                                    }, new Realm.Transaction.OnSuccess() {
                                        @Override
                                        public void onSuccess() {
                                            getView().updateSuccess(response.body().getMessage());
                                        }
                                    }, new Realm.Transaction.OnError() {
                                        @Override
                                        public void onError(Throwable error) {
                                            error.printStackTrace();
                                            getView().showMessage("Error Saving Applicant on DB");
                                        }
                                    });
                                }
                            } else {
                                getView().showMessage(response.body().getMessage());
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
                    public void onFailure(Call<ApplicantAcceptResponse> call, Throwable t) {
                        t.printStackTrace();
                        getView().stopLoad();
                        getView().showMessage("Error Updating Applicant");
                    }
                });
    }
}
