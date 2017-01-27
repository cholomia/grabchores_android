package com.tip.theboss.ui.profile;

import com.tip.theboss.model.data.User;
import com.tip.theboss.ui.base.UserView;

/**
 * @author pocholomia
 * @since 27/01/2017
 */

public interface ProfileView extends UserView {

    void onLogout();

    void setUser(User user);

    void onLogoutSuccess();
}
