package com.tip.theboss.model.response;

import com.tip.theboss.model.data.Rating;

import java.util.List;

/**
 * Created by Cholo Mia on 2/26/2017.
 */

public class RatingListResponse extends ListResponse {

    private List<Rating> results;

    public List<Rating> getResults() {
        return results;
    }

    public void setResults(List<Rating> results) {
        this.results = results;
    }
}
