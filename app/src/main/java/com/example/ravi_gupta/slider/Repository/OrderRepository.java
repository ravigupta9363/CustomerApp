package com.example.ravi_gupta.slider.Repository;

import com.example.ravi_gupta.slider.Models.Order;
import com.strongloop.android.loopback.ModelRepository;

/**
 * Created by Ravi-Gupta on 8/13/2015.
 */
public class OrderRepository extends ModelRepository<Order> {
    public OrderRepository() {
        super("Order", null, Order.class);
    }


  


}



