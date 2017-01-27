package com.tip.theboss.model.data;

import com.google.gson.annotations.SerializedName;
import com.tip.theboss.util.DateTimeUtils;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author pocholomia
 * @since 26/01/2017
 */

public class Job extends RealmObject {

    @PrimaryKey
    private int id;

    private String user;

    @SerializedName("classification_id")
    private int classificationId;
    @SerializedName("classification_title")
    private String classificationTitle;

    private String title;
    private String description;
    private Date created;
    @SerializedName("date_start")
    private String dateStart;
    @SerializedName("date_end")
    private String dateEnd;
    private double fee;
    private String location;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(int classificationId) {
        this.classificationId = classificationId;
    }

    public String getClassificationTitle() {
        return classificationTitle;
    }

    public void setClassificationTitle(String classificationTitle) {
        this.classificationTitle = classificationTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getJobInfo() {
        return "Fee: Php " + fee + "\t\t" + "Duration: " + getDuration() + "\t\t" + "Posted: " + DateTimeUtils.toDuration(created);
    }

    public String getDuration() {
        return dateStart.contentEquals(dateEnd) ? dateStart : dateStart + "-" + dateEnd;
    }

}
