package com.tip.theboss.ui.splash;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.tip.theboss.app.App;
import com.tip.theboss.model.data.Classification;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cholo Mia on 2/11/2017.
 */

public class SplashPresenter extends MvpNullObjectBasePresenter<SplashView> {
    public void refreshClassifications() {
        App.getInstance().getApiInterface().classifications().enqueue(new Callback<List<Classification>>() {
            @Override
            public void onResponse(Call<List<Classification>> call, final Response<List<Classification>> response) {
                if (response.isSuccessful()) {
                    try (Realm realm = Realm.getDefaultInstance()) {
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.insertOrUpdate(response.body());
                            }
                        });
                    }
                }
                getView().onClassificationReturn();
            }

            @Override
            public void onFailure(Call<List<Classification>> call, Throwable t) {
                t.printStackTrace();
                getView().onClassificationReturn();
            }
        });
    }
}
