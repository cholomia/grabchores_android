package com.tip.theboss.ui.profile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tip.theboss.ui.rating.list.RatingListFragment;

/**
 * Created by Cholo Mia on 2/26/2017.
 */

class ProfileTabAdapter extends FragmentStatePagerAdapter {

    private final static String[] TITLES = {"AS AVAILER", "AS RENDER"};
    private final static int[] TYPE = {1, 2};
    private String username;

    ProfileTabAdapter(FragmentManager fm, String username) {
        super(fm);
        this.username = username;
    }

    @Override
    public Fragment getItem(int position) {
        return RatingListFragment.newInstance(TYPE[position], username);
    }

    @Override
    public int getCount() {
        return TYPE.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
