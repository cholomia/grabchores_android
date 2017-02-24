package com.tip.theboss.ui.jobs.form;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.tip.theboss.R;
import com.tip.theboss.app.Constants;
import com.tip.theboss.databinding.ActivityJobFormBinding;
import com.tip.theboss.model.data.Classification;
import com.tip.theboss.model.data.Job;
import com.tip.theboss.util.DateTimeUtils;

import java.util.Calendar;
import java.util.List;


public class JobFormActivity extends MvpActivity<JobFormView, JobFormPresenter>
        implements JobFormView, CompoundButton.OnCheckedChangeListener {

    private ActivityJobFormBinding binding;
    private ProgressDialog progressDialog;
    private List<Classification> classifications;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_form);

        binding.setView(getMvpView());
        binding.checkboxSingleDay.setOnCheckedChangeListener(this);

        id = getIntent().getIntExtra(Constants.ID, -1);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(id == -1 ? "Create New Job" : "Update Job");
        }
        presenter.onStart(id);
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
    protected void onDestroy() {
        presenter.onStop();
        super.onDestroy();
    }

    @NonNull
    @Override
    public JobFormPresenter createPresenter() {
        return new JobFormPresenter();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        binding.txtTo.setVisibility(b ? View.GONE : View.VISIBLE);
        binding.txtDateEnd.setVisibility(b ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onPost() {
        int classificationItemPosition = binding.spinnerClassification.getSelectedItemPosition();
        if (classifications == null || classificationItemPosition == -1
                || classificationItemPosition > classifications.size()) {
            showMessage("Invalid Classification");
            return;
        }
        presenter.addJob(id,
                binding.etTitle.getText().toString(),
                binding.etDescription.getText().toString(),
                classifications.get(classificationItemPosition).getId(),
                binding.etLocation.getText().toString(),
                binding.etFee.getText().toString(),
                binding.txtDateStart.getText().toString(),
                binding.txtDateEnd.getText().toString());
    }

    @Override
    public void onSetDate(final View view) {
        Calendar calendar = null;
        if (view.getId() == binding.txtDateStart.getId()) {
            calendar = DateTimeUtils.convertTransactionStringDate(
                    binding.txtDateStart.getText().toString(), DateTimeUtils.DATE_ONLY);
        } else if (view.getId() == binding.txtDateEnd.getId()) {
            calendar = DateTimeUtils.convertTransactionStringDate(
                    binding.txtDateEnd.getText().toString(), DateTimeUtils.DATE_ONLY);
        }
        if (calendar == null) calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.YEAR, i);
                        calendar1.set(Calendar.MONTH, i1);
                        calendar1.set(Calendar.DAY_OF_MONTH, i2);
                        String dateString = DateTimeUtils.convertDateToString("MM/dd/yy", calendar1);
                        if (view.getId() == binding.txtDateStart.getId()) {
                            binding.txtDateStart.setText(dateString);
                            if (binding.checkboxSingleDay.isChecked())
                                binding.txtDateEnd.setText(dateString);
                        } else if (view.getId() == binding.txtDateEnd.getId()
                                || binding.checkboxSingleDay.isChecked()) {
                            binding.txtDateEnd.setText(dateString);
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Connecting...");
        }
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void onCreateSuccess() {
        binding.etTitle.setText("");
        binding.etDescription.setText("");
        binding.spinnerClassification.setSelection(0);
        binding.etLocation.setText("");
        binding.etFee.setText("");
        binding.txtDateStart.setText("");
        binding.txtDateEnd.setText("");
        new AlertDialog.Builder(this)
                .setTitle("Job Posted!")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        JobFormActivity.this.finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void setClassifications(List<Classification> classifications) {
        this.classifications = classifications;
        String[] spinnerArray = new String[classifications.size()];
        for (int i = 0; i < classifications.size(); i++) {
            spinnerArray[i] = classifications.get(i).getTitle();
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerClassification.setAdapter(spinnerArrayAdapter);
    }

    @Override
    public void setJob(Job job) {
        binding.etTitle.setText(job.getTitle());
        binding.etDescription.setText(job.getDescription());
        for (int i = 0; i < classifications.size(); i++) {
            if (job.getClassification() == classifications.get(i).getId()) {
                binding.spinnerClassification.setSelection(i);
                break;
            }
        }
        binding.etLocation.setText(job.getLocation());
        binding.etFee.setText(String.valueOf(job.getFee()));
        binding.txtDateStart.setText(job.getDateStart());
        binding.txtDateEnd.setText(job.getDateEnd());
        binding.checkboxSingleDay.setChecked(job.getDateStart().contentEquals(job.getDateEnd()));
    }
}
