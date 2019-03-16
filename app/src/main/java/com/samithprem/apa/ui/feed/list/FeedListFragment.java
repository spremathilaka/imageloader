package com.samithprem.apa.ui.feed.list;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.samithprem.apa.R;
import com.samithprem.apa.data.source.remote.api.model.ApiResponse;
import com.samithprem.apa.data.source.remote.api.model.FeedsResponse;
import com.samithprem.apa.databinding.FragmentFeedListBinding;
import com.samithprem.apa.ui.MainActivity;
import com.samithprem.apa.ui.feed.list.adapter.FeedAdapter;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;


public class FeedListFragment extends Fragment {
    public static final String TAG = FeedListFragment.class.getName();

    private FeedAdapter feedsAdapter;
    private FragmentFeedListBinding binding;

    @Inject
    //ViewModelProvider.Factory viewModelFactory;
            FeedListViewModelFactory viewModelFactory;

    private FeedListViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed_list, container, false);

        feedsAdapter = new FeedAdapter(feedItemClickCallback);
        binding.feedList.setAdapter(feedsAdapter);
        //binding.setIsLoading(true);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpSwipeRefreshLayout(view);
    }

    private void setUpSwipeRefreshLayout(View view) {
        binding.swipeContainer.setOnRefreshListener(() -> viewModel.getFeed());
        // Configure the refreshing colors
        binding.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.configureDagger();
        this.configureViewModel();
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    private void configureDagger() {
        AndroidSupportInjection.inject(this);
    }

    private void configureViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FeedListViewModel.class);
        viewModel.getFeed();
        viewModel.getFeedsResponseObservable().observe(this, this::consumeResponse);
    }


    /*
     * method to handle response
     * */
    private void consumeResponse(@NonNull ApiResponse apiResponse) {

        if (apiResponse != null) {
            switch (apiResponse.getStatus()) {
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;

                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    updateUI(apiResponse.getData());
                    break;

                case ERROR:
                    hideLoadingAndshowErrorMessage(getResources().getString(R.string.error_loading_data));
                    break;
                default:
                    break;
            }
        }
    }

    private void hideLoadingAndshowErrorMessage(@NonNull String errorMessage) {
        binding.progressBar.setVisibility(View.GONE);
        binding.feedList.setVisibility(View.GONE);
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
    }


    private void updateUI(FeedsResponse feedsResponses) {
        // Update the list when the data changes
        if (feedsResponses != null) {
            if (feedsResponses.getFeed().isEmpty()) {
                binding.feedList.setVisibility(View.GONE);
                binding.emptyView.setVisibility(View.VISIBLE);
            } else {
                binding.feedList.setVisibility(View.VISIBLE);
                binding.emptyView.setVisibility(View.GONE);
                feedsAdapter.clear();
                feedsAdapter.setFeedList(feedsResponses.getFeed());
            }

            // binding.setIsLoading(false);

            if (!TextUtils.isEmpty(feedsResponses.getTitle())) {
                if (getActivity() != null) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(feedsResponses.getTitle());
                }
            }
        } else {
            binding.feedList.setVisibility(View.GONE);
            Toast.makeText(getActivity(), getResources().getString(R.string.error_loading_data), Toast.LENGTH_SHORT).show();
        }
        binding.swipeContainer.setRefreshing(false);
    }


    private final ListItemClickCallback feedItemClickCallback = feed -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            if (getActivity() != null) {
                ((MainActivity) getActivity()).show(feed);
            }
        }
    };
}
