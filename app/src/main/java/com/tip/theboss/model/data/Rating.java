package com.tip.theboss.model.data;

import com.google.gson.annotations.SerializedName;
import com.tip.theboss.app.Constants;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Cholo Mia on 2/26/2017.
 */

public class Rating extends RealmObject {

    public static final String RATE_USERNAME = "rateUsername";

    @PrimaryKey
    private int id;
    private String username;
    @SerializedName(Constants.RATE_USERNAME)
    private String rateUsername;
    private int rate;
    private int type;
    private String comment;
    private Date created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRateUsername() {
        return rateUsername;
    }

    public void setRateUsername(String rateUsername) {
        this.rateUsername = rateUsername;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
