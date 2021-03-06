package com.tip.theboss.ui.jobs.list;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.tip.theboss.R;
import com.tip.theboss.app.Constants;
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
    private boolean hasSearch;
    private int classification;
    private String username;
    private boolean open;
    private boolean apply;

    public JobListFragment() {
    }

    public static JobListFragment newInstance(boolean hasSearch, int classification, String username,
                                              boolean open, boolean apply) {
        JobListFragment jobListFragment = new JobListFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.HAS_SEARCH, hasSearch);
        args.putInt(Constants.CLASSIFICATION, classification);
        args.putString(Constants.USERNAME, username);
        args.putBoolean(Constants.OPEN, open);
        args.putBoolean(Constants.APPLY, apply);
        jobListFragment.setArguments(args);
        return jobListFragment;
    }

    @NonNull
    @Override
    public JobListPresenter createPresenter() {
        return new JobListPresenter();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            hasSearch = getArguments().getBoolean(Constants.HAS_SEARCH, false);
            classification = getArguments().getInt(Constants.CLASSIFICATION, -1);
            username = getArguments().getString(Constants.USERNAME);
            open = getArguments().getBoolean(Constants.OPEN, true);
            apply = getArguments().getBoolean(Constants.APPLY, false);
        }
        if (hasSearch) setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_job_list, container, false);
        adapter = new JobListAdapter(getMvpView());
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0)
                        ? 0 : recyclerView.getChildAt(0).getTop();
                binding.swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart(classification, username, open, apply);
    }

    @Override
    public void onStop() {
        presenter.onStop();
        super.onStop();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_job_list, menu);
        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.setQuery(query, true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.setQuery(newText, false);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onRefresh() {
        presenter.refresh();
    }

    @Override
    public void stopLoading() {
        binding.swipeRefreshLayout.setRefreshing(false);
        adapter.setLoading(false);
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
        intent.putExtra(Constants.ID, job.getId());
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
