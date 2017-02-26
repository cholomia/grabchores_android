package com.tip.theboss.ui.poster;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.tip.theboss.R;
import com.tip.theboss.app.Constants;
import com.tip.theboss.databinding.ActivityPosterBinding;
import com.tip.theboss.ui.jobs.list.JobListActivity;
import com.tip.theboss.ui.rating.form.RatingFormActivity;
import com.tip.theboss.ui.rating.list.RatingListFragment;

public class PosterActivity extends AppCompatActivity implements PosterView {

    private ActivityPosterBinding binding;
    private String username;
    private String fullName;
    private String email;
    private String mobileNumber;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_poster);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = getIntent().getStringExtra(Constants.USERNAME);
        fullName = getIntent().getStringExtra(Constants.FULL_NAME);
        email = getIntent().getStringExtra(Constants.EMAIL);
        mobileNumber = getIntent().getStringExtra(Constants.MOBILE_NUMBER);

        if (username == null || fullName == null || email == null || mobileNumber == null) {
            Toast.makeText(getApplicationContext(), "No intent extra found", Toast.LENGTH_SHORT).show();
            finish();
        }

        binding.setFullName(fullName);
        binding.setEmail(email);
        binding.setMobileNumber(mobileNumber);
        binding.setView(this);

        fragmentManager = getSupportFragmentManager();
        changeFragment(RatingListFragment.newInstance(Constants.TYPE_AVAIL, username),
                RatingListFragment.class.getSimpleName(),
                false);
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

    @Override
    public void onAddRating() {
        Intent intent = new Intent(this, RatingFormActivity.class);
        intent.putExtra(Constants.RATE_USERNAME, username);
        intent.putExtra(Constants.FULL_NAME, fullName);
        intent.putExtra(Constants.TYPE, Constants.TYPE_AVAIL);
        startActivity(intent);
    }

    @Override
    public void onViewPosts() {
        Intent intent = new Intent(this, JobListActivity.class);
        intent.putExtra(Constants.HAS_SEARCH, true);
        intent.putExtra(Constants.USERNAME, username);
        startActivity(intent);
    }

    public void changeFragment(Fragment fragment, String tag, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layout_reviews, fragment, tag);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        if (addToBackStack)
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

}
