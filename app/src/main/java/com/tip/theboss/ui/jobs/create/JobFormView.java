package com.tip.theboss.ui.jobs.create;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * @author pocholomia
 * @since 27/01/2017
 */

public interface JobFormView extends MvpView {

    void onPost();

    void onDateStart();

    void onDateEnd();
}
