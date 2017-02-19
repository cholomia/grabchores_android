package com.tip.theboss.ui.jobs.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * Created by Cholo Mia on 2/19/2017.
 */

public class JobDetailViewState implements RestorableViewState<JobDetailView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<JobDetailView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(JobDetailView view, boolean retained) {

    }
}
