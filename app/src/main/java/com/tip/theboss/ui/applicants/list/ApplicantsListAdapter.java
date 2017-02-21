package com.tip.theboss.ui.applicants.list;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tip.theboss.R;
import com.tip.theboss.databinding.ItemApplicantBinding;
import com.tip.theboss.databinding.ItemMoreBinding;
import com.tip.theboss.model.data.Applicant;
import com.tip.theboss.ui.base.MoreViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pocholomia
 * @since 21/02/2017
 */

class ApplicantsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MORE = 1;
    private static final int VIEW_TYPE_DEFAULT = 0;

    private final List<Applicant> applicantList;
    private ApplicantsView applicantsView;

    private String nextUrl;
    private boolean loading;

    ApplicantsListAdapter(ApplicantsView applicantsView) {
        this.applicantsView = applicantsView;
        applicantList = new ArrayList<>();
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
                ItemApplicantBinding itemApplicantBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()), R.layout.item_applicant, parent, false);
                return new ApplicantViewHolder(itemApplicantBinding);
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
                moreViewHolder.itemMoreBinding.setView(applicantsView);
                moreViewHolder.itemMoreBinding.setLoading(loading);
                break;
            case VIEW_TYPE_DEFAULT:
                ApplicantViewHolder applicantViewHolder = (ApplicantViewHolder) holder;
                applicantViewHolder.itemApplicantBinding.setApplicant(applicantList.get(position));
                applicantViewHolder.itemApplicantBinding.setView(applicantsView);
                break;
        }
    }

    @Override
    public int getItemCount() {
        int count = applicantList.size();
        if (nextUrl != null && !nextUrl.isEmpty())
            count++;
        return count;
    }

    void setApplicantList(List<Applicant> applicantList) {
        this.applicantList.clear();
        this.applicantList.addAll(applicantList);
        notifyDataSetChanged();
    }

    void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
        notifyDataSetChanged();
    }

    void setLoading(boolean loading) {
        this.loading = loading;
        notifyDataSetChanged();
    }

    private class ApplicantViewHolder extends RecyclerView.ViewHolder {
        private ItemApplicantBinding itemApplicantBinding;

        ApplicantViewHolder(ItemApplicantBinding itemApplicantBinding) {
            super(itemApplicantBinding.getRoot());
            this.itemApplicantBinding = itemApplicantBinding;
        }
    }
}
