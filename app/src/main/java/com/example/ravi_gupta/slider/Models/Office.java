package com.example.ravi_gupta.slider.Models;

import com.strongloop.android.loopback.Model;

/**
 * Created by robins on 1/9/15.
 */
public class Office extends Model {

    private String id;
    private String name;
    private String address;
    //Array of strings..
    private Object allowedPincodes;
    private Object geoLocation;
    private String OfficeSettingsId;
    private Object timings;


    public Object getTimings() {
        return timings;
    }

    public void setTimings(Object timings) {
        this.timings = timings;
    }


    public boolean isClosed(){
        /*Needs to be implemented*/
        return false;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getAllowedPincodes() {
        return allowedPincodes;
    }

    public void setAllowedPincodes(Object allowedPincodes) {
        this.allowedPincodes = allowedPincodes;
    }

    public Object getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(Object geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getOfficeSettingsId() {
        return OfficeSettingsId;
    }

    public void setOfficeSettingsId(String officeSettingsId) {
        OfficeSettingsId = officeSettingsId;
    }

}
