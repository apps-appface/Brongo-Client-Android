package com.turnipconsultants.brongo_client.responseModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mohit on 10-10-2017.
 */

public class SecondLandingResponseModel {

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


    public static class Data {
        @SerializedName("sellAndRentOut")
        private List<SellAndRentOut> sellAndRentOut;
        @SerializedName("potentialCommission")
        private float potentialCommission;
        @SerializedName("buyAndRent")
        private List<BuyAndRent> buyAndRent;

        public List<SellAndRentOut> getSellAndRentOut() {
            return sellAndRentOut;
        }

        public void setSellAndRentOut(List<SellAndRentOut> sellAndRentOut) {
            this.sellAndRentOut = sellAndRentOut;
        }

        public float getPotentialCommission() {
            return potentialCommission;
        }

        public void setPotentialCommission(float potentialCommission) {
            this.potentialCommission = potentialCommission;
        }

        public List<BuyAndRent> getBuyAndRent() {
            return buyAndRent;
        }

        public void setBuyAndRent(List<BuyAndRent> buyAndRent) {
            this.buyAndRent = buyAndRent;
        }


        public static class BuyAndRent implements Parcelable {
            @SerializedName("brokerName")
            private String brokerName;
            @SerializedName("brokerMobileNo")
            private String brokerMobileNo;
            @SerializedName("brokerImage")
            private String brokerImage;
            @SerializedName("planType")
            private String planType;
            @SerializedName("rating")
            private float rating;
            @SerializedName("reviews")
            private int reviews;
            @SerializedName("propertyId")
            private String propertyId;
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
            @SerializedName("bedRoomType")
            private String bedRoomType;
            @SerializedName("budget")
            private String budget;
            @SerializedName("propertyStatus")
            private String propertyStatus;
            @SerializedName("leadStatus")
            private String leadStatus;
            @SerializedName("meetingLocation")
            private String meetingLocation;
            @SerializedName("commission")
            private float commission;
            @SerializedName("dateOfVisit")
            private String dateOfVisit;
            @SerializedName("timeOfVisit")
            private String timeOfVisit;
            @SerializedName("meetAt")
            private String meetAt;
            @SerializedName("siteVisit")
            private List<SiteVisit> siteVisit;
            @SerializedName("regDate")
            private String regDate;
            @SerializedName("completedStatus")
            private List<String> completedStatus;
            @SerializedName("statusUpdatedTimes")
            private List<String> statusUpdatedTimes;
            @SerializedName("remainingStatus")
            private List<String> remainingStatus;
            @SerializedName("reminder")
            private String reminder;
            @SerializedName("dealClosedTime")
            private String dealClosedTime;
            @SerializedName("budgetRange1")
            private int budgetRange1;
            @SerializedName("budgetRange2")
            private int budgetRange2;
            @SerializedName("reSquareFeetRange1")
            private String reSquareFeetRange1;
            @SerializedName("reSquareFeetRange2")
            private String reSquareFeetRange2;
            @SerializedName("carParking")
            private String carParking;
            @SerializedName("preferredFloors")
            private String preferredFloors;


            @SerializedName("maritalStatus")
            private String maritalStatus;
            @SerializedName("preferredProjects")
            private String preferredProjects;
            @SerializedName("avoidProjects")
            private String avoidProjects;
            @SerializedName("furnishingStatus")
            private String furnishingStatus;
            @SerializedName("orientation")
            private String orientation;
            @SerializedName("comments")
            private String comments;

            protected BuyAndRent(Parcel in) {
                brokerName = in.readString();
                brokerMobileNo = in.readString();
                brokerImage = in.readString();
                planType = in.readString();
                rating = in.readFloat();
                reviews = in.readInt();
                propertyId = in.readString();
                postingType = in.readString();
                propertyType = in.readString();
                microMarketName = in.readString();
                rentalType = in.readString();
                subPropertyType = in.readString();
                bedRoomType = in.readString();
                budget = in.readString();
                propertyStatus = in.readString();
                leadStatus = in.readString();
                meetingLocation = in.readString();
                commission = in.readInt();
                dateOfVisit = in.readString();
                timeOfVisit = in.readString();
                meetAt = in.readString();
                regDate = in.readString();
                completedStatus = in.createStringArrayList();
                statusUpdatedTimes = in.createStringArrayList();
                remainingStatus = in.createStringArrayList();
                reminder = in.readString();
                dealClosedTime = in.readString();
                budgetRange1 = in.readInt();
                budgetRange2 = in.readInt();
                reSquareFeetRange1 = in.readString();
                reSquareFeetRange2 = in.readString();
                carParking = in.readString();
                preferredFloors = in.readString();
                maritalStatus = in.readString();
                preferredProjects = in.readString();
                avoidProjects = in.readString();
                furnishingStatus = in.readString();
                orientation = in.readString();
                comments=in.readString();
            }

