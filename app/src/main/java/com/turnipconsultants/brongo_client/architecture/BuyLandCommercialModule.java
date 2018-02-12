package com.turnipconsultants.brongo_client.architecture;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mohit on 29-01-2018.
 */

@Module
public class BuyLandCommercialModule {

    @Provides
    BuyLandRepository providesBuyLandRepository() {
        return new BuyLandRepository();
    }

    @Provides
    static BuyLandViewModel provideBuyLandViewModel(BuyLandRepository repository) {
        return new BuyLandViewModel(repository);
    }

}
