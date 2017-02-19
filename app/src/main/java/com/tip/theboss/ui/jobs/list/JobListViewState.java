package com.tip.theboss.ui.jobs.list;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * @author pocholomia
 * @since 26/01/2017
 */

class JobListViewState implements RestorableViewState<JobListView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<JobListView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(JobListView view, boolean retained) {

    }
}
