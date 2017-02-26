package com.tip.theboss.ui.applicants.detail;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.theboss.model.data.Applicant;

/**
 * @author pocholomia
 * @since 23/02/2017
 */

public interface ApplicantDetailView extends MvpView {
    void setApplicant(Applicant applicant);

    void showMessage(String message);

    void onAccept(Applicant applicant);

    void onCancel(Applicant applicant);

    void startLoad();

    void stopLoad();

    void updateSuccess(String message);

    void setAcceptEnable(boolean acceptEnable);

    void onAddRating(Applicant applicant);
}
