package com.tip.theboss.ui.jobs.list;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.tip.theboss.R;
import com.tip.theboss.databinding.FragmentJobListBinding;
import com.tip.theboss.model.data.Job;
import com.tip.theboss.ui.jobs.detail.JobDetailActivity;
import com.tip.theboss.util.StringUtils;

import java.util.List;
import java.util.Map;


public class JobListFragment extends MvpViewStateFragment<JobListView, JobListPresenter>
        implements JobListView, SwipeRefreshLayout.OnRefreshListener {

    private FragmentJobListBinding binding;
    private JobListAdapter adapter;

    @NonNull
    @Override
    public JobListPresenter createPresenter() {
        return new JobListPresenter();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_job_list, container, false);
        adapter = new JobListAdapter(getMvpView());
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        return binding.getRoot();
    }

    @NonNull
    @Override
    public ViewState createViewState() {
        setRetainInstance(true);
        return new JobListViewState();
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
    public void stopLoading() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addNext(String nextUrl) {
        adapter.setNextUrl(nextUrl);
    }

    @Override
    public void setJobs(List<Job> jobs) {
        adapter.setJobs(jobs);
    }

    @Override
    public void onJobItemClicked(Job job) {
        Intent intent = new Intent(getContext(), JobDetailActivity.class);
        intent.putExtra("id", job.getId());
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
