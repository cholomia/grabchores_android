package com.tip.theboss.ui.profile;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tip.theboss.R;
import com.tip.theboss.databinding.ActivityProfileBinding;
import com.tip.theboss.model.data.User;
import com.tip.theboss.ui.login.LoginActivity;


public class ProfileActivity extends MvpActivity<ProfileView, ProfilePresenter>
        implements ProfileView, SwipeRefreshLayout.OnRefreshListener {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        binding.setView(getMvpView());
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        presenter.onStart();
    }

    @Override
    protected void onDestroy() {
        presenter.onStop();
        super.onDestroy();
    }

    @NonNull
    @Override
    public ProfilePresenter createPresenter() {
        return new ProfilePresenter();
    }

    @Override
    public void onRefresh() {
        presenter.refresh();
    }

    @Override
    public void onLogout() {
        presenter.deleteAll();
    }

    @Override
    public void setUser(User user) {
        binding.setUser(user);
    }

    @Override
    public void onLogoutSuccess() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startLoading() {
        binding.swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void stopLoading() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoginSuccess() {
        showMessage("Update Info Success");
    }
}
