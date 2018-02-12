package com.turnipconsultants.brongo_client.responseModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Created by mohit on 16-01-2018.
 */

@Data
public class PastRequirementResponse {
    @SerializedName("statusCode")
    private int statusCode;
    @SerializedName("data")
    private List<DataEntity> data;
    @SerializedName("message")
    private String message;

    @Data
    public class DataEntity {
        private String postingType;

        private String addedTime;

        private int reviews;

        private String status;

        private String subPropertyType;

        private List<String> property;

        private String brokerImage;

        private boolean isClientRated;

        private String planType;

        private String propertyId;

        private String propertyType;

        private String brokerMobileNo;

        private boolean isPayed;

        private String brokerName;

        private double rating;

        private double commission;

        private boolean isBrokerRated;


    }
}
