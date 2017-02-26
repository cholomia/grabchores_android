package com.tip.theboss.model.response;

import com.tip.theboss.model.data.User;

/**
 * Created by Cholo Mia on 2/26/2017.
 */

public class ProfileUpdateResponse extends BasicResponse {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
