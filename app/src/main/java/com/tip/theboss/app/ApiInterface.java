package com.tip.theboss.app;

import com.tip.theboss.model.data.User;
import com.tip.theboss.model.response.JobListResponse;
import com.tip.theboss.model.response.LoginResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * @author pocholomia
 * @since 24/01/2017
 */

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
                        @Field(Constants.PASSWORD) String password);

    @GET(Endpoints.JOBS)
    Call<JobListResponse> jobs(@Header("Authorization") String basicAuthentication);

    @GET(Endpoints.JOBS)
    Call<JobListResponse> jobs(@Header("Authorization") String basicAuthentication,
                               @QueryMap Map<String, String> params);

}
