package com.turnipconsultants.brongo_client.models.BuyAProperty;

/**
 * Created by mohit on 29-09-2017.
 */

public class BuyPropInputModel {
    private String postingType;

    private String budget;

    private String orientation;

    private String propertyStatus;

    private String bedRoomType;

    private long budgetRange2;

    private long budgetRange1;

    private String subPropertyType;

    private Area area;

    private String propertyType;

    private int isTenant;

    private String plotSize;

    private int isOwner;

    private String clientMobileNo;

    private float commission;

    private String comments;

    private String preferredFloors;

    private String requiredSquareFeet;
    private String reSquareFeetRange1;
    private String reSquareFeetRange2;
    private String amenities;

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

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
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

    public long getBudgetRange2() {
        return budgetRange2;
    }

    public void setBudgetRange2(long budgetRange2) {
        this.budgetRange2 = budgetRange2;
    }

    public long getBudgetRange1() {
        return budgetRange1;
    }

    public void setBudgetRange1(long budgetRange1) {
        this.budgetRange1 = budgetRange1;
    }

    public String getSubPropertyType() {
        return subPropertyType;
    }

    public void setSubPropertyType(String subPropertyType) {
        this.subPropertyType = subPropertyType;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }


    public String getPlotSize() {
        return plotSize;
    }

    public void setPlotSize(String plotSize) {
        this.plotSize = plotSize;
    }

    public int getIsTenant() {
        return isTenant;
    }

    public void setIsTenant(int isTenant) {
        this.isTenant = isTenant;
    }

    public int getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(int isOwner) {
        this.isOwner = isOwner;
    }

    public String getClientMobileNo() {
        return clientMobileNo;
    }

    public void setClientMobileNo(String clientMobileNo) {
        this.clientMobileNo = clientMobileNo;
    }

    public float getCommission() {
        return commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPreferredFloors() {
        return preferredFloors;
    }

    public void setPreferredFloors(String preferredFloors) {
        this.preferredFloors = preferredFloors;
    }

    public String getRequiredSquareFeet() {
        return requiredSquareFeet;
    }

    public void setRequiredSquareFeet(String requiredSquareFeet) {
        this.requiredSquareFeet = requiredSquareFeet;
    }

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

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
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
    }


}
