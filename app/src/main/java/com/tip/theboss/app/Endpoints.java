package com.tip.theboss.app;

/**
 * @author pocholomia
 * @since 24/01/2017
 */

class Endpoints {

    private static final String _ID = "{id}/";
    public static final String BASE_URL = "http://grabchores.pythonanywhere.com";
    //private static final String BASE_URL = "http://10.3.32.203:8000";

    static final String API_URL = BASE_URL + "/api/";

    static final String LOGIN = "user/login/";
    static final String REGISTER = "user/register/";

    static final String JOBS = "jobs/";
    static final String JOB_APPLICATION = "applications/";
    static final String CLASSIFICATIONS = "classifications/";
    static final String JOB_ID = JOBS + _ID;
    public static final String JOB_APPLICATION_ID = JOB_APPLICATION + _ID;
    public static final String JOB_APPLICATION_ACCEPT = "applications-accept/";
    public static final String RATINGS = "ratings/";
    public static final String RATINGS_ID = RATINGS + _ID;
    public static final String PROFILE_UPDATE = "profile-update/";
    public static final String CHANGE_PASSWORD = "user/change-password/";
}
