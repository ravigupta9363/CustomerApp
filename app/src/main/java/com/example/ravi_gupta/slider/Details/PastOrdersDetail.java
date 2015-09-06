package com.example.ravi_gupta.slider.Details;

import java.util.List;
import java.util.Map;

/**
 * Created by Ravi-Gupta on 7/13/2015.
 */
public class PastOrdersDetail {

    public String date;
    public String time;
    public String orderId;
    //public int price;
    public String address;
    public List<Map<String, String>> drawable;
    public boolean isDelivered;
    public String retailerId;


    public PastOrdersDetail() {
        super();
    }

    public PastOrdersDetail(String date,
            String time,
            String orderId,
            String address,List<Map<String, String>> drawable,
            boolean isDelivered,
            String retailerId){
        this.date = date;
        this.time = time;
        this.orderId = orderId;
        //this.price = price;
        this.address = address;
        this.drawable = drawable;
        this.isDelivered = isDelivered;
        this.retailerId = retailerId;
    }
}
