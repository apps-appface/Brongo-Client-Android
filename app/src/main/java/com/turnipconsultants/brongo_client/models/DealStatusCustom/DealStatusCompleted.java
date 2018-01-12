package com.turnipconsultants.brongo_client.models.DealStatusCustom;

/**
 * Created by Pankaj on 15-11-2017.
 */

public class DealStatusCompleted {
    private String status;
    private String date;

    public DealStatusCompleted(String status, String date) {
        this.status = status;
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
