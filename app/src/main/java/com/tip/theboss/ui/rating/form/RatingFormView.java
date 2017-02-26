package com.tip.theboss.ui.rating.form;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Cholo Mia on 2/26/2017.
 */

public interface RatingFormView extends MvpView {

    void onSubmit();

    void startLoad();

    void stopLoad();

    void showMessage(String message);

    void createRatingSuccess();
}
