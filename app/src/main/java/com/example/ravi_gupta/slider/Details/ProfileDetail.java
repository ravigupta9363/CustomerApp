package com.example.ravi_gupta.slider.Details;

/**
 * Created by Ravi-Gupta on 7/23/2015.
 */
public class ProfileDetail {
    String name;
    String email;
    String phone;

    public ProfileDetail(){
        super();
    }

    public ProfileDetail(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}
