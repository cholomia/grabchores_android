package com.tip.theboss.app;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author pocholomia
 * @since 24/01/2017
 */

public class App extends Application {

    private static App sInstance;
    private OkHttpClient.Builder httpClient;
    private Retrofit retrofit;
    private ApiInterface apiInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public synchronized static App getInstance() {
        return sInstance;
    }


    private OkHttpClient.Builder getOkHttpClient() {
        if (httpClient == null) {
            // setup logs for debugging of http request
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            httpClient = new OkHttpClient.Builder();
            // add your other interceptors …

            // add logging as last interceptor
            httpClient.addInterceptor(logging);  // <-- this is the important line!
        }
        return httpClient;
    }

    private Retrofit getClient() {
        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                    .setDateFormat(Constants.YYYY_MM_DD_T_HH_MM_SS)
                    .create();

            String url = Endpoints.API_URL;
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(getOkHttpClient().build())
                    .build();
        }
        return retrofit;
    }

    public ApiInterface getApiInterface() {
        if (apiInterface == null) {
            apiInterface = getClient().create(ApiInterface.class);
        }
        return apiInterface;
    }

}
