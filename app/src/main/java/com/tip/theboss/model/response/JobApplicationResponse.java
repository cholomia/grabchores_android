package com.tip.theboss.model.response;

import com.google.gson.annotations.SerializedName;
import com.tip.theboss.model.data.Job;

/**
 * Created by Cholo Mia on 2/8/2017.
 */

public class JobApplicationResponse {

    private int id;
    @SerializedName("username")
    private String username;
    @SerializedName("job")
    private int jobId;
    private boolean apply;
    @SerializedName("job_obj")
    private Job jobObj;

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

    public Job getJobObj() {
        return jobObj;
    }

    public void setJobObj(Job jobObj) {
        this.jobObj = jobObj;
    }
}
