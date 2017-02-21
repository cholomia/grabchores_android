package com.tip.theboss.ui.jobs.detail;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.tip.theboss.R;
import com.tip.theboss.app.Constants;
import com.tip.theboss.databinding.ActivityJobDetailBinding;
import com.tip.theboss.model.data.Job;
import com.tip.theboss.ui.applicants.list.ApplicantsActivity;
import com.tip.theboss.ui.jobs.form.JobFormActivity;
import com.tip.theboss.ui.jobs.list.JobListActivity;

public class JobDetailActivity extends MvpViewStateActivity<JobDetailView, JobDetailPresenter>
        implements JobDetailView, SwipeRefreshLayout.OnRefreshListener {

    private ActivityJobDetailBinding binding;
    private ProgressDialog progressDialog;
    private int jobId;
    private boolean owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_detail);
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        binding.setView(getMvpView());
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        jobId = getIntent().getIntExtra(Constants.ID, -1);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        if (owner) getMenuInflater().inflate(R.menu.menu_job_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(this, JobFormActivity.class);
                intent.putExtra(Constants.ID, jobId);
                startActivity(intent);
                return true;
            case R.id.action_delete:
                presenter.deleteJob(jobId);
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
        // TODO: 2/19/2017 start activity of user profile instead of job list posted by user
        Intent intent = new Intent(this, JobListActivity.class);
        intent.putExtra(Constants.HAS_SEARCH, true);
        intent.putExtra(Constants.USERNAME, user);
        startActivity(intent);
    }

    @Override
    public void onClassificationClick(int classificationId, String classificationTitle) {
        Intent intent = new Intent(this, JobListActivity.class);
        intent.putExtra(Constants.HAS_SEARCH, true);
        intent.putExtra(Constants.CLASSIFICATION, classificationId);
        intent.putExtra(Constants.CLASSIFICATION_TITLE, classificationTitle);
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
            progressDialog.setMessage("Connecting...");
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

    @Override
    public void setEnableApply(boolean enableApply) {
        binding.setEnableApply(enableApply);
    }

    @Override
    public void stopPullLoading() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDeleteSuccess() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Successful")
                .setCancelable(false)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        JobDetailActivity.this.finish();
                    }
                })
                .show();
    }

    @Override
    public void onViewApplicants(Job job) {
        Intent intent = new Intent(this, ApplicantsActivity.class);
        intent.putExtra(Constants.JOB, job.getId());
        startActivity(intent);
    }

    @Override
    public void setEnableViewApplicant(boolean enableViewApplicant) {
        this.owner = enableViewApplicant;
        binding.setEnableViewApplicants(enableViewApplicant);
        invalidateOptionsMenu();
    }

    @Override
    public void onCancelApplication(Job job) {
        presenter.cancelApplication(job);
    }

    @Override
    public void setEnableCancelApplication(boolean enableCancelApplication) {
        binding.setEnableCancelApplication(enableCancelApplication);
    }

    @NonNull
    @Override
    public ViewState<JobDetailView> createViewState() {
        return new JobDetailViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        binding.swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                binding.swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    @Override
    public void onRefresh() {
        presenter.refresh(jobId);
    }

}
