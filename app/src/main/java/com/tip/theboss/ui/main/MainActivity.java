package com.tip.theboss.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tip.theboss.R;
import com.tip.theboss.databinding.ActivityMainBinding;
import com.tip.theboss.databinding.NavHeaderMainBinding;
import com.tip.theboss.model.data.Classification;
import com.tip.theboss.model.data.User;
import com.tip.theboss.ui.classifications.ClassificationsActivity;
import com.tip.theboss.ui.jobs.form.JobFormActivity;
import com.tip.theboss.ui.jobs.list.JobListActivity;
import com.tip.theboss.ui.login.LoginActivity;
import com.tip.theboss.ui.profile.ProfileActivity;

import java.util.List;

public class MainActivity extends MvpActivity<MainView, MainPresenter>
        implements MainView, NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private NavHeaderMainBinding navHeaderMainBinding;
    private MainTabAdapter mainTabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.appBarMain.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout,
                binding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);
        navHeaderMainBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0));

        mainTabAdapter = new MainTabAdapter(getSupportFragmentManager());
        binding.appBarMain.viewPager.setAdapter(mainTabAdapter);
        binding.appBarMain.tabLayout.setupWithViewPager(binding.appBarMain.viewPager);

        presenter.onStart();
    }

    @Override
    protected void onDestroy() {
        presenter.onStop();
        super.onDestroy();
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search_jobs) {
            startActivity(new Intent(this, ClassificationsActivity.class));
        } else if (id == R.id.nav_create_new_job) {
            startActivity(new Intent(this, JobFormActivity.class));
        } else if (id == R.id.nav_my_job_posted) {
            String user = presenter.getUser();
            if (user != null) {
                Intent intent = new Intent(this, JobListActivity.class);
                intent.putExtra("has_search", true);
                intent.putExtra("username", user);
                intent.putExtra("open", false);
                startActivity(intent);
            } else {
                showMessage("User not yet loaded");
            }
        } else if (id == R.id.nav_my_job_applications) {
            Intent intent = new Intent(this, JobListActivity.class);
            intent.putExtra("has_search", true);
            intent.putExtra("open", false);
            intent.putExtra("apply", true);
            startActivity(intent);
        } else if (id == R.id.nav_my_account) {
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (id == R.id.nav_logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            presenter.logout();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setUser(User user) {
        navHeaderMainBinding.setUser(user);
    }

    @Override
    public void setClassifications(List<Classification> classifications) {
        mainTabAdapter.setClassifications(classifications);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLogout() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
