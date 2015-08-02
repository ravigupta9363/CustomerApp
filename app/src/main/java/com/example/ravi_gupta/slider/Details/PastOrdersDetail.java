package com.example.ravi_gupta.slider.Details;

import android.graphics.drawable.Drawable;

/**
 * Created by Ravi-Gupta on 7/13/2015.
 */
public class PastOrdersDetail {

    public String date;
    public String time;
    public String orderId;
    public int price;
    public String address;
    public Drawable drawable;

    public PastOrdersDetail() {
        super();
    }

    public PastOrdersDetail(String date,
            String time,
            String orderId,
            int price,
            String address,Drawable drawable){
        this.date = date;
        this.time = time;
        this.orderId = orderId;
        this.price = price;
        this.address = address;
        this.drawable = drawable;
    }
}
