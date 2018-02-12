package com.turnipconsultants.brongo_client.models;

/**
 * Created by mohit on 28-09-2017.
 */

public class TokenInputModel {
    private String platform;

    private String deviceId;

    private String mobileNo;
    private String version;


    public TokenInputModel(String platform, String deviceId, String mobileNo) {
        this.platform = platform;
        this.deviceId = deviceId;
        this.mobileNo = mobileNo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
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
