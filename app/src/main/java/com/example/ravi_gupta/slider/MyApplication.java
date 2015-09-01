package com.example.ravi_gupta.slider;

/**
 * Created by robins on 26/8/15.
 */
import android.app.Application;

import com.example.ravi_gupta.slider.Models.Office;
import com.example.ravi_gupta.slider.Models.Retailer;
import com.strongloop.android.loopback.RestAdapter;

import java.util.List;

public class MyApplication extends Application {

    RestAdapter adapter;
    Office office;
    List<Retailer> retailerList;


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
            // Instantiate the shared RestAdapter. In most circumstances,
            // you'll do this only once; putting that reference in a singleton
            // is recommended for the sake of simplicity.
            // However, some applications will need to talk to more than one
            // server - create as many Adapters as you need.
            adapter = new RestAdapter(
                    getApplicationContext(),

                    "http://192.168.1.100:3001/api/");

        }
        return adapter;
    }

}
