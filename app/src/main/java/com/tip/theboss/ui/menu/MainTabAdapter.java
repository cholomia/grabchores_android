package com.tip.theboss.ui.menu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * @author pocholomia
 * @since 26/01/2017
 */

public class MainTabAdapter extends FragmentStatePagerAdapter {

    private static final String[] TITLES = {"Avail Job", "Post Job", "Profile"};

    public MainTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
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
