package com.tip.theboss.ui.base;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * @author pocholomia
 * @since 27/01/2017
 */

public interface UserView extends MvpView {
    void showMessage(String message);

    void startLoading();

    void stopLoading();

    void onLoginSuccess();
}
