package com.turnipconsultants.brongo_client.models.RentAProperty;

/**
 * Created by Pankaj on 09-11-2017.
 */

public class RentAPropertyModel {
    private String orientation;

    private String furnishingStatus;

    private String postingType;

    private String budget;

    private String propertyStatus;

    private String bedRoomType;

    private String avoidProjects;

    private String subPropertyType;

    private double budgetRange2;

    private double budgetRange1;

    private String maritalStatus;

    private String propertyType;

    private Area area;

    private String preferredProjects;

    private String clientMobileNo;

    private String comments;
    private int commission;

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getFurnishingStatus() {
        return furnishingStatus;
    }

    public void setFurnishingStatus(String furnishingStatus) {
        this.furnishingStatus = furnishingStatus;
    }

    public String getPostingType() {
        return postingType;
    }

    public void setPostingType(String postingType) {
        this.postingType = postingType;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getPropertyStatus() {
        return propertyStatus;
    }

    public void setPropertyStatus(String propertyStatus) {
        this.propertyStatus = propertyStatus;
    }

    public String getBedRoomType() {
        return bedRoomType;
    }

    public void setBedRoomType(String bedRoomType) {
        this.bedRoomType = bedRoomType;
    }

    public String getAvoidProjects() {
        return avoidProjects;
    }

    public void setAvoidProjects(String avoidProjects) {
        this.avoidProjects = avoidProjects;
    }

    public String getSubPropertyType() {
        return subPropertyType;
    }

    public void setSubPropertyType(String subPropertyType) {
        this.subPropertyType = subPropertyType;
    }

    public double getBudgetRange2() {
        return budgetRange2;
    }

    public void setBudgetRange2(double budgetRange2) {
        this.budgetRange2 = budgetRange2;
    }

    public double getBudgetRange1() {
        return budgetRange1;
    }

    public void setBudgetRange1(double budgetRange1) {
        this.budgetRange1 = budgetRange1;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }



    public String getPreferredProjects() {
        return preferredProjects;
    }

    public void setPreferredProjects(String preferredProjects) {
        this.preferredProjects = preferredProjects;
    }

    public String getClientMobileNo() {
        return clientMobileNo;
    }

    public void setClientMobileNo(String clientMobileNo) {
        this.clientMobileNo = clientMobileNo;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    @Override
    public String toString() {
        return "ClassPojo [orientation = " + orientation + ", furnishingStatus = " + furnishingStatus + ", postingType = " + postingType + ", budget = " + budget + ", propertyStatus = " + propertyStatus + ", bedRoomType = " + bedRoomType + ", avoidProjects = " + avoidProjects + ", subPropertyType = " + subPropertyType + ", budgetRange2 = " + budgetRange2 + ", budgetRange1 = " + budgetRange1 + ", maritalStatus = " + maritalStatus + ", propertyType = " + propertyType + ", area = " + area + ", preferredProjects = " + preferredProjects + ", clientMobileNo = " + clientMobileNo + ", comments = " + comments + "]";
    }

    public static class Area {
        private String microMarketName;

        private String microMarketCity;

        private String microMarketState;

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

        @Override
        public String toString() {
            return "ClassPojo [microMarketName = " + microMarketName + ", microMarketCity = " + microMarketCity + ", microMarketState = " + microMarketState + "]";
        }
    }

}
