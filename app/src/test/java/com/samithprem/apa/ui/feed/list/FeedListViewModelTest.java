package com.samithprem.apa.ui.feed.list;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;

import com.samithprem.apa.data.repository.FeedRepository;
import com.samithprem.apa.data.source.remote.api.model.ApiResponse;
import com.samithprem.apa.data.source.remote.api.model.FeedsResponse;
import com.samithprem.apa.data.source.remote.api.model.Status;
import com.samithprem.apa.util.NetworkUtil;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NetworkUtil.class})
public class FeedListViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private FeedRepository feedRepository;

    private FeedListViewModel feedListViewModel;

    @Mock
    Observer<ApiResponse> apiResponseObserver ;

    @Mock
    Context  context;

    @BeforeClass
    public static void setUpRxSchedulers() {
        Scheduler immediate = new Scheduler() {
            @Override
            public Disposable scheduleDirect(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
                // this prevents StackOverflowErrors when scheduling with a delay
                return super.scheduleDirect(run, 0, unit);
            }

            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(Runnable::run);
            }
        };

        RxJavaPlugins.setInitIoSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitSingleSchedulerHandler(scheduler -> immediate);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> immediate);
    }


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(NetworkUtil.class);
        PowerMockito.when(NetworkUtil.checkInternetConnection(context)).thenReturn(true);
        feedListViewModel = new FeedListViewModel( context, feedRepository);
    }


    @Test
    public void should_set_success_feed_response_when_api_success() throws InterruptedException {

        //Given
        FeedsResponse data = new FeedsResponse();
        ApiResponse apiResponse = new ApiResponse(Status.SUCCESS,data,null);
        when(feedRepository.getFeeds()).thenReturn(Single.just(apiResponse));

        feedListViewModel.getFeedsResponseObservable().observeForever(apiResponseObserver);
        feedListViewModel.getFeed();

        assertEquals (true,feedListViewModel.getFeedsResponseObservable().getValue().getStatus() == Status.SUCCESS);

    }

    @Test
    public void should_set_error_feed_response_when_api_failed() throws InterruptedException {

        FeedsResponse data = new FeedsResponse();
        when(feedRepository.getFeeds()).thenReturn(Single.error(new IOException()));

        feedListViewModel.getFeedsResponseObservable().observeForever(apiResponseObserver);
        feedListViewModel.getFeed();

        assertEquals (true,feedListViewModel.getFeedsResponseObservable().getValue().getStatus() == Status.ERROR);

    }
}