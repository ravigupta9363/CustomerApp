package com.example.ravi_gupta.slider.Models;

import com.strongloop.android.loopback.User;

import java.util.HashMap;

/**
 * Created by robins on 28/8/15.
 */
public class Customer extends User  {

    private String contactNo;
    private String username;
    private String email;
    private String name;
  /*  private String ttl;
    private String userId;
    private String created;
    private HashMap<String, String> user;*/
    private int totalOrders;
    private String area;
    private String password;
    private String address;
    private String lastOrder;
    private int totalIssues;
    private String birthDate;
    private Object deviceType;
    private int cancelledOrders;
    private int totalSales;
    private int returnedOrders;
    private int totalReturnedCost;
    private int minDeliveryTime;
    private int maxDeliveryTime;
    private int totalSavings;
    private int pinCode;
    private String location;



   /* public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public HashMap<String, String> getUser() {
        return user;
    }

    public void setUser(HashMap<String, String> user) {
        this.user = user;
    }


    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }
*/

    public String getContactNo (){
        return this.contactNo;
    }

    public void setContactNo(String contactNo){
        this.contactNo = contactNo;

    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username){
        this.username = username;
    }


    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

}
