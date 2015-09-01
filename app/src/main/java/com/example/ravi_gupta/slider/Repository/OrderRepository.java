package com.example.ravi_gupta.slider.Repository;

import com.example.ravi_gupta.slider.Models.Order;
import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.callbacks.VoidCallback;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

/**
 * Created by Ravi-Gupta on 8/13/2015.
 */
public class OrderRepository extends ModelRepository<Order> {
    public OrderRepository() {
        super("Order", null, Order.class);
    }


}



