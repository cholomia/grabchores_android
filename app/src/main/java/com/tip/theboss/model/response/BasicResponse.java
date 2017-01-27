package com.tip.theboss.model.response;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public class BasicResponse {

    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
