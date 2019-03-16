package com.samithprem.apa.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.samithprem.apa.R;
import com.samithprem.apa.data.source.remote.api.model.Feed;
import com.samithprem.apa.ui.feed.list.FeedListFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;


public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.configureDagger();
        this.showFragment(savedInstanceState);

    }


    private void showFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {

            FeedListFragment fragment = new FeedListFragment();

            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, FeedListFragment.TAG).commit();
        }
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    /**
     * Shows the feed detail fragment
     */
    public void show(Feed feed) {
        //do nothing
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
