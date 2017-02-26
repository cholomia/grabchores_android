package com.tip.theboss.ui.rating.form;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tip.theboss.R;
import com.tip.theboss.app.Constants;
import com.tip.theboss.databinding.ActivityRatingFormBinding;

public class RatingFormActivity extends MvpActivity<RatingFormView, RatingFormPresenter>
        implements RatingFormView {

    private ActivityRatingFormBinding binding;
    private String rateUsername;
    private String fullName;
    private int typeRating;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rating_form);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rateUsername = getIntent().getStringExtra(Constants.RATE_USERNAME);
        fullName = getIntent().getStringExtra(Constants.FULL_NAME);
        typeRating = getIntent().getIntExtra(Constants.TYPE, -1);

        if (rateUsername == null || fullName == null || typeRating == -1) {
            Toast.makeText(getApplicationContext(), "Incorrect Intent Extra", Toast.LENGTH_SHORT).show();
            finish();
        }
        binding.setRateUsername(fullName);
        binding.setView(getMvpView());
        presenter.onStart();
    }

    @Override
    protected void onDestroy() {
        presenter.onStop();
        super.onDestroy();
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

    @NonNull
    @Override
    public RatingFormPresenter createPresenter() {
        return new RatingFormPresenter();
    }

    @Override
    public void onSubmit() {
        presenter.newRating(rateUsername, Math.round(binding.ratingBar.getRating()), typeRating,
                binding.etComment.getText().toString());
    }

    @Override
    public void startLoad() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Connecting...");
        }
        progressDialog.show();
    }

    @Override
    public void stopLoad() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void createRatingSuccess() {
        new AlertDialog.Builder(this)
                .setTitle("Submit rating successful")
                .setCancelable(false)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RatingFormActivity.this.finish();
                    }
                })
                .show();
    }
}
