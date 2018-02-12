package com.turnipconsultants.brongo_client.responseModels;

import java.util.List;

import javax.inject.Singleton;

/**
 * Created by mohit on 27-09-2017.
 */


public class PropertyTransactionResponseModel {
    private String message;
    private int statusCode;
    private List<DataEntity> data;

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

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity {
         String clientMobileNo;
        String propertyId;
         String postingType;

        public String getClientMobileNo() {
            return clientMobileNo;
        }

        public void setClientMobileNo(String clientMobileNo) {
            this.clientMobileNo = clientMobileNo;
        }

        public String getPropertyId() {
            return propertyId;
        }

        public void setPropertyId(String propertyId) {
            this.propertyId = propertyId;
        }

        public String getPostingType() {
            return postingType;
        }

        public void setPostingType(String postingType) {
            this.postingType = postingType;
        }
    }
}
