package com.samithprem.apa.di.module;


import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samithprem.apa.BuildConfig;
import com.samithprem.apa.data.source.remote.api.FeedService;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {

    public static String BASE_API_URL = "https://mock.com/";

    private int CACHE_SIZE = 10 * 1024 * 1024; //10mib
    private final int CONNECT_TIMEOUT_IN_SEC = 60;
    private final String CACHE_DIR_NAME = "http";

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder builder =
                new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return builder.setLenient().create();
    }


    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_API_URL)
                .client(okHttpClient)
                .build();

    }

    @Provides
    @Singleton
    FeedService provideFeedAPIService(Retrofit retrofit) {
        return retrofit.create(FeedService.class);
    }


    private Cache provideHttpCache(Context context) {
        Cache cache = new Cache(new File(context.getCacheDir(), CACHE_DIR_NAME), CACHE_SIZE);
        return cache;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Context context) {
        OkHttpClient tempClient = new OkHttpClient.Builder()
                .readTimeout(CONNECT_TIMEOUT_IN_SEC, TimeUnit.SECONDS)// connect timeout
                .connectTimeout(CONNECT_TIMEOUT_IN_SEC, TimeUnit.SECONDS)// socket timeout
                .followRedirects(false)
                .cache(provideHttpCache(context))
                .build();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            tempClient = tempClient.newBuilder()
                    .addInterceptor(logging)
                    .build();
        }

        return tempClient;
    }
}
