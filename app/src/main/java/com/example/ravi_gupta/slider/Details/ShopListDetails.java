package com.example.ravi_gupta.slider.Details;

import com.example.ravi_gupta.slider.Models.Retailer;

import java.util.Map;

/**
 * Created by Ravi-Gupta on 7/2/2015.
 */
public class ShopListDetails{
    public String shopName;
    public double  discount;
    public String address;
    public boolean Isreturn;
    public int deliveryTime;
    public int orderFulfilment;
    public boolean IsClosed;
    public Object id;

    //public Drawable icon;


    public ShopListDetails() {
        super();
    }

    public ShopListDetails(Object id,String shopName, double discount, String address, boolean IsClosed, boolean Isreturn, int orderFulfilment) {
        this.id = id;
        this.shopName = shopName;
        this.discount = discount;
        this.address = address;
        this.IsClosed = IsClosed;
        this.Isreturn = Isreturn;
        this.deliveryTime = deliveryTime;
        this.orderFulfilment = orderFulfilment;
        //this.icon = icon;
    }
}
