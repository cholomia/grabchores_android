package com.tip.theboss.ui.jobs.form;

import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.theboss.model.data.Classification;

import java.util.List;

/**
 * @author pocholomia
 * @since 27/01/2017
 */

public interface JobFormView extends MvpView {

    void onPost();

    void onSetDate(View view);

    void showMessage(String message);

    void startLoading();

    void stopLoading();

    void onCreateSuccess();

    void setClassifications(List<Classification> classifications);
}
