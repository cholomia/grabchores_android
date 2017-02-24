package com.tip.theboss.ui.applicants.list;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.tip.theboss.R;
import com.tip.theboss.app.Constants;
import com.tip.theboss.databinding.ActivityApplicantsBinding;
import com.tip.theboss.model.data.Applicant;
import com.tip.theboss.ui.applicants.detail.ApplicantDetailActivity;
import com.tip.theboss.util.StringUtils;

import java.util.List;
import java.util.Map;

public class ApplicantsActivity extends MvpViewStateActivity<ApplicantsView, ApplicantsPresenter>
        implements ApplicantsView, SwipeRefreshLayout.OnRefreshListener {

    private ActivityApplicantsBinding binding;
    private ApplicantsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_applicants);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new ApplicantsListAdapter(getMvpView());
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        int jobId = getIntent().getIntExtra(Constants.JOB, -1);
        if (jobId == -1) {
            Toast.makeText(getApplicationContext(), "No Intent Extra Found!", Toast.LENGTH_SHORT)
                    .show();
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
    public ApplicantsPresenter createPresenter() {
        return new ApplicantsPresenter();
    }

    @NonNull
    @Override
    public ViewState<ApplicantsView> createViewState() {
        return new ApplicantsViewState();
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
        presenter.refresh();
    }

    @Override
    public void setApplicantList(List<Applicant> applicants) {
        adapter.setApplicantList(applicants);
    }

    @Override
    public void stopLoading() {
        binding.swipeRefreshLayout.setRefreshing(false);
        adapter.setLoading(false);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addNext(String nextUrl) {
        adapter.setNextUrl(nextUrl);
    }

    @Override
    public void onApplicantClick(Applicant applicant) {
        Intent intent = new Intent(this, ApplicantDetailActivity.class);
        intent.putExtra(Constants.ID, applicant.getId());
        startActivity(intent);
    }

    @Override
    public void onMore(final String nextUrl) {
        binding.swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                binding.swipeRefreshLayout.setRefreshing(true);
                adapter.setLoading(true);
                Map<String, String> parameters = StringUtils.getParamsFromUrl(nextUrl);
                if (parameters.size() > 0) presenter.refresh(parameters);
                else presenter.refresh();
            }
        });
    }
}
