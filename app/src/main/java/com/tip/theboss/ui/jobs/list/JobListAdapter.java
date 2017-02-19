package com.tip.theboss.ui.jobs.list;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tip.theboss.R;
import com.tip.theboss.databinding.ItemJobBinding;
import com.tip.theboss.databinding.ItemMoreBinding;
import com.tip.theboss.model.data.Job;
import com.tip.theboss.ui.base.MoreViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 26/01/2017
 */

class JobListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MORE = 1;
    private static final int VIEW_TYPE_DEFAULT = 0;

    private String nextUrl;
    private boolean loading;
    private JobListView view;
    private List<Job> jobs;

    JobListAdapter(JobListView view) {
        this.view = view;
        jobs = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (nextUrl != null && !nextUrl.isEmpty() && position == getItemCount() - 1)
            return VIEW_TYPE_MORE;
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_MORE:
                ItemMoreBinding itemMoreBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()), R.layout.item_more, parent, false);
                return new MoreViewHolder(itemMoreBinding);
            case VIEW_TYPE_DEFAULT:
                ItemJobBinding itemJobBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()), R.layout.item_job, parent, false);
                return new JobViewHolder(itemJobBinding);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MORE:
                MoreViewHolder moreViewHolder = (MoreViewHolder) holder;
                moreViewHolder.itemMoreBinding.setUrl(nextUrl);
                moreViewHolder.itemMoreBinding.setView(view);
                moreViewHolder.itemMoreBinding.setLoading(loading);
                break;
            case VIEW_TYPE_DEFAULT:
                JobViewHolder jobViewHolder = (JobViewHolder) holder;
                jobViewHolder.itemJobBinding.setJob(jobs.get(position));
                jobViewHolder.itemJobBinding.setView(view);
                break;
        }
    }

    @Override
    public int getItemCount() {
        int count = jobs.size();
        if (nextUrl != null && !nextUrl.isEmpty())
            count++;
        return count;
    }

    void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
        notifyDataSetChanged();
    }

    public void setJobs(List<Job> jobs) {
        this.jobs.clear();
        this.jobs.addAll(jobs);
        notifyDataSetChanged();
    }

    void setLoading(boolean loading) {
        this.loading = loading;
        notifyDataSetChanged();
    }

    private class JobViewHolder extends RecyclerView.ViewHolder {
        private final ItemJobBinding itemJobBinding;

        JobViewHolder(ItemJobBinding itemJobBinding) {
            super(itemJobBinding.getRoot());
            this.itemJobBinding = itemJobBinding;
        }
    }
}
