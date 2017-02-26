package com.tip.theboss.ui.applicants.detail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.tip.theboss.R;
import com.tip.theboss.app.Constants;
import com.tip.theboss.databinding.ActivityApplicantDetailBinding;
import com.tip.theboss.model.data.Applicant;
import com.tip.theboss.ui.rating.form.RatingFormActivity;
import com.tip.theboss.ui.rating.list.RatingListFragment;

public class ApplicantDetailActivity
        extends MvpViewStateActivity<ApplicantDetailView, ApplicantDetailPresenter>
        implements ApplicantDetailView {

    private ActivityApplicantDetailBinding binding;
    private int applicantId;
    private ProgressDialog progressDialog;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_applicant_detail);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.setView(getMvpView());
        applicantId = getIntent().getIntExtra(Constants.ID, -1);
        if (applicantId == -1) {
            Toast.makeText(getApplicationContext(), "No Intent Extra Found", Toast.LENGTH_SHORT).show();
            finish();
        }
        fragmentManager = getSupportFragmentManager();
        presenter.onStart(applicantId);

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
    public ApplicantDetailPresenter createPresenter() {
        return new ApplicantDetailPresenter();
    }

    @NonNull
    @Override
    public ViewState<ApplicantDetailView> createViewState() {
        return new ApplicantDetailViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        presenter.refresh(applicantId);
    }

    @Override
    public void setApplicant(Applicant applicant) {
        binding.setApplicant(applicant);
        changeFragment(RatingListFragment.newInstance(Constants.TYPE_RENDER, applicant.getUsername()),
                RatingListFragment.class.getSimpleName(),
                false);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAccept(Applicant applicant) {
        presenter.update(applicant.getId(), true);
    }

    @Override
    public void onCancel(Applicant applicant) {
        presenter.update(applicant.getId(), false);
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
    public void updateSuccess(String message) {
        new AlertDialog.Builder(this)
                .setTitle(message)
                .setCancelable(false)
                .setPositiveButton("Close", null)
                .show();
    }

    @Override
    public void setAcceptEnable(boolean acceptEnable) {
        binding.setAcceptEnable(acceptEnable);
        Log.d("APPLY", "onChange: accept: " + String.valueOf(acceptEnable));
    }

    @Override
    public void onAddRating(Applicant applicant) {
        Intent intent = new Intent(this, RatingFormActivity.class);
        intent.putExtra(Constants.RATE_USERNAME, applicant.getUsername());
        intent.putExtra(Constants.FULL_NAME, applicant.getFullName());
        intent.putExtra(Constants.TYPE, Constants.TYPE_RENDER);
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
