package com.turnipconsultants.brongo_client.mvp;

import com.turnipconsultants.brongo_client.models.AcceptedBrokersInputModel;

import retrofit2.http.Header;

/**
 * Created by mohit on 15-01-2018.
 */

public interface RequirementsPresenter {
    void getAllRequirements(String accessToken, String platform, String deviceId, AcceptedBrokersInputModel model);
}
