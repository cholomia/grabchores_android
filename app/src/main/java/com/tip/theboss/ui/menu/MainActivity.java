package com.tip.theboss.ui.menu;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.tip.theboss.R;
import com.tip.theboss.databinding.ActivityMainBinding;
import com.tip.theboss.ui.jobs.list.JobListActivity;

public class MainActivity extends AppCompatActivity {


    private MainTabAdapter mainTabAdapter;

    //private ViewPager mViewPager;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.toolbar);

        mainTabAdapter = new MainTabAdapter(getSupportFragmentManager());
        binding.container.setAdapter(mainTabAdapter);
        binding.tabs.setupWithViewPager(binding.container);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent = new Intent(this, JobListActivity.class);
                intent.putExtra("has_search", true);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
