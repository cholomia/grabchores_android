package com.tip.theboss.model.response;

import com.tip.theboss.model.data.Applicant;

/**
 * @author pocholomia
 * @since 23/02/2017
 */

public class ApplicantAcceptResponse extends BasicResponse {

    private Applicant applicant;

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }
}
