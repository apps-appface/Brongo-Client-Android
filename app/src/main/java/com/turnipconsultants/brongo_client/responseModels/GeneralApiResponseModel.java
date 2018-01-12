package com.turnipconsultants.brongo_client.responseModels;

import java.util.List;

/**
 * Created by mohit on 06-10-2017.
 */

public class GeneralApiResponseModel {
    private int statusCode;
    private List<DataEntity> data;
    private String message;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class DataEntity {

    }
}
