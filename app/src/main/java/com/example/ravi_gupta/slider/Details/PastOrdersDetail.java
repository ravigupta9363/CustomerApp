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
    public List<Map<String, Map>> drawable;
    public boolean isDelivered;
    public String retailerId;
    public String status;


    public PastOrdersDetail() {
        super();
    }

    public PastOrdersDetail(String date,
                            String time,
                            String orderId,
                            String address,List<Map<String, Map>> drawable,
                            String status,
                            String retailerId){
        this.date = date;
        this.time = time;
        this.orderId = orderId;
        //this.price = price;
        this.address = address;
        this.drawable = drawable;
        this.status = status;
        this.retailerId = retailerId;
    }
}
