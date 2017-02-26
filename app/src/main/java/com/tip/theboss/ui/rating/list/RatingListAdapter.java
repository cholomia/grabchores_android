package com.tip.theboss.ui.rating.list;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tip.theboss.R;
import com.tip.theboss.databinding.ItemMoreBinding;
import com.tip.theboss.databinding.ItemRatingsBinding;
import com.tip.theboss.model.data.Rating;
import com.tip.theboss.ui.base.MoreViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cholo Mia on 2/26/2017.
 */

class RatingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MORE = 1;
    private static final int VIEW_TYPE_DEFAULT = 0;

    private List<Rating> ratings;
    private RatingListView ratingListView;

    private String nextUrl;
    private boolean loading;

    RatingListAdapter(RatingListView ratingListView) {
        this.ratingListView = ratingListView;
        ratings = new ArrayList<>();
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
                ItemRatingsBinding itemRatingsBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()), R.layout.item_ratings, parent, false);
                return new RatingViewHolder(itemRatingsBinding);
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
                moreViewHolder.itemMoreBinding.setView(ratingListView);
                moreViewHolder.itemMoreBinding.setLoading(loading);
                break;
            case VIEW_TYPE_DEFAULT:
                RatingViewHolder ratingViewHolder = (RatingViewHolder) holder;
                ratingViewHolder.itemRatingsBinding.setRating(ratings.get(position));
                ratingViewHolder.itemRatingsBinding.setView(ratingListView);
                break;
        }
    }

    @Override
    public int getItemCount() {
        int count = ratings.size();
        if (nextUrl != null && !nextUrl.isEmpty())
            count++;
        return count;
    }

    void setRatings(List<Rating> ratings) {
        this.ratings.clear();
        this.ratings.addAll(ratings);
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

    private class RatingViewHolder extends RecyclerView.ViewHolder {
        private ItemRatingsBinding itemRatingsBinding;

        RatingViewHolder(ItemRatingsBinding itemRatingsBinding) {
            super(itemRatingsBinding.getRoot());
            this.itemRatingsBinding = itemRatingsBinding;
        }
    }
}
