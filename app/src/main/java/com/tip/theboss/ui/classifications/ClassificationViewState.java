package com.tip.theboss.ui.classifications;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * Created by Cholo Mia on 2/11/2017.
 */

class ClassificationViewState implements RestorableViewState<ClassificationsView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<ClassificationsView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(ClassificationsView view, boolean retained) {

    }
}
