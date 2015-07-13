package com.example.ravi_gupta.slider.Details;

/**
 * Created by Ravi-Gupta on 7/13/2015.
 */
public class PastOrdersDetail {

    public String date;
    public String time;
    public String orderId;
    public int price;
    public String address;
    public String status;

    public PastOrdersDetail() {
        super();
    }

    public PastOrdersDetail(String date,
            String time,
            String orderId,
            int price,
            String address,
            String status){
        this.date = date;
        this.time = time;
        this.orderId = orderId;
        this.price = price;
        this.address = address;
        this.status = status;
    }
}