            public static final Creator<BuyAndRent> CREATOR = new Creator<BuyAndRent>() {
                @Override
                public BuyAndRent createFromParcel(Parcel in) {
                    return new BuyAndRent(in);
                }

                @Override
                public BuyAndRent[] newArray(int size) {
                    return new BuyAndRent[size];
                }
            };

            public String getComments() {
                return comments;
            }

            public void setComments(String comments) {
                this.comments = comments;
            }

            public String getMaritalStatus() {
                return maritalStatus;
            }

            public void setMaritalStatus(String maritalStatus) {
                this.maritalStatus = maritalStatus;
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

            public String getFurnishingStatus() {
                return furnishingStatus;
            }

            public void setFurnishingStatus(String furnishingStatus) {
                this.furnishingStatus = furnishingStatus;
            }

            public String getOrientation() {
                return orientation;
            }

            public void setOrientation(String orientation) {
                this.orientation = orientation;
            }

            public String getReSquareFeetRange1() {
                return reSquareFeetRange1;
            }

            public String getPreferredFloors() {
                return preferredFloors;
            }

            public void setPreferredFloors(String preferredFloors) {
                this.preferredFloors = preferredFloors;
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

            public String getCarParking() {
                return carParking;
            }

            public void setCarParking(String carParking) {
                this.carParking = carParking;
            }

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

            public String getPlanType() {
                return planType;
            }

            public void setPlanType(String planType) {
                this.planType = planType;
            }

            public float getRating() {
                return rating;
            }

            public void setRating(float rating) {
                this.rating = rating;
            }

            public int getReviews() {
                return reviews;
            }

            public void setReviews(int reviews) {
                this.reviews = reviews;
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

            public String getBedRoomType() {
                return bedRoomType;
            }

            public void setBedRoomType(String bedRoomType) {
                this.bedRoomType = bedRoomType;
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

            public float getCommission() {
                return commission;
            }

            public void setCommission(float commission) {
                this.commission = commission;
            }

            public String getDateOfVisit() {
                return dateOfVisit;
            }

            public void setDateOfVisit(String dateOfVisit) {
                this.dateOfVisit = dateOfVisit;
            }

            public String getTimeOfVisit() {
                return timeOfVisit;
            }

            public void setTimeOfVisit(String timeOfVisit) {
                this.timeOfVisit = timeOfVisit;
            }

            public String getMeetAt() {
                return meetAt;
            }

            public void setMeetAt(String meetAt) {
                this.meetAt = meetAt;
            }

            public List<SiteVisit> getSiteVisit() {
                return siteVisit;
            }

            public void setSiteVisit(List<SiteVisit> siteVisit) {
                this.siteVisit = siteVisit;
            }

            public String getRegDate() {
                return regDate;
            }

            public void setRegDate(String regDate) {
                this.regDate = regDate;
            }

            public List<String> getCompletedStatus() {
                return completedStatus;
            }

            public void setCompletedStatus(List<String> completedStatus) {
                this.completedStatus = completedStatus;
            }

            public List<String> getStatusUpdatedTimes() {
                return statusUpdatedTimes;
            }

            public void setStatusUpdatedTimes(List<String> statusUpdatedTimes) {
                this.statusUpdatedTimes = statusUpdatedTimes;
            }

            public List<String> getRemainingStatus() {
                return remainingStatus;
            }

            public void setRemainingStatus(List<String> remainingStatus) {
                this.remainingStatus = remainingStatus;
            }

            public String getReminder() {
                return reminder;
            }

            public void setReminder(String reminder) {
                this.reminder = reminder;
            }

            public String getDealClosedTime() {
                return dealClosedTime;
            }

            public void setDealClosedTime(String dealClosedTime) {
                this.dealClosedTime = dealClosedTime;
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

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(brokerName);
                dest.writeString(brokerMobileNo);
                dest.writeString(brokerImage);
                dest.writeString(planType);
                dest.writeFloat(rating);
                dest.writeInt(reviews);
                dest.writeString(propertyId);
                dest.writeString(postingType);
                dest.writeString(propertyType);
                dest.writeString(microMarketName);
                dest.writeString(rentalType);
                dest.writeString(subPropertyType);
                dest.writeString(bedRoomType);
                dest.writeString(budget);
                dest.writeString(propertyStatus);
                dest.writeString(leadStatus);
                dest.writeString(meetingLocation);
                dest.writeFloat(commission);
                dest.writeString(dateOfVisit);
                dest.writeString(timeOfVisit);
                dest.writeString(meetAt);
                dest.writeString(regDate);
                dest.writeStringList(completedStatus);
                dest.writeStringList(statusUpdatedTimes);
                dest.writeStringList(remainingStatus);
                dest.writeString(reminder);
                dest.writeString(dealClosedTime);
                dest.writeInt(budgetRange1);
                dest.writeInt(budgetRange2);
                dest.writeString(reSquareFeetRange1);
                dest.writeString(reSquareFeetRange2);
                dest.writeString(carParking);
                dest.writeString(preferredFloors);
                dest.writeString(maritalStatus);
                dest.writeString(preferredProjects);
                dest.writeString(avoidProjects);
                dest.writeString(furnishingStatus);
                dest.writeString(orientation);
                dest.writeString(comments);
            }
        }

        public static class SellAndRentOut implements Parcelable {

            @SerializedName("brokerName")
            private String brokerName;
            @SerializedName("brokerMobileNo")
            private String brokerMobileNo;
            @SerializedName("brokerImage")
            private String brokerImage;
            @SerializedName("planType")
            private String planType;
            @SerializedName("rating")
            private float rating;
            @SerializedName("reviews")
            private int reviews;
            @SerializedName("propertyId")
            private String propertyId;
            @SerializedName("postingType")
            private String postingType;
            @SerializedName("propertyType")
            private String propertyType;
            @SerializedName("microMarketName")
            private String microMarketName;
            @SerializedName("subPropertyType")
            private String subPropertyType;
            @SerializedName("bedRoomType")
            private String bedRoomType;
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
            @SerializedName("commission")
            private float commission;
            @SerializedName("dateOfVisit")
            private String dateOfVisit;
            @SerializedName("timeOfVisit")
            private String timeOfVisit;
            @SerializedName("meetAt")
            private String meetAt;
            @SerializedName("siteVisit")
            private List<SiteVisit> siteVisit;
            @SerializedName("regDate")
            private String regDate;
            @SerializedName("completedStatus")
            private List<String> completedStatus;
            @SerializedName("statusUpdatedTimes")
            private List<String> statusUpdatedTimes;
            @SerializedName("remainingStatus")
            private List<String> remainingStatus;
            @SerializedName("reminder")
            private String reminder;
            @SerializedName("dealClosedTime")
            private String dealClosedTime;
            @SerializedName("payment")
            private boolean payment;
            @SerializedName("projectName")
            private String projectName;
            @SerializedName("expectedPrice")
            private String expectedPrice;
            @SerializedName("furnishingStatus")
            private String furnishingStatus;
            @SerializedName("availableFloors")
            private String availableFloors;
            @SerializedName("orientation")
            private String orientation;
            @SerializedName("comments")
            private String comments;
            @SerializedName("totalBuildUpAreaInSquareFT")
            private String totalBuildUpAreaInSquareFT;
            @SerializedName("floors")
            private String floors;
            @SerializedName("possessionStatus")
            private String possessionStatus;
            @SerializedName("possessionBy")
            private String possessionBy;
            @SerializedName("expectedRent")
            private String expectedRent;
            @SerializedName("expectedDeposit")
            private String expectedDeposit;
            @SerializedName("tenantType")
            private String tenantType;
            @SerializedName("availabilityStatus")
            private String availabilityStatus;
            @SerializedName("availableSquareFeet")
            private String availableSquareFeet;


            protected SellAndRentOut(Parcel in) {
                brokerName = in.readString();
                brokerMobileNo = in.readString();
                brokerImage = in.readString();
                planType = in.readString();
                rating = in.readFloat();
                reviews = in.readInt();
                propertyId = in.readString();
                postingType = in.readString();
                propertyType = in.readString();
                microMarketName = in.readString();
                subPropertyType = in.readString();
                bedRoomType = in.readString();
                budget = in.readString();
                budgetRange1 = in.readInt();
                budgetRange2 = in.readInt();
                propertyStatus = in.readString();
                leadStatus = in.readString();
                meetingLocation = in.readString();
                commission = in.readInt();
                dateOfVisit = in.readString();
                timeOfVisit = in.readString();
                meetAt = in.readString();
                regDate = in.readString();
                completedStatus = in.createStringArrayList();
                statusUpdatedTimes = in.createStringArrayList();
                remainingStatus = in.createStringArrayList();
                reminder = in.readString();
                dealClosedTime = in.readString();
                projectName = in.readString();
                expectedPrice = in.readString();
                furnishingStatus = in.readString();
                availableFloors = in.readString();
                orientation = in.readString();
                comments = in.readString();
                totalBuildUpAreaInSquareFT = in.readString();
                floors = in.readString();
                possessionStatus = in.readString();
                possessionBy = in.readString();
                expectedRent = in.readString();
                expectedDeposit = in.readString();
                tenantType = in.readString();
                availabilityStatus = in.readString();
                availableSquareFeet = in.readString();
                payment = in.readByte() != 0;
            }

            public static final Creator<SellAndRentOut> CREATOR = new Creator<SellAndRentOut>() {
                @Override
                public SellAndRentOut createFromParcel(Parcel in) {
                    return new SellAndRentOut(in);
                }

                @Override
                public SellAndRentOut[] newArray(int size) {
                    return new SellAndRentOut[size];
                }
            };


            public String getAvailableSquareFeet() {
                return availableSquareFeet;
            }

            public void setAvailableSquareFeet(String availableSquareFeet) {
                this.availableSquareFeet = availableSquareFeet;
            }

            public String getExpectedRent() {
                return expectedRent;
            }

            public void setExpectedRent(String expectedRent) {
                this.expectedRent = expectedRent;
            }

            public String getExpectedDeposit() {
                return expectedDeposit;
            }

            public void setExpectedDeposit(String expectedDeposit) {
                this.expectedDeposit = expectedDeposit;
            }

            public String getTenantType() {
                return tenantType;
            }

            public void setTenantType(String tenantType) {
                this.tenantType = tenantType;
            }

            public String getAvailabilityStatus() {
                return availabilityStatus;
            }

            public void setAvailabilityStatus(String availabilityStatus) {
                this.availabilityStatus = availabilityStatus;
            }

            public String getPossessionStatus() {
                return possessionStatus;
            }

            public void setPossessionStatus(String possessionStatus) {
                this.possessionStatus = possessionStatus;
            }

            public String getPossessionBy() {
                return possessionBy;
            }

            public void setPossessionBy(String possessionBy) {
                this.possessionBy = possessionBy;
            }

            public String getTotalBuildUpAreaInSquareFT() {
                return totalBuildUpAreaInSquareFT;
            }

            public void setTotalBuildUpAreaInSquareFT(String totalBuildUpAreaInSquareFT) {
                this.totalBuildUpAreaInSquareFT = totalBuildUpAreaInSquareFT;
            }

            public String getFloors() {
                return floors;
            }

            public void setFloors(String floors) {
                this.floors = floors;
            }

            public boolean isPayment() {
                return payment;
            }

            public String getProjectName() {
                return projectName;
            }

            public void setProjectName(String projectName) {
                this.projectName = projectName;
            }

            public String getExpectedPrice() {
                return expectedPrice;
            }

            public void setExpectedPrice(String expectedPrice) {
                this.expectedPrice = expectedPrice;
            }

            public String getFurnishingStatus() {
                return furnishingStatus;
            }

            public void setFurnishingStatus(String furnishingStatus) {
                this.furnishingStatus = furnishingStatus;
            }

            public String getAvailableFloors() {
                return availableFloors;
            }

            public void setAvailableFloors(String availableFloors) {
                this.availableFloors = availableFloors;
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

            public String getPlanType() {
                return planType;
            }

            public void setPlanType(String planType) {
                this.planType = planType;
            }

            public float getRating() {
                return rating;
            }

            public void setRating(float rating) {
                this.rating = rating;
            }

            public int getReviews() {
                return reviews;
            }

            public void setReviews(int reviews) {
                this.reviews = reviews;
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

            public String getSubPropertyType() {
                return subPropertyType;
            }

            public void setSubPropertyType(String subPropertyType) {
                this.subPropertyType = subPropertyType;
            }

            public String getBedRoomType() {
                return bedRoomType;
            }

            public void setBedRoomType(String bedRoomType) {
                this.bedRoomType = bedRoomType;
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

            public float getCommission() {
                return commission;
            }

            public void setCommission(float commission) {
                this.commission = commission;
            }

            public String getDateOfVisit() {
                return dateOfVisit;
            }

            public void setDateOfVisit(String dateOfVisit) {
                this.dateOfVisit = dateOfVisit;
            }

            public String getTimeOfVisit() {
                return timeOfVisit;
            }

            public void setTimeOfVisit(String timeOfVisit) {
                this.timeOfVisit = timeOfVisit;
            }

            public String getMeetAt() {
                return meetAt;
            }

            public void setMeetAt(String meetAt) {
                this.meetAt = meetAt;
            }

            public List<SiteVisit> getSiteVisit() {
                return siteVisit;
            }

            public void setSiteVisit(List<SiteVisit> siteVisit) {
                this.siteVisit = siteVisit;
            }

            public String getRegDate() {
                return regDate;
            }

            public void setRegDate(String regDate) {
                this.regDate = regDate;
            }

            public List<String> getCompletedStatus() {
                return completedStatus;
            }

            public void setCompletedStatus(List<String> completedStatus) {
                this.completedStatus = completedStatus;
            }

            public List<String> getStatusUpdatedTimes() {
                return statusUpdatedTimes;
            }

            public void setStatusUpdatedTimes(List<String> statusUpdatedTimes) {
                this.statusUpdatedTimes = statusUpdatedTimes;
            }

            public List<String> getRemainingStatus() {
                return remainingStatus;
            }

            public void setRemainingStatus(List<String> remainingStatus) {
                this.remainingStatus = remainingStatus;
            }

            public String getReminder() {
                return reminder;
            }

            public void setReminder(String reminder) {
                this.reminder = reminder;
            }

            public String getDealClosedTime() {
                return dealClosedTime;
            }

            public void setDealClosedTime(String dealClosedTime) {
                this.dealClosedTime = dealClosedTime;
            }

            public boolean getPayment() {
                return payment;
            }

            public void setPayment(boolean payment) {
                this.payment = payment;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(brokerName);
                dest.writeString(brokerMobileNo);
                dest.writeString(brokerImage);
                dest.writeString(planType);
                dest.writeFloat(rating);
                dest.writeInt(reviews);
                dest.writeString(propertyId);
                dest.writeString(postingType);
                dest.writeString(propertyType);
                dest.writeString(microMarketName);
                dest.writeString(subPropertyType);
                dest.writeString(bedRoomType);
                dest.writeString(budget);
                dest.writeInt(budgetRange1);
                dest.writeInt(budgetRange2);
                dest.writeString(propertyStatus);
                dest.writeString(leadStatus);
                dest.writeString(meetingLocation);
                dest.writeFloat(commission);
                dest.writeString(dateOfVisit);
                dest.writeString(timeOfVisit);
                dest.writeString(meetAt);
                dest.writeString(regDate);
                dest.writeStringList(completedStatus);
                dest.writeStringList(statusUpdatedTimes);
                dest.writeStringList(remainingStatus);
                dest.writeString(reminder);
                dest.writeString(dealClosedTime);
                dest.writeByte((byte) (payment ? 1 : 0));
                dest.writeString(projectName);
                dest.writeString(expectedPrice);
                dest.writeString(furnishingStatus);
                dest.writeString(availableFloors);
                dest.writeString(orientation);
                dest.writeString(comments);
                dest.writeString(totalBuildUpAreaInSquareFT);
                dest.writeString(floors);
                dest.writeString(possessionStatus);
                dest.writeString(possessionBy);
                dest.writeString(expectedRent);
                dest.writeString(expectedDeposit);
                dest.writeString(tenantType);
                dest.writeString(availabilityStatus);
                dest.writeString(availableSquareFeet);

            }

            public static class SiteVisit {
            }
        }

        public static class SiteVisit {
        }
    }
}
