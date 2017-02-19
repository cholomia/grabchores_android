package com.tip.theboss.ui.jobs.detail;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.theboss.model.data.Job;

/**
 * @author pocholomia
 * @since 26/01/2017
 */

public interface JobDetailView extends MvpView {

    void onUserClick(String user);

    void onClassificationClick(int classificationId, String classificationTitle);

    void onApply(Job job);

    void setJob(Job job);

    void startLoading();

    void stopLoading();

    void showMessage(String message);

    void onApplySuccess();

    void setOwner(boolean owner);

    void stopPullLoading();

    void onDeleteSuccess();
}
