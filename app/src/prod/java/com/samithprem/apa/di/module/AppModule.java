package com.samithprem.apa.di.module;

import android.content.Context;

import com.samithprem.apa.APAApplication;
import com.samithprem.apa.data.repository.FeedRepository;
import com.samithprem.apa.data.repository.FeedRepositoryImpl;
import com.samithprem.apa.data.source.remote.api.FeedService;
import com.samithprem.apa.ui.feed.list.FeedListModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ViewModelModule.class,FeedListModule.class})
public class AppModule {


    @Provides
    Context provideContext(APAApplication application) {
        return application.getApplicationContext();
    }

    @Singleton
    @Provides
    FeedRepository provideFeedRepository(FeedService feedService) {
       return new FeedRepositoryImpl(feedService);
    }

}
