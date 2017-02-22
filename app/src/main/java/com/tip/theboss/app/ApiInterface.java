package com.tip.theboss.app;

import com.tip.theboss.model.data.Classification;
import com.tip.theboss.model.data.Job;
import com.tip.theboss.model.data.User;
import com.tip.theboss.model.response.JobApplicantListResponse;
import com.tip.theboss.model.response.JobApplicationResponse;
import com.tip.theboss.model.response.JobListResponse;
import com.tip.theboss.model.response.LoginResponse;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * @author pocholomia
 * @since 24/01/2017
 */

@SuppressWarnings("WeakerAccess")
public interface ApiInterface {

    @FormUrlEncoded
    @POST(Endpoints.LOGIN)
    Call<LoginResponse> login(@Field(Constants.USERNAME) String username,
                              @Field(Constants.PASSWORD) String password);

    @FormUrlEncoded
    @POST(Endpoints.REGISTER)
    Call<User> register(@Field(Constants.USERNAME) String username,
                        @Field(Constants.EMAIL) String email,
                        @Field(Constants.FIRST_NAME) String firstName,
                        @Field(Constants.LAST_NAME) String lastName,
                        @Field(Constants.MOBILE_NUMBER) String mobileNumber,
                        @Field(Constants.PASSWORD) String password);

    @GET(Endpoints.CLASSIFICATIONS)
    Call<List<Classification>> classifications();

    @GET(Endpoints.JOBS)
    Call<JobListResponse> jobs(@Header(Constants.AUTHORIZATION) String basicAuthentication);

    @GET(Endpoints.JOBS)
    Call<JobListResponse> jobs(@Header(Constants.AUTHORIZATION) String basicAuthentication,
                               @QueryMap Map<String, String> params);

    @GET(Endpoints.JOB_ID)
    Call<Job> getJob(@Path(Constants.ID) int id,
                     @Header(Constants.AUTHORIZATION) String basicAuthentication);

    @FormUrlEncoded
    @POST(Endpoints.JOBS)
    Call<Job> createJob(@Header(Constants.AUTHORIZATION) String basicAuthentication,
                        @Field(Constants.TITLE) String title,
                        @Field(Constants.DESCRIPTION) String description,
                        @Field(Constants.CLASSIFICATION) int classificationId,
                        @Field(Constants.LOCATION) String location,
                        @Field(Constants.FEE) double fee,
                        @Field(Constants.DATE_START) String dateStart,
                        @Field(Constants.DATE_END) String dateEnd);

    @FormUrlEncoded
    @PUT(Endpoints.JOB_ID)
    Call<Job> updateJob(@Path(Constants.ID) int id,
                        @Header(Constants.AUTHORIZATION) String basicAuthentication,
                        @Field(Constants.TITLE) String title,
                        @Field(Constants.DESCRIPTION) String description,
                        @Field(Constants.CLASSIFICATION) int classificationId,
                        @Field(Constants.LOCATION) String location,
                        @Field(Constants.FEE) double fee,
                        @Field(Constants.DATE_START) String dateStart,
                        @Field(Constants.DATE_END) String dateEnd);

    @DELETE(Endpoints.JOB_ID)
    Call<ResponseBody> deleteJob(@Path(Constants.ID) int id,
                                 @Header(Constants.AUTHORIZATION) String basicAuthentication);

    @GET(Endpoints.JOB_APPLICATION)
    Call<JobApplicantListResponse> applicants(@Header(Constants.AUTHORIZATION) String basicAuthentication,
                                              @QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST(Endpoints.JOB_APPLICATION)
    Call<JobApplicationResponse> apply(@Header(Constants.AUTHORIZATION) String basicAuthentication,
                                       @Field(Constants.JOB) int jobId);


    @DELETE(Endpoints.JOB_APPLICATION_ID)
    Call<ResponseBody> deleteApplicant(@Path(Constants.ID) int id,
                                       @Header(Constants.AUTHORIZATION) String basicAuthentication);


}
