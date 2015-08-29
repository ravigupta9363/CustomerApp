package com.example.ravi_gupta.slider.Models;

import com.strongloop.android.loopback.Model;

import java.util.List;
import java.util.Map;

/**
 * Created by Ravi-Gupta on 8/13/2015.
 */
public class Order extends Model {

    private int _id;
    private String date;
    private String googleAddr;
    private String landmark;
    private int pincode;
    private String flatNo;
    private Map<String, String> geoLocation;
    private String status;
    private Map<String, Boolean> callCustomer;
    private String note;
    private String sendNotice;
    private Map<String, String> forwardOrder;
    private List<Map<String, String>> prescription;
    private String retailerId;
    private String prototypeStatusCode;
    private String prototypeOrderCancelReason;



    public void setOrderId(int id) {
        this._id = _id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Map<String, String>> getPrescription() {
        return prescription;
    }

    public void setPrescription(List<Map<String, String>> prescription) {
        this.prescription = prescription;
    }

    public String getGoogleAddr() {
        return  googleAddr;
    }

    public void setGoogleAddr(String googleAddr) {
        this.googleAddr = googleAddr;
    }
}
