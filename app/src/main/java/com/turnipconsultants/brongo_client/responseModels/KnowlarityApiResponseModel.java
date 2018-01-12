package com.turnipconsultants.brongo_client.responseModels;

import java.util.ArrayList;

/**
 * Created by mohit on 06-10-2017.
 */

public class KnowlarityApiResponseModel {
    private int statusCode;
    private ArrayList<String> data;
    private String message;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
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
