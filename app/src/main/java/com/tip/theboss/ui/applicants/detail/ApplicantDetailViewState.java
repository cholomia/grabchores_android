package com.tip.theboss.ui.applicants.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * @author pocholomia
 * @since 23/02/2017
 */

public class ApplicantDetailViewState implements RestorableViewState<ApplicantDetailView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<ApplicantDetailView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(ApplicantDetailView view, boolean retained) {

    }
}
