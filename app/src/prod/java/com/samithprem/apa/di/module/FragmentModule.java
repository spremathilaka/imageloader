package com.samithprem.apa.di.module;

import com.samithprem.apa.ui.feed.list.FeedListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract FeedListFragment contributeFeedListFragment();
}
