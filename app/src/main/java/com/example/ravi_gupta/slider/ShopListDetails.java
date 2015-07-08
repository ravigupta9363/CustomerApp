package com.example.ravi_gupta.slider;

/**
 * Created by Ravi-Gupta on 7/2/2015.
 */
public class ShopListDetails {
    String shopName;
    int discount;
    String address;
    boolean Isreturn;
    int deliveryTime;
    int orderFulfilment;
    boolean Isopen;


    public ShopListDetails() {
        super();
    }

    public ShopListDetails(String shopName, int discount, String address, boolean Isopen, boolean Isreturn, int deliveryTime, int orderFulfilment) {
        this.shopName = shopName;
        this.discount = discount;
        this.address = address;
        this.Isopen = Isopen;
        this.Isreturn = Isreturn;
        this.deliveryTime = deliveryTime;
        this.orderFulfilment = orderFulfilment;
    }
}
