package com.example.ravi_gupta.slider.Repository;

import android.util.Log;

import com.example.ravi_gupta.slider.Models.Constants;
import com.example.ravi_gupta.slider.Models.Order;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.strongloop.android.loopback.ModelRepository;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;
import com.strongloop.android.remoting.JsonUtil;
import com.strongloop.android.remoting.adapters.Adapter;
import com.strongloop.android.remoting.adapters.RestContract;
import com.strongloop.android.remoting.adapters.RestContractItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ravi-Gupta on 8/13/2015.
 */
public class OrderRepository extends ModelRepository<Order> {
    public OrderRepository() {
        super("Order", null, Order.class);
    }

    public RestContract createContract() {
        RestContract contract = super.createContract();
        contract.addItem(new RestContractItem("/" + getNameForRestUrl() + "/:id", "GET"), getClassName() + ".findById");
        return contract;
    }



    public void getOrder(Object id, final ObjectCallback<Order> callback){
        invokeStaticMethod("findById", ImmutableMap.of("id", id), new Adapter.JsonObjectCallback(){
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(Constants.TAG, "Order fetched for given id" );
                Log.d(Constants.TAG, response.toString());
                OrderRepository orderRepo = getRestAdapter().createRepository(OrderRepository.class);
                Map<String, Object> orderObj = JsonUtil.fromJson(response);
                Order order = orderRepo.createObject(orderObj);
                callback.onSuccess(order);


               /* List<Map<String, Object>> objList = (List) JsonUtil.fromJson(response);
                OrderRepository orderRepo = getRestAdapter().createRepository(OrderRepository.class);
                Order order;
                if(objList.size() == 0){
                    callback.onSuccess(null);
                }
                //Now converting jsonObject to list
                for(Map<String, Object> orderObj: objList) {
                    order = orderRepo.createObject(orderObj);
                    callback.onSuccess(order);
                }*/
            }


            @Override
            public void onError(Throwable t) {
                Log.d(Constants.TAG, "An Error occured while fetching customer order");
                Log.d(Constants.TAG, t.toString());
                callback.onError(t);
            }
        });
    }
}



