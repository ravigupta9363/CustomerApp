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
    private Map<String, Double> discount;
    private boolean isReturn;
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

    public Map<String, Double> getDiscount() {
        return discount;
    }

    public void setDiscount(Map<String, Double> discount) {
        this.discount = discount;
    }

    public boolean getReturn() {
        return isReturn;
    }

    public void setReturn() {
        this.isReturn = isReturn;
    }

}
