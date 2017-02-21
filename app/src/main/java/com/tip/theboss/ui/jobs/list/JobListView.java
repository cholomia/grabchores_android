package com.tip.theboss.ui.jobs.list;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.theboss.model.data.Job;
import com.tip.theboss.ui.base.MoreListView;

import java.util.List;

/**
 * @author pocholomia
 * @since 26/01/2017
 */

public interface JobListView extends MvpView, MoreListView {
    void stopLoading();

    void showMessage(String message);

    void addNext(String nextUrl);

    void setJobs(List<Job> jobs);

    void onJobItemClicked(Job job);
}
