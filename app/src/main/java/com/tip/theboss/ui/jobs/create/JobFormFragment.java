package com.tip.theboss.ui.jobs.create;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.tip.theboss.R;
import com.tip.theboss.databinding.FragmentJobFormBinding;


public class JobFormFragment extends MvpFragment<JobFormView, JobFormPresenter>
        implements JobFormView, CompoundButton.OnCheckedChangeListener {

    private FragmentJobFormBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_job_form, container, false);
        binding.setView(getMvpView());
        binding.checkboxSingleDay.setOnCheckedChangeListener(this);
        return binding.getRoot();
    }

    @NonNull
    @Override
    public JobFormPresenter createPresenter() {
        return new JobFormPresenter();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    @Override
    public void onPost() {

    }

    @Override
    public void onDateStart() {

    }

    @Override
    public void onDateEnd() {

    }
}
