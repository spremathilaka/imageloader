package com.samithprem.apa.data.repository;

import android.support.annotation.NonNull;

import com.samithprem.apa.data.source.remote.api.FeedService;
import com.samithprem.apa.data.source.remote.api.model.ApiResponse;
import com.samithprem.apa.data.source.remote.api.model.Feed;
import com.samithprem.apa.data.source.remote.api.model.FeedsResponse;
import com.samithprem.apa.data.source.remote.api.model.Status;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;

@Singleton
public class FeedRepositoryImpl implements FeedRepository {
    private final FeedService feedServiceService;

    @Inject
    public FeedRepositoryImpl(@NonNull FeedService feedService) {
        feedServiceService = feedService;
    }

    @Override
    public Single<ApiResponse> getFeeds() {
        final FeedsResponse response = new FeedsResponse();
        return feedServiceService.getFeedList()
                .flatMap((Function<FeedsResponse, ObservableSource<List<Feed>>>) feedsResponse -> {
                    response.setTitle(feedsResponse.getTitle());
                    return Observable.just(feedsResponse.getFeed());
                })
                .flatMap(Observable::fromIterable)
                .filter(feed -> !(feed != null && (StringUtils.isEmpty(feed.getTitle())
                        && StringUtils.isEmpty(feed.getDescription())
                        && StringUtils.isEmpty(feed.getImageHref()))))
                .toList().map(feeds -> {
                    response.setFeed(feeds);
                    return new ApiResponse(Status.SUCCESS, response, null);
                }).onErrorReturn(throwable -> new ApiResponse(Status.ERROR, null, throwable));
    }
}
