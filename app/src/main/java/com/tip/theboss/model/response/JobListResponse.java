package com.tip.theboss.model.response;

import com.tip.theboss.model.data.Job;

import java.util.List;

/**
 * @author pocholomia
 * @since 26/01/2017
 */

public class JobListResponse extends ListResponse {

    private List<Job> results;

    public List<Job> getResults() {
        return results;
    }

    public void setResults(List<Job> results) {
        this.results = results;
    }
}
