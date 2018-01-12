package com.turnipconsultants.brongo_client.responseModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Created by mohit on 12-01-2018.
 */

@Data
public class SecondLandingResponse {
    @SerializedName("statusCode")
    private int statusCode;
    @SerializedName("data")
    private List<DataEntity> data;
    @SerializedName("message")
    private String message;

    @Data
    public class DataEntity {
        private String reminder;

        private String postingType;

        private List<String> statusUpdatedTimes;

        private int reviews;

        private String payment;

        private String status;

        private String dealClosedTime;

        private String timeOfVisit;

        private List<String> remainingStatus;

        private String subPropertyType;

        private List<String> property;

        private String brokerImage;

        private List<SiteVisit> siteVisit;

        private String planType;

        private String meetAt;

        private String propertyId;

        private String propertyType;

        private String brokerMobileNo;

        private String brokerName;

        private double rating;

        private double commission;

        private List<String> completedStatus;

        private String meetingLocation;

        private String dateOfVisit;

        @Data
        public class SiteVisit {
            private String propertyName;

            private String meetAt;

            private String propertyId;

            private String timeOfVisit;

            private String siteVisitName;

            private String updatedTime;

            private String dateOfVisit;
        }
    }
}
