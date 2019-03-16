package com.samithprem.apa.ui.feed.list.adapter;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class CustomBindingAdapter {

    private static final String TAG = CustomBindingAdapter.class.getName();

    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImageWithPicasso(final ImageView view, String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            view.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(imageUrl)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(view, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Image load successfully" + imageUrl);
                        }

                        @Override
                        public void onError(Exception e) {
                            view.setVisibility(View.GONE);
                            Log.d(TAG, "Image load failed: " + imageUrl);
                        }
                    });
        } else {
            view.setVisibility(View.GONE);
        }

    }
}