package com.tip.theboss.ui.jobs.detail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tip.theboss.R;
import com.tip.theboss.databinding.ActivityJobDetailBinding;
import com.tip.theboss.model.data.Job;
import com.tip.theboss.ui.jobs.list.JobListActivity;

public class JobDetailActivity extends MvpActivity<JobDetailView, JobDetailPresenter>
        implements JobDetailView {

    private ActivityJobDetailBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_detail);
        binding.setView(getMvpView());
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int jobId = getIntent().getIntExtra("id", -1);
        if (jobId == -1) {
            Toast.makeText(getApplicationContext(), "No Intent Extra Found", Toast.LENGTH_SHORT).show();
            finish();
        }
        presenter.onStart(jobId);
    }

    @Override
    protected void onDestroy() {
        presenter.onStop();
        super.onDestroy();
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

    @NonNull
    @Override
    public JobDetailPresenter createPresenter() {
        return new JobDetailPresenter();
    }

    @Override
    public void onUserClick(String user) {

    }

    @Override
    public void onClassificationClick(int classificationId) {
        Intent intent = new Intent(this, JobListActivity.class);
        intent.putExtra("has_search", true);
        intent.putExtra("classification", classificationId);
        startActivity(intent);
    }

    @Override
    public void onApply(Job job) {
        presenter.applyJob(job);
    }

    @Override
    public void setJob(Job job) {
        binding.setJob(job);
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
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onApplySuccess() {
        new AlertDialog.Builder(this)
                .setTitle("Job Application Sent")
                .setCancelable(false)
                .setPositiveButton("Close", null)
                .show();
    }
}
