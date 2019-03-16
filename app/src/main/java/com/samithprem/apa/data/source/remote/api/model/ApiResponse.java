package com.samithprem.apa.data.source.remote.api.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.samithprem.apa.data.source.remote.api.model.Status.ERROR;
import static com.samithprem.apa.data.source.remote.api.model.Status.LOADING;
import static com.samithprem.apa.data.source.remote.api.model.Status.SUCCESS;

public class ApiResponse {

    private final Status status;

    @Nullable
    private final FeedsResponse data;

    @Nullable
    private final Throwable throwable;

    public ApiResponse(Status status, @Nullable FeedsResponse data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.throwable = error;
    }

    public static ApiResponse loading() {
        return new ApiResponse(LOADING, null, null);
    }

    public  static ApiResponse success(@NonNull FeedsResponse data) {
        return new ApiResponse(SUCCESS, data, null);
    }

    public static ApiResponse error(@NonNull Throwable throwable) {
        return new ApiResponse(ERROR, null, throwable);
    }

    public Status getStatus() {
        return status;
    }

    @Nullable
    public Throwable getThrowable() {
        return throwable;
    }

    @Nullable
    public FeedsResponse getData() {
        return data;
    }
}