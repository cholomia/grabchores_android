package com.tip.theboss.model.data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author pocholomia
 * @since 01/02/2017
 */

public class Applicants extends RealmObject {

    @PrimaryKey
    private int id;
    @SerializedName("username")
    private String username;
    @SerializedName("job")
    private int jobId;
    private boolean apply;
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

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public boolean isApply() {
        return apply;
    }

    public void setApply(boolean apply) {
        this.apply = apply;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
