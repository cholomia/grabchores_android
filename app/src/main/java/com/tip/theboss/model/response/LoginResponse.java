package com.tip.theboss.model.response;


import com.tip.theboss.model.data.User;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public class LoginResponse extends BasicResponse {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
