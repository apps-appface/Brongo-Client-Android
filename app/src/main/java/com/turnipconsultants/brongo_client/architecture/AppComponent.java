package com.turnipconsultants.brongo_client.architecture;

import android.app.Application;

import com.turnipconsultants.brongo_client.BrongoClientApplication;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by mohit on 25-01-2018.
 */

@Component(modules = {
        AndroidSupportInjectionModule.class,
        ApplicationModule.class,
        ActivityBuilder.class})
public interface AppComponent extends AndroidInjector<DaggerApplication> {

    void inject(BrongoClientApplication app);

    @Override
    void inject(DaggerApplication instance);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

}
