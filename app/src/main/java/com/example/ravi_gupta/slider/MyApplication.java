package com.example.ravi_gupta.slider;

/**
 * Created by robins on 26/8/15.
 */
import android.app.Application;
import android.content.SharedPreferences;
import android.location.Address;

import com.example.ravi_gupta.slider.Models.Constants;
import com.example.ravi_gupta.slider.Models.Office;
import com.example.ravi_gupta.slider.Models.Order;
import com.example.ravi_gupta.slider.Models.Retailer;
import com.google.gson.Gson;
import com.strongloop.android.loopback.RestAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    private RestAdapter adapter;
    private Office office;
    private List<Retailer> retailerList;
    private Address updatedAddress;

    public String getShowSplash(MainActivity activity) {
        if(this.showSplash == null){
            String json = getData(activity, Constants.splash);
            this.showSplash = json;
        }
        return this.showSplash;
    }

    public void setShowSplash(String showSplash, MainActivity activity) {
        this.showSplash = showSplash;
        addData(activity, Constants.splash, this.showSplash);
    }

    private String showSplash;

    public List<java.io.File> getImageFileArray() {
        return imageFileArray;
    }


    // TODO: Convert this array into list
    private List<java.io.File> imageFileArray = new ArrayList<>();

    public Address getUpdatedAddress(MainActivity activity) {
        if(this.updatedAddress == null){
            Gson gson = new Gson();
            String json = getData(activity, Constants.address);
            updatedAddress = gson.fromJson(json, Address.class);
        }

        return updatedAddress;
    }



    public void setUpdatedAddress(Address updatedAddress, MainActivity activity) {
        this.updatedAddress = updatedAddress;
        //Converting object to string..
        Gson gson = new Gson();
        String json = gson.toJson(updatedAddress);
        addData(activity, Constants.address, json);
    }





    public Order getOrder(MainActivity activity) {
        if(this.order == null){
            Gson gson = new Gson();
            String json = getData(activity, Constants.order);
            this.order = gson.fromJson(json, Order.class);
        }
        return order;
    }

    public void setOrder(Order order, MainActivity activity) {
        this.order = order;
        //Converting object to string..
        Gson gson = new Gson();
        try{
            if(!(this.order.getFlatNo() == null)){
                String json = gson.toJson(this.order);
                addData(activity, Constants.order, json);
            }
        }
        catch (Exception e){
            //DO nothing here...JUST IGNORE
        }
    }

    private Order order;





    //=======================================SHARED PREFERENCES======================================================
    /**
     *
     * @param activity
     * @param key
     * @param value
     */
    public void addData(MainActivity activity, String key, String value){
        SharedPreferences.Editor editor = activity.getSharedPreferences(Constants.MY_PREFS_NAME, activity.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }



    public void addData(MainActivity activity, String key, int value){
        SharedPreferences.Editor editor = activity.getSharedPreferences(Constants.MY_PREFS_NAME, activity.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }


    public void clear(MainActivity activity){
        SharedPreferences.Editor editor = activity.getSharedPreferences(Constants.MY_PREFS_NAME, activity.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }


    public String getData(MainActivity activity, String key){
        SharedPreferences prefs = activity.getSharedPreferences(Constants.MY_PREFS_NAME, activity.MODE_PRIVATE);
        String text = prefs.getString(key, null);
        return text;
    }


    public int getIntData(MainActivity activity, String key){
        SharedPreferences prefs = activity.getSharedPreferences(Constants.MY_PREFS_NAME, activity.MODE_PRIVATE);
        int text = prefs.getInt(key, 0);
        return text;
    }





    //=======================================END SHARED PREFERENCES======================================================




    public List<Retailer> getRetailerList() {
        return retailerList;
    }

    public void setRetailerList(List<Retailer> retailerList) {
        this.retailerList = retailerList;
    }


    public Office getOffice() {
        return this.office;
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