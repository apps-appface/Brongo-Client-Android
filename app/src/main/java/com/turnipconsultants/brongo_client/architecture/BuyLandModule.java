package com.turnipconsultants.brongo_client.architecture;

import com.turnipconsultants.brongo_client.fragments.BuyAProperty.BUY_Land_CommercialFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by mohit on 29-01-2018.
 */

@Module
public abstract class BuyLandModule {
    @PerChildFragment
    @ContributesAndroidInjector(modules = BuyLandCommercialModule.class)
    abstract BUY_Land_CommercialFragment BUY_Land_CommercialFragmentInjector();
}
