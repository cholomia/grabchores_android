package com.tip.theboss.ui.applicants.list;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.theboss.model.data.Applicant;
import com.tip.theboss.ui.base.MoreListView;

import java.util.List;

/**
 * @author pocholomia
 * @since 20/02/2017
 */

public interface ApplicantsView extends MvpView, MoreListView {
    void setApplicantList(List<Applicant> applicants);

    void stopLoading();

    void showMessage(String message);

    void addNext(String nextUrl);

    void onApplicantClick(Applicant applicant);
}
