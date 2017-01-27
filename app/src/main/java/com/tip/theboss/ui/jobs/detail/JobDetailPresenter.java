package com.tip.theboss.ui.jobs.detail;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.theboss.model.data.Job;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;

/**
 * @author pocholomia
 * @since 26/01/2017
 */

public class JobDetailPresenter extends MvpNullObjectBasePresenter<JobDetailView> {

    private Realm realm;
    private Job job;

    void onStart(int jobId) {
        realm = Realm.getDefaultInstance();
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
}
