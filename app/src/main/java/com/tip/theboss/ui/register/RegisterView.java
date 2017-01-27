package com.tip.theboss.ui.register;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * @author pocholomia
 * @since 24/01/2017
 */

public interface RegisterView extends MvpView {

    void onRegister();

    void showMessage(String message);

    void startLoading();

    void stopLoading();

    void onRegisterSuccess();
}
