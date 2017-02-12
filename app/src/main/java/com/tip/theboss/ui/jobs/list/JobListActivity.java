package com.tip.theboss.ui.jobs.list;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.tip.theboss.R;

public class JobListActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        boolean hasSearch = getIntent().getBooleanExtra("has_search", false);
        int classification = getIntent().getIntExtra("classification", -1);
        String classificationTitle = getIntent().getStringExtra("classification_title");
        String username = getIntent().getStringExtra("username");
        boolean open = getIntent().getBooleanExtra("open", true);
        boolean apply = getIntent().getBooleanExtra("apply", false);
        if (classification != -1 && classificationTitle != null && getSupportActionBar() != null)
            getSupportActionBar().setTitle(classificationTitle);
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
