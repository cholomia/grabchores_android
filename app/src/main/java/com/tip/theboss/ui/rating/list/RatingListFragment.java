package com.tip.theboss.ui.rating.list;


import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.tip.theboss.R;
import com.tip.theboss.databinding.FragmentRatingListBinding;
import com.tip.theboss.model.data.Rating;
import com.tip.theboss.util.StringUtils;

import java.util.List;
import java.util.Map;


public class RatingListFragment extends MvpViewStateFragment<RatingListView, RatingListPresenter>
        implements RatingListView, SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_TYPE = "arg-type";
    private static final String ARG_USERNAME = "arg-username";

    private int typeRating;
    private String rateUsername;

    private FragmentRatingListBinding binding;
    private RatingListAdapter adapter;
    private ProgressDialog progressDialog;

    public RatingListFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public RatingListPresenter createPresenter() {
        return new RatingListPresenter();
    }

    @NonNull
    @Override
    public ViewState createViewState() {
        return new RatingListViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        binding.swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                binding.swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    public static RatingListFragment newInstance(int typeRating, String rateUsername) {
        RatingListFragment fragment = new RatingListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, typeRating);
        args.putString(ARG_USERNAME, rateUsername);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            typeRating = getArguments().getInt(ARG_TYPE, -1);
            rateUsername = getArguments().getString(ARG_USERNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rating_list, container, false);
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new RatingListAdapter(getMvpView());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0)
                        ? 0 : recyclerView.getChildAt(0).getTop();
                binding.swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart(typeRating, rateUsername);
    }

    @Override
    public void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override
    public void onRefresh() {
        presenter.refresh(typeRating, rateUsername);
    }

    @Override
    public void stopLoad() {
        binding.swipeRefreshLayout.setRefreshing(false);
        adapter.setLoading(false);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addNext(String nextUrl) {
        adapter.setNextUrl(nextUrl);
    }

    @Override
    public void setRatingList(List<Rating> ratings) {
        adapter.setRatings(ratings);
    }

    @Override
    public void startProgressLoad() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Connecting...");
        }
        progressDialog.show();
    }

    @Override
    public void stopProgressLoad() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void onDeleteRating(Rating rating) {
        presenter.deleteRating(rating.getId());
    }

    @Override
    public void onMore(final String nextUrl) {
        binding.swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                binding.swipeRefreshLayout.setRefreshing(true);
                adapter.setLoading(true);
                Map<String, String> parameters = StringUtils.getParamsFromUrl(nextUrl);
                if (parameters.size() > 0) presenter.refresh(parameters);
                else presenter.refresh(typeRating, rateUsername);
            }
        });
    }
}
