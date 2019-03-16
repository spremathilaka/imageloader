package com.samithprem.apa.di.module;

import com.samithprem.apa.ui.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Binds all sub-components within the app.
 */
@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract MainActivity bindMainActivity();

    // Add bindings for other sub-components here
}
