package com.tip.theboss.ui.jobs.detail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tip.theboss.R;
import com.tip.theboss.databinding.ActivityJobDetailBinding;
import com.tip.theboss.model.data.Job;

public class JobDetailActivity extends MvpActivity<JobDetailView, JobDetailPresenter>
        implements JobDetailView {

    private ActivityJobDetailBinding binding;

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

    }

    @Override
    public void onApply(Job job) {

    }

    @Override
    public void setJob(Job job) {
        binding.setJob(job);
    }
}
