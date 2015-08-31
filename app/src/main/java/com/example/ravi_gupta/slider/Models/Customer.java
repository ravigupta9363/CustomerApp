package com.example.ravi_gupta.slider.Models;

import com.strongloop.android.loopback.User;

import java.util.HashMap;

/**
 * Created by robins on 28/8/15.
 */
public class Customer extends User  {
    private String id;
    private String contactNo;
    private String username;
    private String email;
    private String name;




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
