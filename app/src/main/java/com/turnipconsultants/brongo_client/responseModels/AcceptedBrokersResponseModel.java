package com.turnipconsultants.brongo_client.responseModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mohit on 03-10-2017.
 */

public class AcceptedBrokersResponseModel {


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

    public static class ReviewsSingle {
        @SerializedName("name")
        private String name;
        @SerializedName("rating")
        private int rating;
        @SerializedName("review")
        private List<String> review;
        @SerializedName("comment")
        private String comment;
        @SerializedName("duration")
        private String duration;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public List<String> getReview() {
            return review;
        }

        public void setReview(List<String> review) {
            this.review = review;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }
    }

    public static class Reviews {
        @SerializedName("brokerMobileNo")
        private String brokerMobileNo;
        @SerializedName("brokerName")
        private String brokerName;
        @SerializedName("plan")
        private String plan;
        @SerializedName("rating")
        private int rating;
        @SerializedName("closedLeads")
        private int closedLeads;
        @SerializedName("openDeals")
        private int openDeals;
        @SerializedName("reviewsCount")
        private int reviewsCount;
        @SerializedName("reviews")
        private List<ReviewsSingle> reviews;

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

        public String getPlan() {
            return plan;
        }

        public void setPlan(String plan) {
            this.plan = plan;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public int getClosedLeads() {
            return closedLeads;
        }

        public void setClosedLeads(int closedLeads) {
            this.closedLeads = closedLeads;
        }

        public int getOpenDeals() {
            return openDeals;
        }

        public void setOpenDeals(int openDeals) {
            this.openDeals = openDeals;
        }

        public int getReviewsCount() {
            return reviewsCount;
        }

        public void setReviewsCount(int reviewsCount) {
            this.reviewsCount = reviewsCount;
        }

        public List<ReviewsSingle> getReviews() {
            return reviews;
        }

        public void setReviews(List<ReviewsSingle> reviews) {
            this.reviews = reviews;
        }
    }

    public static class Data {
        @SerializedName("userId")
        private String userId;
        @SerializedName("firstName")
        private String firstName;
        @SerializedName("lastName")
        private String lastName;
        @SerializedName("rating")
        private int rating;
        @SerializedName("mobileNo")
        private String mobileNo;
        @SerializedName("reviews")
        private List<Reviews> reviews;
        @SerializedName("profileImage")
        private String profileImage;
        @SerializedName("lastLeadsNo")
        private int lastLeadsNo;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public List<Reviews> getReviews() {
            return reviews;
        }

        public void setReviews(List<Reviews> reviews) {
            this.reviews = reviews;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public int getLastLeadsNo() {
            return lastLeadsNo;
        }

        public void setLastLeadsNo(int lastLeadsNo) {
            this.lastLeadsNo = lastLeadsNo;
        }
    }
}
