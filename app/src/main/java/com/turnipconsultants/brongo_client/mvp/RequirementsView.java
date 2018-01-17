package com.turnipconsultants.brongo_client.mvp;

import com.turnipconsultants.brongo_client.responseModels.AllRequirementsResponse;

/**
 * Created by mohit on 15-01-2018.
 */

public interface RequirementsView {
    void onSuccessCall(AllRequirementsResponse.DataEntity dataEntity);

    void onFailures(String message);

    void onProgress(boolean showLoader);
}
