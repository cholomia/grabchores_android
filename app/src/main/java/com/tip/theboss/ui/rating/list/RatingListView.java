package com.tip.theboss.ui.rating.list;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.tip.theboss.model.data.Rating;
import com.tip.theboss.ui.base.MoreListView;

import java.util.List;

/**
 * Created by Cholo Mia on 2/25/2017.
 */

public interface RatingListView extends MvpView, MoreListView {

    void stopLoad();

    void showMessage(String message);

    void addNext(String nextUrl);

    void setRatingList(List<Rating> ratings);

    void startProgressLoad();

    void stopProgressLoad();

    void onDeleteRating(Rating rating);
}
