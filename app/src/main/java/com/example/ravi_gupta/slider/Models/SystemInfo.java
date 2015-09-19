package com.example.ravi_gupta.slider.Models;

import com.strongloop.android.loopback.Model;

/**
 * Created by robins on 5/9/15.
 */
public class SystemInfo extends Model {
    private String info;
    private String html;
    private String edited;


    public String getInfoType() {
        return info;
    }

    public void setInfoType(String infoType) {
        this.info = infoType;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getEdited() {
        return edited;
    }

    public void setEdited(String edited) {
        this.edited = edited;
    }

}
