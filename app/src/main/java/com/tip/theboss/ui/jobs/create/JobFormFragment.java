package com.tip.theboss.ui.jobs.create;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.tip.theboss.R;
import com.tip.theboss.databinding.FragmentJobFormBinding;
import com.tip.theboss.util.DateTimeUtils;

import java.util.Calendar;


public class JobFormFragment extends MvpFragment<JobFormView, JobFormPresenter>
        implements JobFormView, CompoundButton.OnCheckedChangeListener {

    private FragmentJobFormBinding binding;
    private ProgressDialog progressDialog;

    public JobFormFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_job_form, container, false);
        binding.setView(getMvpView());
        binding.checkboxSingleDay.setOnCheckedChangeListener(this);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @NonNull
    @Override
    public JobFormPresenter createPresenter() {
        return new JobFormPresenter();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            binding.txtTo.setVisibility(View.GONE);
            binding.txtDateEnd.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPost() {
        presenter.addJob(binding.etTitle.getText().toString(),
                binding.etDescription.getText().toString(),
                binding.spinnerClassification.getSelectedItemPosition(),
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.YEAR, i);
                        calendar1.set(Calendar.MONTH, i1);
                        calendar1.set(Calendar.DAY_OF_MONTH, i2);
                        String dateString = DateTimeUtils.convertDateToString("MM/dd/yy", calendar1);
                        if (view.getId() == binding.txtDateStart.getId()
                                || binding.checkboxSingleDay.isChecked()) {
                            binding.txtDateStart.setText(dateString);
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
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Connecting in...");
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
        new AlertDialog.Builder(getContext())
                .setTitle("Job Posted!")
                .setPositiveButton("Close", null)
                .setCancelable(false)
                .show();

    }
}
