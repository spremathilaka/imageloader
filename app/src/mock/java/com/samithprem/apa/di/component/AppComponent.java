package com.samithprem.apa.di.component;

import com.samithprem.apa.APAApplication;
import com.samithprem.apa.di.module.ActivityModule;
import com.samithprem.apa.di.module.AppModule;
import com.samithprem.apa.di.module.FragmentModule;
import com.samithprem.apa.di.module.NetModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, AppModule.class,
        NetModule.class, ActivityModule.class, FragmentModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(APAApplication apaApplication);

        AppComponent build();
    }

    void inject(APAApplication app);
}
