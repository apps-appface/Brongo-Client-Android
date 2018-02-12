package com.turnipconsultants.brongo_client.architecture;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mohit on 22-01-2018.
 */

@Module
public class ApplicationModule {
    private final Application mapp;

    public ApplicationModule(Application mapp) {
        this.mapp = mapp;
    }

}
