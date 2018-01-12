package com.turnipconsultants.brongo_client.Listener;

import com.turnipconsultants.brongo_client.responseModels.SecondLandingResponseModel;

import java.util.List;

/**
 * Created by Pankaj on 23-11-2017.
 */

public interface DealStatusListener {
    void updateDealStatus(List<SecondLandingResponseModel.Data.BuyAndRent> buyAndRent);
}
