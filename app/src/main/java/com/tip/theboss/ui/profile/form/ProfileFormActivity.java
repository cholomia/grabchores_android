package com.tip.theboss.ui.profile.form;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tip.theboss.R;
import com.tip.theboss.databinding.ActivityProfileFormBinding;
import com.tip.theboss.model.data.User;

public class ProfileFormActivity extends MvpActivity<ProfileFormView, ProfileFormPresenter>
        implements ProfileFormView {

    private ActivityProfileFormBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_form);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    public ProfileFormPresenter createPresenter() {
        return new ProfileFormPresenter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSubmit() {
        presenter.submit(
                binding.etFirstName.getText().toString(),
                binding.etLastName.getText().toString(),
                binding.etMobileNumber.getText().toString()
        );
    }

    @Override
    public void startLoad() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Connecting...");
        }
        progressDialog.show();
    }

    @Override
    public void stopLoad() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateSuccess() {
        new AlertDialog.Builder(this)
                .setTitle("Update Info Success")
                .setCancelable(false)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ProfileFormActivity.this.finish();
                    }
                })
                .show();
    }

    @Override
    public void setUser(User user) {
        binding.etFirstName.setText(user.getFirstName());
        binding.etLastName.setText(user.getLastName());
        binding.etMobileNumber.setText(user.getUserProfile().getMobileNumber());
    }
}
