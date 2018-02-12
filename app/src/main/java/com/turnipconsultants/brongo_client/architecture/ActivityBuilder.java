package com.turnipconsultants.brongo_client.architecture;



import com.turnipconsultants.brongo_client.activities.PropertyRouterActivity;
import com.turnipconsultants.brongo_client.fragments.BuyAProperty.BUY_Land_CommercialFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by mohit on 29-01-2018.
 */

@Module
public abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules =PropertyRouterActivityModule.class)
    abstract PropertyRouterActivity bindPropertyRouterActivity();
}
