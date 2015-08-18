package com.example.ravi_gupta.slider.Details;

/**
 * Created by Ravi-Gupta on 7/2/2015.
 */
public class ShopListDetails{
    public String shopName;
    public double discount;
    public String address;
    public boolean Isreturn;
    public int deliveryTime;
    public int orderFulfilment;
    public boolean Isopen;
    //public Drawable icon;


    public ShopListDetails() {
        super();
    }

    public ShopListDetails(String shopName, double discount, String address, boolean Isopen, boolean Isreturn, int orderFulfilment) {
        this.shopName = shopName;
        this.discount = discount;
        this.address = address;
        this.Isopen = Isopen;
        this.Isreturn = Isreturn;
        this.deliveryTime = deliveryTime;
        this.orderFulfilment = orderFulfilment;
        //this.icon = icon;
    }
}
