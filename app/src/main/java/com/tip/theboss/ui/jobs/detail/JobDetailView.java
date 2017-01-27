package com.tip.theboss.ui.jobs.detail;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.theboss.model.data.Job;

/**
 * @author pocholomia
 * @since 26/01/2017
 */

public interface JobDetailView extends MvpView {

    void onUserClick(String user);

    void onClassificationClick(int classificationId);

    void onApply(Job job);

    void setJob(Job job);
}
