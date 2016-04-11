package com.corporatetaxi.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Evon on 12/14/2015.
 */
public class NotificationhistoryModel {
    @DatabaseField(columnName = "_ID", id = true, generatedId = false)
    private int notificationId;
    @DatabaseField(columnName = "notification_Name")
    private String notificationName;
    @DatabaseField(columnName = "notification_Message")
    private String notificationMessage;
    @DatabaseField(columnName = "notification_Date")
    private String notificationDate;
    @DatabaseField(columnName = "notificatio_Time")
    private String notificatioTime;

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationName() {
        return notificationName;
    }

    public void setNotificationName(String notificationName) {
        this.notificationName = notificationName;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getNotificatioTime() {
        return notificatioTime;
    }

    public void setNotificatioTime(String notificatioTime) {
        this.notificatioTime = notificatioTime;
    }
}
