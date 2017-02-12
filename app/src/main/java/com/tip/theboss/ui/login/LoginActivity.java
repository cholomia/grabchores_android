package com.tip.theboss.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tip.theboss.R;
import com.tip.theboss.databinding.ActivityLoginBinding;
import com.tip.theboss.ui.register.RegisterActivity;
import com.tip.theboss.ui.splash.SplashActivity;

public class LoginActivity extends MvpActivity<LoginView, LoginPresenter> implements LoginView {

    private ActivityLoginBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setView(getMvpView());
        presenter.onStart();
    }

    @Override
    protected void onDestroy() {
        presenter.onStop();
        super.onDestroy();
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void onLogin() {
        presenter.login(binding.etUsername.getText().toString(), binding.etPassword.getText().toString());
    }

    @Override
    public void onRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Logging in...");
        }
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void onLoginSuccess() {
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }
}
