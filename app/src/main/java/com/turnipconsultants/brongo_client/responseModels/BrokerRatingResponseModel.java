package com.turnipconsultants.brongo_client.responseModels;

import java.util.List;

/**
 * Created by mohit on 06-10-2017.
 */

public class BrokerRatingResponseModel {
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
        private String[] reviews;

        private long closedLeads;

        private String plan;

        private String brokerMobileNo;

        private String brokerName;

        private float rating;

        private long reviewsCount;

        private long openDeals;

        public String[] getReviews() {
            return reviews;
        }

        public void setReviews(String[] reviews) {
            this.reviews = reviews;
        }

        public long getClosedLeads() {
            return closedLeads;
        }

        public void setClosedLeads(long closedLeads) {
            this.closedLeads = closedLeads;
        }

        public String getPlan() {
            return plan;
        }

        public void setPlan(String plan) {
            this.plan = plan;
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

        public long getReviewsCount() {
            return reviewsCount;
        }

        public void setReviewsCount(long reviewsCount) {
            this.reviewsCount = reviewsCount;
        }

        public long getOpenDeals() {
            return openDeals;
        }

        public void setOpenDeals(long openDeals) {
            this.openDeals = openDeals;
        }
    }
}
