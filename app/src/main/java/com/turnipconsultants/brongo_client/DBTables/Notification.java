package com.turnipconsultants.brongo_client.DBTables;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Pankaj on 20-12-2017.
 */

@Entity(nameInDb = "notification_brongo")
public class Notification {

    @Id(autoincrement = true)
    private Long seq;

    @Property(nameInDb = "message")
    private String message;

    @Property(nameInDb = "brokerName")
    private String brokerName;

    @Property(nameInDb = "brokerProfile")
    private String brokerProfile;

    @Property(nameInDb = "isRead")
    private boolean isRead;

    @Property(nameInDb = "id")
    private String id;

    @Property(nameInDb = "alertType")
    private String alertType;

    @Property(nameInDb = "days")
    private String days;

    @Generated(hash = 1141576806)
    public Notification(Long seq, String message, String brokerName,
            String brokerProfile, boolean isRead, String id, String alertType,
            String days) {
        this.seq = seq;
        this.message = message;
        this.brokerName = brokerName;
        this.brokerProfile = brokerProfile;
        this.isRead = isRead;
        this.id = id;
        this.alertType = alertType;
        this.days = days;
    }

    @Generated(hash = 1855225820)
    public Notification() {
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBrokerName() {
        return this.brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public String getBrokerProfile() {
        return this.brokerProfile;
    }

    public void setBrokerProfile(String brokerProfile) {
        this.brokerProfile = brokerProfile;
    }

    public boolean getIsRead() {
        return this.isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlertType() {
        return this.alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getDays() {
        return this.days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public Long getSeq() {
        return this.seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }


}
