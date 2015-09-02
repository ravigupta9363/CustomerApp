package com.example.ravi_gupta.slider.Models;

import android.location.Geocoder;

import com.strongloop.android.loopback.Model;

import java.util.Date;
import java.util.Map;

/**
 * Created by Ravi-Gupta on 8/12/2015.
 */


public class Retailer extends Model {

    private String name;
    private int fulfillement;
    private String area;
    private Map<String, Object> discount;
    private boolean isReturn;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerContact() {
        return ownerContact;
    }

    public void setOwnerContact(String ownerContact) {
        this.ownerContact = ownerContact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getMaxPackingTime() {
        return maxPackingTime;
    }

    public void setMaxPackingTime(int maxPackingTime) {
        this.maxPackingTime = maxPackingTime;
    }

    public int getMinPackingTime() {
        return minPackingTime;
    }

    public void setMinPackingTime(int minPackingTime) {
        this.minPackingTime = minPackingTime;
    }

    public int getOpenIssues() {
        return openIssues;
    }

    public void setOpenIssues(int openIssues) {
        this.openIssues = openIssues;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getTimings() {
        return timings;
    }

    public void setTimings(Object timings) {
        this.timings = timings;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public Geocoder getMapLocation() {
        return mapLocation;
    }

    public void setMapLocation(Geocoder mapLocation) {
        this.mapLocation = mapLocation;
    }

    private String ownerName;
    private String ownerContact;
    private String address;
    private String contact;
    private String licenceNumber;
    private int totalOrders;
    private int totalSales;
    private Date created;
    private int maxPackingTime;
    private int minPackingTime;
    private int openIssues;
    private String status;
    private Object timings;
    private int pincode;
    private Geocoder mapLocation;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFulfillment() {
        return fulfillement;
    }

    public void setFulfillment(int fulfillement) {
        this.fulfillement = fulfillement;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Map<String, Object> getDiscount() {
        return discount;
    }

    public void setDiscount(Map<String, Object> discount) {
        this.discount = discount;
    }

    public boolean getReturn() {
        return isReturn;
    }

    public void setReturn() {
        this.isReturn = isReturn;
    }

}
