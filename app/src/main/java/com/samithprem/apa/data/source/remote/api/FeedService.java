package com.samithprem.apa.data.source.remote.api;

import com.samithprem.apa.data.source.remote.api.model.FeedsResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface FeedService {



    @GET("s/2iodh4vg0eortkl/facts.json")
    Observable<FeedsResponse> getFeedList();
}
