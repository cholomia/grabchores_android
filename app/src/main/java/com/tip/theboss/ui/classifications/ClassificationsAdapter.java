package com.tip.theboss.ui.classifications;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tip.theboss.R;
import com.tip.theboss.databinding.ItemClassificationsBinding;
import com.tip.theboss.model.data.Classification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cholo Mia on 2/11/2017.
 */

class ClassificationsAdapter extends RecyclerView.Adapter<ClassificationsAdapter.ViewHolder> {

    private List<Classification> classifications;
    private ClassificationsView view;

    ClassificationsAdapter(ClassificationsView view) {
        this.view = view;
        classifications = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemClassificationsBinding itemClassificationsBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_classifications, parent,
                false);
        return new ViewHolder(itemClassificationsBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemClassificationsBinding.setClassification(classifications.get(position));
        holder.itemClassificationsBinding.setView(view);
    }

    @Override
    public int getItemCount() {
        return classifications.size();
    }

    public void setClassifications(List<Classification> classifications) {
        this.classifications.clear();
        this.classifications.addAll(classifications);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemClassificationsBinding itemClassificationsBinding;

        ViewHolder(ItemClassificationsBinding itemClassificationsBinding) {
            super(itemClassificationsBinding.getRoot());
            this.itemClassificationsBinding = itemClassificationsBinding;
        }
    }
}
