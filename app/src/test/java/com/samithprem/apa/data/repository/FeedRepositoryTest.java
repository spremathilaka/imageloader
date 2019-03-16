package com.samithprem.apa.data.repository;

import com.samithprem.apa.data.source.remote.api.FeedService;
import com.samithprem.apa.data.source.remote.api.model.ApiResponse;
import com.samithprem.apa.data.source.remote.api.model.Feed;
import com.samithprem.apa.data.source.remote.api.model.FeedsResponse;
import com.samithprem.apa.data.source.remote.api.model.Status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(JUnit4.class)
public class FeedRepositoryTest {

    @Mock
    FeedService feedService;

    private FeedRepository feedRepository;

    @Before
    public void setUp() throws Exception {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                __ -> Schedulers.trampoline());
        MockitoAnnotations.initMocks(this);
        feedRepository = new FeedRepositoryImpl(feedService);
    }


    @Test
    public void should_returnSuccessAPIResponse_when_feedAPIReturn_200OK_Status() {
        //Given
        FeedsResponse feedsResponse = getMockFeedResponse(false);
        ApiResponse response = new ApiResponse(Status.SUCCESS, feedsResponse, null);

        when(feedService.getFeedList()).thenReturn(Observable.just(feedsResponse));

        //When
        TestObserver<ApiResponse> testObserver = new TestObserver<>();
        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS);
        feedRepository.getFeeds().subscribeWith(testObserver);

        //then
        testObserver.assertNoErrors();
        testObserver.assertComplete();
        assertEquals(Status.SUCCESS, response.getStatus());
        assertEquals(response.getData().getTitle(), testObserver.values().get(0).getData().getTitle());
        assertEquals(10, testObserver.values().get(0).getData().getFeed().size());
    }

    @Test
    public void should_returnSuccessAPIResponse_when_feedAPIReturn_200OK_withEmptyFeed() {
        //Given
        FeedsResponse feedsResponse = getMockFeedResponseWithEmptyFeed();
        ApiResponse response = new ApiResponse(Status.SUCCESS, feedsResponse, null);

        when(feedService.getFeedList()).thenReturn(Observable.just(feedsResponse));

        //When
        TestObserver<ApiResponse> testObserver = new TestObserver<>();
        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS);
        feedRepository.getFeeds().subscribeWith(testObserver);

        //then
        testObserver.assertNoErrors();
        testObserver.assertComplete();
        assertEquals(Status.SUCCESS, response.getStatus());
        assertEquals(response.getData().getTitle(), testObserver.values().get(0).getData().getTitle());
        assertEquals(0, testObserver.values().get(0).getData().getFeed().size());
    }

    @Test
    public void should_return_filtered_feedList_when_APIFeedResponseListContains_FeedWith_EmptyContent() {
        //Given
        FeedsResponse feedsResponse = getMockFeedResponse(true);
        ApiResponse response = new ApiResponse(Status.SUCCESS, feedsResponse, null);

        when(feedService.getFeedList()).thenReturn(Observable.just(feedsResponse));

        //When
        TestObserver<ApiResponse> testObserver = new TestObserver<>();
        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS);
        feedRepository.getFeeds().subscribeWith(testObserver);

        //then
        testObserver.assertNoErrors();
        testObserver.assertComplete();
        assertEquals(Status.SUCCESS, response.getStatus());
        assertEquals(response.getData().getTitle(), testObserver.values().get(0).getData().getTitle());
        assertEquals(9, testObserver.values().get(0).getData().getFeed().size());
    }

    @Test
    public void should_returnErrorAPIResponse_when_feedAPIReturn_IOException() {
        //Given

        when(feedService.getFeedList()).thenReturn(getIOExceptionError());

        //When
        TestObserver<ApiResponse> testObserver = new TestObserver<>();
        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS);
        feedRepository.getFeeds().subscribeWith(testObserver);

        //then
        assertEquals(Status.ERROR, testObserver.values().get(0).getStatus());
    }

    private FeedsResponse getMockFeedResponse(boolean incldeEmptyContent) {
        FeedsResponse feedsResponse = new FeedsResponse();
        feedsResponse.setTitle("My Feed List");
        feedsResponse.setFeed(getFeedList(incldeEmptyContent));
        return feedsResponse;
    }

    private FeedsResponse getMockFeedResponseWithEmptyFeed() {
        FeedsResponse feedsResponse = new FeedsResponse();
        feedsResponse.setTitle("My Feed List");
        feedsResponse.setFeed(Collections.emptyList());
        return feedsResponse;
    }

    private List<Feed> getFeedList(boolean incldeEmptyContent) {
        List<Feed> feedList = new ArrayList<>();
        int list_size = 10;
        for (int i = 1; i <= list_size; i++) {

            if (i == 5 && incldeEmptyContent) {
                feedList.add(createNewFed("", "", ""));
            } else {
                feedList.add(createNewFed("Beavers-" + i, "Beavers are second only to humans in their ability to manipulate-" + i, "http://image.com.au/image.jpeg"));
            }
        }
        return feedList;

    }

    private Observable getIOExceptionError() {
        return Observable.error(new IOException());
    }


    private Feed createNewFed(String title, String desc, String url) {
        return new Feed(title, desc, url);
    }
}