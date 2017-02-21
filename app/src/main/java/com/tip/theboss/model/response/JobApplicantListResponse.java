package com.tip.theboss.model.response;

import com.tip.theboss.model.data.Applicant;

import java.util.List;

/**
 * @author pocholomia
 * @since 21/02/2017
 */

public class JobApplicantListResponse extends ListResponse {

    private List<Applicant> results;

    public List<Applicant> getResults() {
        return results;
    }

    public void setResults(List<Applicant> results) {
        this.results = results;
    }
}
