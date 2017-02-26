package com.tip.theboss.ui.profile.change;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Cholo Mia on 2/26/2017.
 */

public interface ChangePasswordView extends MvpView {

    void onSubmit();

    void showMessage(String message);

    void startLoad();

    void stopLoad();

    void changePasswordSuccess();
}
