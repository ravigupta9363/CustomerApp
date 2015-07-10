package com.example.ravi_gupta.slider.Details;

/**
 * Created by Ravi-Gupta on 7/10/2015.
 */
public class NotificationItemDetail {
    String notificationHeading;
    String notificationDetail;

    public NotificationItemDetail() {
        super();
    }

    public NotificationItemDetail(String notificationHeading, String notificationDetail) {
        this.notificationHeading = notificationHeading;
        this.notificationDetail = notificationDetail;
    }

    public String getNotificationHeading() {
        return notificationHeading;
    }

    public void setNotificationHeading(String mText1) {
        this.notificationHeading = mText1;
    }

    public String getNotificationDetail() {
        return notificationDetail;
    }

    public void setNotificationDetail(String mText2) {
        this.notificationDetail = mText2;
    }
}
