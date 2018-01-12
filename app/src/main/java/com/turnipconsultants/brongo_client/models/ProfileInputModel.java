package com.turnipconsultants.brongo_client.models;

import retrofit2.http.Multipart;

/**
 * Created by mohit on 12-10-2017.
 */

public class ProfileInputModel {
    private String firstName;
    private String mobileNo;
    private String lastName;
    private String gender;
    private String alternateMobileNo;
    private Multipart profileImage;
    private boolean hideProfilePic;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Multipart getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Multipart profileImage) {
        this.profileImage = profileImage;
    }

    public boolean isHideProfilePic() {
        return hideProfilePic;
    }

    public void setHideProfilePic(boolean hideProfilePic) {
        this.hideProfilePic = hideProfilePic;
    }
}
