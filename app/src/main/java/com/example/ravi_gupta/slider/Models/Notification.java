package com.example.ravi_gupta.slider.Models;

import com.strongloop.android.loopback.Model;

/**
 * Created by robins on 31/8/15.
 */
public class Notification extends Model {
    private String id;

    private String message;
    private String urlArgs;
    private String messageFrom;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrlArgs() {
        return urlArgs;
    }

    public void setUrlArgs(String urlArgs) {
        this.urlArgs = urlArgs;
    }

    public String getMessageFrom() {
        return messageFrom;
    }

    public void setMessageFrom(String messageFrom) {
        this.messageFrom = messageFrom;
    }


}
