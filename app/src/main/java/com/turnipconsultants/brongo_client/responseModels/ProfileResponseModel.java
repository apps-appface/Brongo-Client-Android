package com.turnipconsultants.brongo_client.responseModels;

import java.util.List;

/**
 * Created by mohit on 13-10-2017.
 */

public class ProfileResponseModel {
    private String message;
    private int statusCode;
    private List<DataEntity> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

    public class DataEntity {
        private String fBId;
        private String gPlusId;
        private String firstName;
        private String lastName;
        private String emailId;
        private String mobileNo;
        private String gender;
        private String alternateMobileNo;
        private String hideProfilePic;
        private String profileImage;
        private int notificationBadge;
        private long activeRequests;
        private float rating;

        public long getActiveRequests() {
            return activeRequests;
        }

        public void setActiveRequests(long activeRequests) {
            this.activeRequests = activeRequests;
        }

        public float getRating() {
            return rating;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }

        public String getfBId() {
            return fBId;
        }

        public void setfBId(String fBId) {
            this.fBId = fBId;
        }

        public String getgPlusId() {
            return gPlusId;
        }

        public void setgPlusId(String gPlusId) {
            this.gPlusId = gPlusId;
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

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAlternateMobileNo() {
            return alternateMobileNo;
        }

        public void setAlternateMobileNo(String alternateMobileNo) {
            this.alternateMobileNo = alternateMobileNo;
        }

        public String getHideProfilePic() {
            return hideProfilePic;
        }

        public void setHideProfilePic(String hideProfilePic) {
            this.hideProfilePic = hideProfilePic;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public int getNotificationBadge() {
            return notificationBadge;
        }

        public void setNotificationBadge(int notificationBadge) {
            this.notificationBadge = notificationBadge;
        }
    }
}
