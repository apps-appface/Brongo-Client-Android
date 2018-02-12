package com.turnipconsultants.brongo_client.models.BuyAProperty;

/**
 * Created by Pankaj on 09-11-2017.
 */

public class BuyPropertyModel {
    String clientMobileNo, subPropertyType,
            plotSize, budget, orientation, comments, propertyType, propertyStatus,
            requiredSquareFeet, preferredFloors, bedRoomType, postingType,
            reSquareFeetRange1, reSquareFeetRange2, amenities,carParking,microMarketId,landType,plotType;
    private String uRoadFacing;
    private String wRoadFacing,plotSizeRange1,plotSizeRange2;
    double budgetRange1,
            budgetRange2;
    float commission;
    Area area;

    public String getLandType() {
        return landType;
    }

    public void setLandType(String landType) {
        this.landType = landType;
    }

    public String getPlotType() {
        return plotType;
    }

    public void setPlotType(String plotType) {
        this.plotType = plotType;
    }

    public String getPlotSizeRange1() {
        return plotSizeRange1;
    }

    public void setPlotSizeRange1(String plotSizeRange1) {
        this.plotSizeRange1 = plotSizeRange1;
    }

    public String getPlotSizeRange2() {
        return plotSizeRange2;
    }

    public void setPlotSizeRange2(String plotSizeRange2) {
        this.plotSizeRange2 = plotSizeRange2;
    }

    public String getuRoadFacing() {
        return uRoadFacing;
    }

    public void setuRoadFacing(String uRoadFacing) {
        this.uRoadFacing = uRoadFacing;
    }

    public String getwRoadFacing() {
        return wRoadFacing;
    }

    public void setwRoadFacing(String wRoadFacing) {
        this.wRoadFacing = wRoadFacing;
    }

    public String getMicroMarketId() {
        return microMarketId;
    }

    public void setMicroMarketId(String microMarketId) {
        this.microMarketId = microMarketId;
    }

    public String getCarParking() {
        return carParking;
    }

    public void setCarParking(String carParking) {
        this.carParking = carParking;
    }

    public String getClientMobileNo() {
        return clientMobileNo;
    }

    public void setClientMobileNo(String clientMobileNo) {
        this.clientMobileNo = clientMobileNo;
    }

    public String getSubPropertyType() {
        return subPropertyType;
    }

    public void setSubPropertyType(String subPropertyType) {
        this.subPropertyType = subPropertyType;
    }

    public String getPlotSize() {
        return plotSize;
    }

    public void setPlotSize(String plotSize) {
        this.plotSize = plotSize;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public String getRequiredSquareFeet() {
        return requiredSquareFeet;
    }

    public void setRequiredSquareFeet(String requiredSquareFeet) {
        this.requiredSquareFeet = requiredSquareFeet;
    }

    public String getPreferredFloors() {
        return preferredFloors;
    }

    public void setPreferredFloors(String preferredFloors) {
        this.preferredFloors = preferredFloors;
    }

    public String getBedRoomType() {
        return bedRoomType;
    }

    public void setBedRoomType(String bedRoomType) {
        this.bedRoomType = bedRoomType;
    }

    public String getPostingType() {
        return postingType;
    }

    public void setPostingType(String postingType) {
        this.postingType = postingType;
    }

    public float getCommission() {
        return commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
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

    public static class Area {
        String microMarketName, microMarketCity, microMarketState;

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
