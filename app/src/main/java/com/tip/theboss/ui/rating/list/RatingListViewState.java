package com.tip.theboss.ui.rating.list;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * Created by Cholo Mia on 2/25/2017.
 */

public class RatingListViewState implements RestorableViewState<RatingListView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<RatingListView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(RatingListView view, boolean retained) {

    }
}
