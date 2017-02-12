package com.tip.theboss.ui.classifications;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.theboss.model.data.Classification;

import java.util.List;

/**
 * Created by Cholo Mia on 2/11/2017.
 */

public interface ClassificationsView extends MvpView {
    void setClassifications(List<Classification> classifications);

    void stopLoad();

    void showMessage(String message);

    void onClassificationClick(Classification classification);
}
