package com.tip.theboss.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tip.theboss.model.data.Classification;
import com.tip.theboss.ui.jobs.list.JobListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cholo Mia on 2/11/2017.
 */

public class MainTabAdapter extends FragmentStatePagerAdapter {

    private List<Classification> classifications;

    public MainTabAdapter(FragmentManager fm) {
        super(fm);
        classifications = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return JobListFragment.newInstance(false, classifications.get(position).getId(), null, true,
                false);
    }

    @Override
    public int getCount() {
        return classifications.size();
    }

    public void setClassifications(List<Classification> classifications) {
        this.classifications.clear();
        this.classifications.addAll(classifications);
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return classifications.get(position).getTitle();
    }
}
