package com.turnipconsultants.brongo_client.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pankaj on 20-11-2017.
 */

public class PaymentHashModel {

    @SerializedName("amount")
    private String amount;
    @SerializedName("firstname")
    private String firstname;
    @SerializedName("productInfo")
    private String productInfo;
    @SerializedName("email")
    private String email;
    @SerializedName("mobileNo")
    private String mobileNo;
    @SerializedName("brokerMobileNo")
    private String brokerMobileNo;
    @SerializedName("paymentId")
    private String paymentId;
    @SerializedName("paymentMode")
    private String paymentMode;
    private String userType;
    private int isDevelopment;


    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getIsDevelopment() {
        return isDevelopment;
    }

    public void setIsDevelopment(int isDevelopment) {
        this.isDevelopment = isDevelopment;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getBrokerMobileNo() {
        return brokerMobileNo;
    }

    public void setBrokerMobileNo(String brokerMobileNo) {
        this.brokerMobileNo = brokerMobileNo;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }
}
