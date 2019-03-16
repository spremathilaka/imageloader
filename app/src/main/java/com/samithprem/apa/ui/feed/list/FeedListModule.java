package com.samithprem.apa.ui.feed.list;

import android.content.Context;

import com.samithprem.apa.data.repository.FeedRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FeedListModule {

    @Provides
    @Singleton
    FeedListViewModelFactory bindFeedListViewModelFactory(Context context, FeedRepository repo) {
        return new FeedListViewModelFactory(context, repo);
    }
}
