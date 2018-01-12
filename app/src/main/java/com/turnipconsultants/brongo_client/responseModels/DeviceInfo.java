package com.turnipconsultants.brongo_client.responseModels;

import java.util.List;


public class DeviceInfo {
    public List<Data> data;
    public String message;
    public int statusCode;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public static class Data {


    }
}