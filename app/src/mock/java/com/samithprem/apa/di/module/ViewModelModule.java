package com.samithprem.apa.di.module;

import android.arch.lifecycle.ViewModelProvider;

import com.samithprem.apa.ui.base.FactoryViewModel;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelModule {

//    @Binds
//    @IntoMap
//    @ViewModelKey(FeedListViewModel.class)
//    abstract ViewModel bindFeedListViewModel(FeedListViewModel feedListViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(FactoryViewModel factory);

}
