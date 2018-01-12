package com.turnipconsultants.brongo_client.responseModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Pankaj on 26-12-2017.
 */

public class PastRequirementResponse {

    @SerializedName("statusCode")
    private int statusCode;
    @SerializedName("data")
    private List<Data> data;
    @SerializedName("message")
    private String message;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

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

    public static class Dropped {
        @SerializedName("brokerName")
        private String brokerName;
        @SerializedName("brokerMobileNo")
        private String brokerMobileNo;
        @SerializedName("brokerImage")
        private String brokerImage;
        @SerializedName("rating")
        private int rating;
        @SerializedName("reviews")
        private int reviews;
        @SerializedName("planType")
        private String planType;
        @SerializedName("bedRoomType")
        private String bedRoomType;
        @SerializedName("propertyId")
        private String propertyId;
        @SerializedName("status")
        private String status;
        @SerializedName("postingType")
        private String postingType;
        @SerializedName("propertyType")
        private String propertyType;
        @SerializedName("microMarketName")
        private String microMarketName;
        @SerializedName("rentalType")
        private String rentalType;
        @SerializedName("subPropertyType")
        private String subPropertyType;
        @SerializedName("budget")
        private String budget;
        @SerializedName("budgetRange1")
        private int budgetRange1;
        @SerializedName("budgetRange2")
        private int budgetRange2;
        @SerializedName("propertyStatus")
        private String propertyStatus;
        @SerializedName("leadStatus")
        private String leadStatus;
        @SerializedName("meetingLocation")
        private String meetingLocation;

        public String getBrokerName() {
            return brokerName;
        }

        public void setBrokerName(String brokerName) {
            this.brokerName = brokerName;
        }

        public String getBrokerMobileNo() {
            return brokerMobileNo;
        }

        public void setBrokerMobileNo(String brokerMobileNo) {
            this.brokerMobileNo = brokerMobileNo;
        }

        public String getBrokerImage() {
            return brokerImage;
        }

        public void setBrokerImage(String brokerImage) {
            this.brokerImage = brokerImage;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public int getReviews() {
            return reviews;
        }

        public void setReviews(int reviews) {
            this.reviews = reviews;
        }

        public String getPlanType() {
            return planType;
        }

        public void setPlanType(String planType) {
            this.planType = planType;
        }

        public String getBedRoomType() {
            return bedRoomType;
        }

        public void setBedRoomType(String bedRoomType) {
            this.bedRoomType = bedRoomType;
        }

        public String getPropertyId() {
            return propertyId;
        }

        public void setPropertyId(String propertyId) {
            this.propertyId = propertyId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPostingType() {
            return postingType;
        }

        public void setPostingType(String postingType) {
            this.postingType = postingType;
        }

        public String getPropertyType() {
            return propertyType;
        }

        public void setPropertyType(String propertyType) {
            this.propertyType = propertyType;
        }

        public String getMicroMarketName() {
            return microMarketName;
        }

        public void setMicroMarketName(String microMarketName) {
            this.microMarketName = microMarketName;
        }

        public String getRentalType() {
            return rentalType;
        }

        public void setRentalType(String rentalType) {
            this.rentalType = rentalType;
        }

        public String getSubPropertyType() {
            return subPropertyType;
        }

        public void setSubPropertyType(String subPropertyType) {
            this.subPropertyType = subPropertyType;
        }

        public String getBudget() {
            return budget;
        }

        public void setBudget(String budget) {
            this.budget = budget;
        }

        public int getBudgetRange1() {
            return budgetRange1;
        }

        public void setBudgetRange1(int budgetRange1) {
            this.budgetRange1 = budgetRange1;
        }

        public int getBudgetRange2() {
            return budgetRange2;
        }

        public void setBudgetRange2(int budgetRange2) {
            this.budgetRange2 = budgetRange2;
        }

        public String getPropertyStatus() {
            return propertyStatus;
        }

        public void setPropertyStatus(String propertyStatus) {
            this.propertyStatus = propertyStatus;
        }

        public String getLeadStatus() {
            return leadStatus;
        }

        public void setLeadStatus(String leadStatus) {
            this.leadStatus = leadStatus;
        }

        public String getMeetingLocation() {
            return meetingLocation;
        }

        public void setMeetingLocation(String meetingLocation) {
            this.meetingLocation = meetingLocation;
        }
    }

    public static class Closed {
    }

    public static class Data {
        @SerializedName("dropped")
        private List<Dropped> dropped;
        @SerializedName("closed")
        private List<Closed> closed;
        @SerializedName("potentialCommission")
        private int potentialCommission;

        public List<Dropped> getDropped() {
            return dropped;
        }

        public void setDropped(List<Dropped> dropped) {
            this.dropped = dropped;
        }

        public List<Closed> getClosed() {
            return closed;
        }

        public void setClosed(List<Closed> closed) {
            this.closed = closed;
        }

        public int getPotentialCommission() {
            return potentialCommission;
        }

        public void setPotentialCommission(int potentialCommission) {
            this.potentialCommission = potentialCommission;
        }
    }
}
