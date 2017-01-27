package com.tip.theboss.ui.jobs.list;

import com.tip.theboss.model.data.Job;
import com.tip.theboss.ui.base.MoreListView;

import java.util.List;

/**
 * @author pocholomia
 * @since 26/01/2017
 */

public interface JobListView extends MoreListView {
    void stopLoading();

    void showMessage(String message);

    void addNext(String nextUrl);

    void setJobs(List<Job> jobs);

    void onJobItemClicked(Job job);
}
