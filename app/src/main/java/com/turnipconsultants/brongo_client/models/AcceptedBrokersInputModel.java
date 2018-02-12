package com.turnipconsultants.brongo_client.models;

import java.util.List;

/**
 * Created by mohit on 03-10-2017.
 */

public class AcceptedBrokersInputModel {
    private String clientMobileNo;
    private String propertyId;
    private String postingType;
    private String brokerMobileNo;
    private String userId;
    private int limit;
    private int challengeQue;
    private String challengeComment;
    private String reason;
    private float rating;
    private List<String> review;
    private String comment;
    private String propertyType;
    private String subPropertyType;

    public String getSubPropertyType() {
        return subPropertyType;
    }

    public void setSubPropertyType(String subPropertyType) {
        this.subPropertyType = subPropertyType;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getChallengeQue() {
        return challengeQue;
    }

    public void setChallengeQue(int challengeQue) {
        this.challengeQue = challengeQue;
    }

    public String getChallengeComment() {
        return challengeComment;
    }

    public void setChallengeComment(String challengeComment) {
        this.challengeComment = challengeComment;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBrokerMobileNo() {
        return brokerMobileNo;
    }

    public void setBrokerMobileNo(String brokerMobileNo) {
        this.brokerMobileNo = brokerMobileNo;
    }

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
