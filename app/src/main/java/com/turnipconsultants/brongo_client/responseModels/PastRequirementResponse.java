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

        private float reviews;

        private String addedTime;

        private String status;

        private String subPropertyType;

        private List<String> property;

        private String brokerImage;

        private String planType;

        private String propertyId;

        private String propertyType;

        private String brokerMobileNo;

        private String brokerName;

        private float rating;

        private float commission;

        public String getPostingType() {
            return postingType;
        }

        public void setPostingType(String postingType) {
            this.postingType = postingType;
        }

        public float getReviews() {
            return reviews;
        }

        public void setReviews(float reviews) {
            this.reviews = reviews;
        }

        public String getAddedTime() {
            return addedTime;
        }

        public void setAddedTime(String addedTime) {
            this.addedTime = addedTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSubPropertyType() {
            return subPropertyType;
        }

        public void setSubPropertyType(String subPropertyType) {
            this.subPropertyType = subPropertyType;
        }

        public List<String> getProperty() {
            return property;
        }

        public void setProperty(List<String> property) {
            this.property = property;
        }

        public String getBrokerImage() {
            return brokerImage;
        }

        public void setBrokerImage(String brokerImage) {
            this.brokerImage = brokerImage;
        }

        public String getPlanType() {
            return planType;
        }

        public void setPlanType(String planType) {
            this.planType = planType;
        }

        public String getPropertyId() {
            return propertyId;
        }

        public void setPropertyId(String propertyId) {
            this.propertyId = propertyId;
        }

        public String getPropertyType() {
            return propertyType;
        }

        public void setPropertyType(String propertyType) {
            this.propertyType = propertyType;
        }

        public String getBrokerMobileNo() {
            return brokerMobileNo;
        }

        public void setBrokerMobileNo(String brokerMobileNo) {
            this.brokerMobileNo = brokerMobileNo;
        }

        public String getBrokerName() {
            return brokerName;
        }

        public void setBrokerName(String brokerName) {
            this.brokerName = brokerName;
        }

        public float getRating() {
            return rating;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }

        public float getCommission() {
            return commission;
        }

        public void setCommission(float commission) {
            this.commission = commission;
        }
    }
}
