package com.tip.theboss.ui.applicants.list;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * @author pocholomia
 * @since 21/02/2017
 */

public class ApplicantsViewState implements RestorableViewState<ApplicantsView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<ApplicantsView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(ApplicantsView view, boolean retained) {

    }
}
