package com.samithprem.apa.data.repository;

import com.samithprem.apa.data.source.remote.api.model.ApiResponse;

import io.reactivex.Single;


public interface FeedRepository {

    Single<ApiResponse> getFeeds();

}