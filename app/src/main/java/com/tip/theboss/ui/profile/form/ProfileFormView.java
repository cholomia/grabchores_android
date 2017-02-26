package com.tip.theboss.ui.profile.form;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.theboss.model.data.User;

/**
 * Created by Cholo Mia on 2/26/2017.
 */

public interface ProfileFormView extends MvpView {

    void onSubmit();

    void startLoad();

    void stopLoad();

    void showMessage(String message);

    void updateSuccess();

    void setUser(User user);
}
