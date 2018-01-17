package com.turnipconsultants.brongo_client.responseModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by mohit on 12-01-2018.
 */

@Data
public class SecondLandingResponse {
    @SerializedName("statusCode")
    private int statusCode;
    @SerializedName("data")
    private List<DataEntity> data;
    @SerializedName("message")
    private String message;

    @Data
    public static class DataEntity implements Parcelable {
        private String reminder;

        private String postingType;

        private List<String> statusUpdatedTimes;

        private int reviews;

        private String payment;

        private String status;

        private String dealClosedTime;

        private String timeOfVisit;

        private List<String> remainingStatus;

        private String subPropertyType;

        private List<String> property;

        private String brokerImage;

        private List<SiteVisit> siteVisit;

        private String planType;

        private String meetAt;

        private String propertyId;

        private String propertyType;

        private String brokerMobileNo;

        private String brokerName;

        private double rating;

        private double commission;

        private List<String> completedStatus;

        private String meetingLocation;

        private String dateOfVisit;

        @Data
        public class SiteVisit implements Parcelable {
            private String propertyName;

            private String meetAt;

            private String propertyId;

            private String timeOfVisit;

            private String siteVisitName;

            private String updatedTime;

            private String dateOfVisit;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.propertyName);
                dest.writeString(this.meetAt);
                dest.writeString(this.propertyId);
                dest.writeString(this.timeOfVisit);
                dest.writeString(this.siteVisitName);
                dest.writeString(this.updatedTime);
                dest.writeString(this.dateOfVisit);
            }

            protected SiteVisit(Parcel in) {
                this.propertyName = in.readString();
                this.meetAt = in.readString();
                this.propertyId = in.readString();
                this.timeOfVisit = in.readString();
                this.siteVisitName = in.readString();
                this.updatedTime = in.readString();
                this.dateOfVisit = in.readString();
            }

            public  final Creator<SiteVisit> CREATOR = new Creator<SiteVisit>() {
                @Override
                public SiteVisit createFromParcel(Parcel source) {
                    return new SiteVisit(source);
                }

                @Override
                public SiteVisit[] newArray(int size) {
                    return new SiteVisit[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            try {
                dest.writeString(this.reminder);
                dest.writeString(this.postingType);
                dest.writeStringList(this.statusUpdatedTimes);
                dest.writeInt(this.reviews);
                dest.writeString(this.payment);
                dest.writeString(this.status);
                dest.writeString(this.dealClosedTime);
                dest.writeString(this.timeOfVisit);
                dest.writeStringList(this.remainingStatus);
                dest.writeString(this.subPropertyType);
                dest.writeStringList(this.property);
                dest.writeString(this.brokerImage);
                dest.writeList(this.siteVisit);
                dest.writeString(this.planType);
                dest.writeString(this.meetAt);
                dest.writeString(this.propertyId);
                dest.writeString(this.propertyType);
                dest.writeString(this.brokerMobileNo);
                dest.writeString(this.brokerName);
                dest.writeDouble(this.rating);
                dest.writeDouble(this.commission);
                dest.writeStringList(this.completedStatus);
                dest.writeString(this.meetingLocation);
                dest.writeString(this.dateOfVisit);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected DataEntity(Parcel in) {
            this.reminder = in.readString();
            this.postingType = in.readString();
            this.statusUpdatedTimes = in.createStringArrayList();
            this.reviews = in.readInt();
            this.payment = in.readString();
            this.status = in.readString();
            this.dealClosedTime = in.readString();
            this.timeOfVisit = in.readString();
            this.remainingStatus = in.createStringArrayList();
            this.subPropertyType = in.readString();
            this.property = in.createStringArrayList();
            this.brokerImage = in.readString();
            this.siteVisit = new ArrayList<SiteVisit>();
            in.readList(this.siteVisit, SiteVisit.class.getClassLoader());
            this.planType = in.readString();
            this.meetAt = in.readString();
            this.propertyId = in.readString();
            this.propertyType = in.readString();
            this.brokerMobileNo = in.readString();
            this.brokerName = in.readString();
            this.rating = in.readDouble();
            this.commission = in.readDouble();
            this.completedStatus = in.createStringArrayList();
            this.meetingLocation = in.readString();
            this.dateOfVisit = in.readString();
        }

        public static final Parcelable.Creator<DataEntity> CREATOR = new Parcelable.Creator<DataEntity>() {
            @Override
            public DataEntity createFromParcel(Parcel source) {
                return new DataEntity(source);
            }

            @Override
            public DataEntity[] newArray(int size) {
                return new DataEntity[size];
            }
        };
    }
}
