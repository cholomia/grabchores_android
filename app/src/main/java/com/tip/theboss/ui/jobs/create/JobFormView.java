package com.tip.theboss.ui.jobs.create;

import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpView;

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
}
