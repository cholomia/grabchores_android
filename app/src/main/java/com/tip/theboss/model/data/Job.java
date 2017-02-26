package com.tip.theboss.model.data;

import com.google.gson.annotations.SerializedName;
import com.tip.theboss.app.Constants;
import com.tip.theboss.util.DateTimeUtils;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author pocholomia
 * @since 26/01/2017
 */

public class Job extends RealmObject {

    public static final String CLASSIFICATION_TITLE = "classificationTitle";
    public static final String DATE_START = "dateStart";
    public static final String DATE_END = "dateEnd";
    public static final String MY_STATUS = "myStatus";
    public static final String JOB_APPLICATION_ID = "jobApplicationId";

    @PrimaryKey
    private int id;

    private String username;

    private int classification;
    @SerializedName(Constants.CLASSIFICATION_TITLE)
    private String classificationTitle;

    private String title;
    private String description;
    private Date created;
    @SerializedName(Constants.DATE_START)
    private String dateStart;
    @SerializedName(Constants.DATE_END)
    private String dateEnd;
    private double fee;
    private String location;

    private boolean apply;
    @SerializedName(Constants.JOB_APPLICATION_ID)
    private int jobApplicationId;
    private boolean open;
    @SerializedName(Constants.MY_STATUS)
    private boolean myStatus;

    private String email;
    @SerializedName(Constants.MOBILE_NUMBER)
    private String mobileNumber;
    @SerializedName(Constants.FIRST_NAME)
    private String firstName;
    @SerializedName(Constants.LAST_NAME)
    private String lastName;

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

    public int getClassification() {
        return classification;
    }

    public void setClassification(int classification) {
        this.classification = classification;
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

    public boolean isApply() {
        return apply;
    }

    public void setApply(boolean apply) {
        this.apply = apply;
    }

    public int getJobApplicationId() {
        return jobApplicationId;
    }

    public void setJobApplicationId(int jobApplicationId) {
        this.jobApplicationId = jobApplicationId;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isMyStatus() {
        return myStatus;
    }

    public void setMyStatus(boolean myStatus) {
        this.myStatus = myStatus;
    }

    public String getJobInfo() {
        return "Fee: Php " + fee + "\t\t" + "Date: " + getDuration() + "\t\t" + "Posted: "
                + DateTimeUtils.toDuration(created);
    }

    public String getDuration() {
        return dateStart.contentEquals(dateEnd) ? dateStart : dateStart + "-" + dateEnd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
