package com.turnipconsultants.brongo_client.models.RentAProperty;

/**
 * Created by Pankaj on 09-11-2017.
 */

public class RentAPropertyCommercial {
    String clientMobileNo,postingType,subPropertyType,
            comments,preferredProjects,avoidProjects,requiredSquareFeet,
            preferredFloor,propertyType,propertyStatus,reSquareFeetRange1,reSquareFeetRange2;
    double budgetRange1,budgetRange2;int commission;
    Area area;

    public String getReSquareFeetRange1() {
        return reSquareFeetRange1;
    }

    public void setReSquareFeetRange1(String reSquareFeetRange1) {
        this.reSquareFeetRange1 = reSquareFeetRange1;
    }

    public String getReSquareFeetRange2() {
        return reSquareFeetRange2;
    }

    public void setReSquareFeetRange2(String reSquareFeetRange2) {
        this.reSquareFeetRange2 = reSquareFeetRange2;
    }

    public String getClientMobileNo() {
        return clientMobileNo;
    }

    public void setClientMobileNo(String clientMobileNo) {
        this.clientMobileNo = clientMobileNo;
    }

    public String getPostingType() {
        return postingType;
    }

    public void setPostingType(String postingType) {
        this.postingType = postingType;
    }

    public String getSubPropertyType() {
        return subPropertyType;
    }

    public void setSubPropertyType(String subPropertyType) {
        this.subPropertyType = subPropertyType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPreferredProjects() {
        return preferredProjects;
    }

    public void setPreferredProjects(String preferredProjects) {
        this.preferredProjects = preferredProjects;
    }

    public String getAvoidProjects() {
        return avoidProjects;
    }

    public void setAvoidProjects(String avoidProjects) {
        this.avoidProjects = avoidProjects;
    }

    public String getRequiredSquareFeet() {
        return requiredSquareFeet;
    }

    public void setRequiredSquareFeet(String requiredSquareFeet) {
        this.requiredSquareFeet = requiredSquareFeet;
    }

    public String getPreferredFloor() {
        return preferredFloor;
    }

    public void setPreferredFloor(String preferredFloor) {
        this.preferredFloor = preferredFloor;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyStatus() {
        return propertyStatus;
    }

    public void setPropertyStatus(String propertyStatus) {
        this.propertyStatus = propertyStatus;
    }

    public double getBudgetRange1() {
        return budgetRange1;
    }

    public void setBudgetRange1(double budgetRange1) {
        this.budgetRange1 = budgetRange1;
    }

    public double getBudgetRange2() {
        return budgetRange2;
    }

    public void setBudgetRange2(double budgetRange2) {
        this.budgetRange2 = budgetRange2;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public static class Area{
        String microMarketName,microMarketCity, microMarketState;

        public String getMicroMarketName() {
            return microMarketName;
        }

        public void setMicroMarketName(String microMarketName) {
            this.microMarketName = microMarketName;
        }

        public String getMicroMarketCity() {
            return microMarketCity;
        }

        public void setMicroMarketCity(String microMarketCity) {
            this.microMarketCity = microMarketCity;
        }

        public String getMicroMarketState() {
            return microMarketState;
        }

        public void setMicroMarketState(String microMarketState) {
            this.microMarketState = microMarketState;
        }
    }
}
