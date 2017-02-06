package com.tip.theboss.model.pojo;

import com.google.gson.annotations.SerializedName;
import com.tip.theboss.model.data.Job;

/**
 * @author pocholomia
 * @since 01/02/2017
 */

public class JobApplication {

    private int id;
    @SerializedName("job")
    private Job job;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
