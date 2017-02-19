package com.tip.theboss.model.response;

import com.google.gson.annotations.SerializedName;
import com.tip.theboss.app.Constants;
import com.tip.theboss.model.data.Job;

import java.util.Date;

/**
 * Created by Cholo Mia on 2/8/2017.
 */

public class JobApplicationResponse {

    private int id;
    private String username;
    private int job;
    private boolean accept;
    private Date created;
    @SerializedName(Constants.JOB_OBJ)
    private Job jobObj;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Job getJobObj() {
        return jobObj;
    }

    public void setJobObj(Job jobObj) {
        this.jobObj = jobObj;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getJob() {
        return job;
    }

    public void setJob(int job) {
        this.job = job;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
