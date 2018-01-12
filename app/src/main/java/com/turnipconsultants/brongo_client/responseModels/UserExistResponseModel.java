package com.turnipconsultants.brongo_client.responseModels;

import java.util.List;

/**
 * Created by mohit on 28-09-2017.
 */

public class UserExistResponseModel {
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
        private String emailId;
        private String gPlusId;
        private String fBId;
        private String firstName;
        private String mobileNo;
        private long otp;

        public long getOtp() {
            return otp;
        }

        public void setOtp(long otp) {
            this.otp = otp;
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

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }
    }
}
