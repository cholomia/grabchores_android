package com.tip.theboss.model.data;

import com.google.gson.annotations.SerializedName;
import com.tip.theboss.app.Constants;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author pocholomia
 * @since 24/01/2017
 */

public class UserProfile extends RealmObject {

    public static final String MOBILE_NUMBER = "mobileNumber";
    public static final String CREDIT_CARD = "creditCard";

    @PrimaryKey
    private int id;
    private int user;
    @SerializedName(Constants.MOBILE_NUMBER)
    private String mobileNumber;
    @SerializedName(Constants.CREDIT_CARD)
    private int creditCard;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public int getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(int creditCard) {
        this.creditCard = creditCard;
    }

}
