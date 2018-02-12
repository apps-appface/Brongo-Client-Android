package com.turnipconsultants.brongo_client.architecture;

import com.turnipconsultants.brongo_client.fragments.BuyAProperty.BUY_A_LandFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by mohit on 29-01-2018.
 */

@Module
public abstract class PropertyRouterActivityModule {
    @PerFragment
    @ContributesAndroidInjector(modules = BuyLandModule.class)
    abstract BUY_A_LandFragment BUY_A_LandFragmentInjector();



}
