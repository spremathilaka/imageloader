package com.samithprem.apa.ui.feed.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.samithprem.apa.R;
import com.samithprem.apa.data.repository.FeedRepository;
import com.samithprem.apa.data.source.remote.api.model.ApiResponse;
import com.samithprem.apa.util.NetworkUtil;
import com.samithprem.apa.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class FeedListViewModel extends ViewModel {

    private final MediatorLiveData<ApiResponse> responseMediatorLiveData = new MediatorLiveData<>();

    private final FeedRepository feedRepository;

    private final CompositeDisposable disposables;

    private Context context;

    @Inject
    public FeedListViewModel(Context context, FeedRepository feedRepository) {
        this.context = context;
        this.feedRepository = feedRepository;
        this.disposables = new CompositeDisposable();
    }

    public void getFeed() {
        if (NetworkUtil.checkInternetConnection(context)) {

            disposables.add(feedRepository.getFeeds()
                    .compose(RxUtil.applySingleSchedulers())
                    .doOnSubscribe((d) -> responseMediatorLiveData.setValue(ApiResponse.loading()))
                    .subscribe(
                            result -> responseMediatorLiveData.setValue(result),
                            throwable -> responseMediatorLiveData.setValue(ApiResponse.error(throwable))
                    ));
        } else {
            responseMediatorLiveData.setValue(ApiResponse.error(new Throwable(context.getString(R.string.no_internet_connection))));
        }
    }

    public LiveData<ApiResponse> getFeedsResponseObservable() {
        return responseMediatorLiveData;
    }


    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
