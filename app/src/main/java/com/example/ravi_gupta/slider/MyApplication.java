package com.example.ravi_gupta.slider;

/**
 * Created by robins on 26/8/15.
 */
import android.app.Application;
import android.location.Address;
import android.net.Uri;

import com.example.ravi_gupta.slider.Models.Constants;
import com.example.ravi_gupta.slider.Models.Message;
import com.example.ravi_gupta.slider.Models.Office;
import com.example.ravi_gupta.slider.Models.Order;
import com.example.ravi_gupta.slider.Models.Retailer;
import com.google.gson.Gson;
import com.squareup.picasso.RequestCreator;
import com.strongloop.android.loopback.File;
import com.strongloop.android.loopback.RestAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    private RestAdapter adapter;
    private Office office;
    private List<Retailer> retailerList;

    public List<java.io.File> getImageFileArray() {
        return imageFileArray;
    }


   /* public void setImageFileArray(java.io.File[] imageFileArray) {
        this.imageFileArray = imageFileArray;
    }*/

    // TODO: Convert this array into list
    private List<java.io.File> imageFileArray = new ArrayList<>();

    public Address getUpdatedAddress() {
        return updatedAddress;
    }

    public void setUpdatedAddress(Address updatedAddress) {
        //Gson gson = new Gson();
        //final Message data = gson.fromJson(updatedAddress, Address.class);
        this.updatedAddress = updatedAddress;
    }

    private Address updatedAddress;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    private Order order;




    //List<RequestCreator> imageList;

/*
    public List<RequestCreator> getImageList() {
        return imageList;
    }

    public void setImageList(List<RequestCreator> imageList) {
        this.imageList = imageList;
    }
*/




    public List<Retailer> getRetailerList() {
        return retailerList;
    }

    public void setRetailerList(List<Retailer> retailerList) {
        this.retailerList = retailerList;
    }


    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public RestAdapter getLoopBackAdapter() {
        if (adapter == null) {
            adapter = new RestAdapter(
                    getApplicationContext(),
                    Constants.apiUrl);
        }
        return adapter;
    }

}
