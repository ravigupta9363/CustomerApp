package com.example.ravi_gupta.slider;

/**
 * Created by robins on 26/8/15.
 */
import android.app.Application;
import android.location.Address;
import android.net.Uri;

import com.example.ravi_gupta.slider.Models.Constants;
import com.example.ravi_gupta.slider.Models.Office;
import com.example.ravi_gupta.slider.Models.Order;
import com.example.ravi_gupta.slider.Models.Retailer;
import com.squareup.picasso.RequestCreator;
import com.strongloop.android.loopback.File;
import com.strongloop.android.loopback.RestAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    private RestAdapter adapter;
    private Office office;
    private List<Retailer> retailerList;

    public Address getUpdatedAddress() {
        return updatedAddress;
    }

    public void setUpdatedAddress(Address updatedAddress) {
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




    List<RequestCreator> imageList;

    public List<RequestCreator> getImageList() {
        return imageList;
    }

    public void setImageList(List<RequestCreator> imageList) {
        this.imageList = imageList;
    }




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
