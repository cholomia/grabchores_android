package com.tip.theboss.ui.jobs.list;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.tip.theboss.R;
import com.tip.theboss.app.Constants;

public class JobListActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        boolean hasSearch = getIntent().getBooleanExtra(Constants.HAS_SEARCH, false);
        int classification = getIntent().getIntExtra(Constants.CLASSIFICATION, -1);
        String classificationTitle = getIntent().getStringExtra(Constants.CLASSIFICATION_TITLE);
        String username = getIntent().getStringExtra(Constants.USERNAME);
        boolean open = getIntent().getBooleanExtra(Constants.OPEN, true);
        boolean apply = getIntent().getBooleanExtra(Constants.APPLY, false);
        if (getSupportActionBar() != null) {
            if (classificationTitle != null && username != null) {
                getSupportActionBar().setTitle(username);
                getSupportActionBar().setSubtitle(classificationTitle);
            } else if (username != null) {
                getSupportActionBar().setTitle(username);
            } else if (classificationTitle != null) {
                getSupportActionBar().setTitle(classificationTitle);
            }
            if (apply) {
                getSupportActionBar().setTitle("My Job Applications");
            }
        }

        fragmentManager = getSupportFragmentManager();
        changeFragment(JobListFragment.newInstance(hasSearch, classification, username, open, apply),
                JobListFragment.class.getSimpleName(), false);
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

    public void changeFragment(Fragment fragment, String tag, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_job_list, fragment, tag);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        if (addToBackStack)
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }
}
