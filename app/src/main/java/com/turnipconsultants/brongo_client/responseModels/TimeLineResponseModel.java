package com.turnipconsultants.brongo_client.responseModels;

import java.util.List;

/**
 * Created by mohit on 03-10-2017.
 */

public class TimeLineResponseModel {
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
        private List<SiteVisit> siteVisit;

        private String reminder;

        private List<String> Completedstatus;

        private List<String> remainingstatus;

        private String propertyId;

        private String dealClosedTime;

        private String brokerMobileNo;

        private String regDate;

        private String clientMobileNo;

        private float commission;

        public List<SiteVisit> getSiteVisit() {
            return siteVisit;
        }

        public void setSiteVisit(List<SiteVisit> siteVisit) {
            this.siteVisit = siteVisit;
        }

        public String getReminder() {
            return reminder;
        }

        public void setReminder(String reminder) {
            this.reminder = reminder;
        }

        public List<String> getCompletedstatus() {
            return Completedstatus;
        }

        public void setCompletedstatus(List<String> completedstatus) {
            Completedstatus = completedstatus;
        }

        public List<String> getRemainingstatus() {
            return remainingstatus;
        }

        public void setRemainingstatus(List<String> remainingstatus) {
            this.remainingstatus = remainingstatus;
        }

        public String getPropertyId() {
            return propertyId;
        }

        public void setPropertyId(String propertyId) {
            this.propertyId = propertyId;
        }

        public String getDealClosedTime() {
            return dealClosedTime;
        }

        public void setDealClosedTime(String dealClosedTime) {
            this.dealClosedTime = dealClosedTime;
        }

        public String getBrokerMobileNo() {
            return brokerMobileNo;
        }

        public void setBrokerMobileNo(String brokerMobileNo) {
            this.brokerMobileNo = brokerMobileNo;
        }

        public String getRegDate() {
            return regDate;
        }

        public void setRegDate(String regDate) {
            this.regDate = regDate;
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

        public class SiteVisit {
            private String propertyName;

            private String meetAt;

            private String propertyId;

            private String timeOfVisit;

            private String siteVisitName;

            private String dateOfVisit;

        }
    }
}
