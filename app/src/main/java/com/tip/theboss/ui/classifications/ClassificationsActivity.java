package com.tip.theboss.ui.classifications;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.tip.theboss.R;
import com.tip.theboss.app.Constants;
import com.tip.theboss.databinding.ActivityClassificationsBinding;
import com.tip.theboss.model.data.Classification;
import com.tip.theboss.ui.jobs.list.JobListActivity;

import java.util.List;

public class ClassificationsActivity
        extends MvpViewStateActivity<ClassificationsView, ClassificationsPresenter>
        implements ClassificationsView, SwipeRefreshLayout.OnRefreshListener {

    private ActivityClassificationsBinding binding;
    private ClassificationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_classifications);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new ClassificationsAdapter(getMvpView());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this,
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
        presenter.onStart();
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
    protected void onDestroy() {
        presenter.onStop();
        super.onDestroy();
    }

    @NonNull
    @Override
    public ClassificationsPresenter createPresenter() {
        return new ClassificationsPresenter();
    }

    @Override
    public void setClassifications(List<Classification> classifications) {
        Classification classification = new Classification();
        classification.setId(-1);
        classification.setTitle("All");
        classifications.add(0, classification);
        adapter.setClassifications(classifications);
    }

    @Override
    public void stopLoad() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClassificationClick(Classification classification) {
        Intent intent = new Intent(this, JobListActivity.class);
        intent.putExtra(Constants.HAS_SEARCH, true);
        intent.putExtra(Constants.CLASSIFICATION, classification.getId());
        intent.putExtra(Constants.CLASSIFICATION_TITLE, classification.getTitle());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        presenter.refresh();
    }

    @NonNull
    @Override
    public ViewState<ClassificationsView> createViewState() {
        return new ClassificationViewState();
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
}
