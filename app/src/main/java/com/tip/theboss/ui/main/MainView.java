package com.tip.theboss.ui.main;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.theboss.model.data.Classification;
import com.tip.theboss.model.data.User;

import java.util.List;

/**
 * Created by Cholo Mia on 2/11/2017.
 */

public interface MainView extends MvpView {
    void setUser(User user);

    void setClassifications(List<Classification> classifications);

    void showMessage(String message);

    void onLogout();
}
