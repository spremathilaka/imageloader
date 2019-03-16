package com.samithprem.apa.ui.feed.list;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.samithprem.apa.data.repository.FeedRepository;

import javax.inject.Inject;

public class FeedListViewModelFactory implements ViewModelProvider.Factory {

    private Context context;

    private final FeedRepository repository;

    @Inject
    public FeedListViewModelFactory(Context context, FeedRepository repo) {
        this.context = context;
        this.repository = repo;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FeedListViewModel.class)) {
            return (T) new FeedListViewModel(context, repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
