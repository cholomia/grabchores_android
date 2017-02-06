package com.tip.theboss.ui.menu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tip.theboss.ui.jobs.create.JobFormFragment;
import com.tip.theboss.ui.jobs.list.JobListFragment;
import com.tip.theboss.ui.profile.ProfileFragment;

/**
 * @author pocholomia
 * @since 26/01/2017
 */

class MainTabAdapter extends FragmentStatePagerAdapter {

    private static final String[] TITLES = {"Avail Job", "Post Job", "Profile"};

    MainTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new JobListFragment();
            case 1:
                return new JobFormFragment();
            case 2:
                return new ProfileFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
