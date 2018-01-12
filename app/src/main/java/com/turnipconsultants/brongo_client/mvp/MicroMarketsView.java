package com.turnipconsultants.brongo_client.mvp;

import com.turnipconsultants.brongo_client.responseModels.FetchMicroMarketResponse;

/**
 * Created by mohit on 11-01-2018.
 */

public interface MicroMarketsView {
    void setMicroMarkets(FetchMicroMarketResponse obj,boolean isSuccess);
}
