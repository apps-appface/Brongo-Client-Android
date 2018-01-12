package com.turnipconsultants.brongo_client.models;

/**
 * Created by mohit on 27-09-2017.
 */

public class SignUpInputModel{
    private String lastName;
    private String platform;
    private String emailId;
    private String gPlusId;
    private String fBId;
    private String firstName;
    private String loginType;
    private String deviceId;
    private String mobileNo;


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getgPlusId() {
        return gPlusId;
    }

    public void setgPlusId(String gPlusId) {
        this.gPlusId = gPlusId;
    }

    public String getfBId() {
        return fBId;
    }

    public void setfBId(String fBId) {
        this.fBId = fBId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
