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


    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public Map<String, String> getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(Map<String, String> geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Boolean> getCallCustomer() {
        return callCustomer;
    }

    public void setCallCustomer(Map<String, Boolean> callCustomer) {
        this.callCustomer = callCustomer;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSendNotice() {
        return sendNotice;
    }

    public void setSendNotice(String sendNotice) {
        this.sendNotice = sendNotice;
    }

    public Map<String, String> getForwardOrder() {
        return forwardOrder;
    }

    public void setForwardOrder(Map<String, String> forwardOrder) {
        this.forwardOrder = forwardOrder;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }

    public String getPrototypeStatusCode() {
        return prototypeStatusCode;
    }

    public void setPrototypeStatusCode(String prototypeStatusCode) {
        this.prototypeStatusCode = prototypeStatusCode;
    }

    public String getPrototypeOrderCancelReason() {
        return prototypeOrderCancelReason;
    }

    public void setPrototypeOrderCancelReason(String prototypeOrderCancelReason) {
        this.prototypeOrderCancelReason = prototypeOrderCancelReason;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    private String officeId;

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    private String registrationId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    private String customerId;



    private String code;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


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
