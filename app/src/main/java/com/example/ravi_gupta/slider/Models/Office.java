package com.example.ravi_gupta.slider.Models;

import android.util.Log;

import com.strongloop.android.loopback.Model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by robins on 1/9/15.
 */
public class Office extends Model {

    private String id;
    private String name;
    private String address;
    private String OfficeSettingsId;

    //Array of strings..
    private List<String> allowedPincodes;
    private Map<String, String> geoLocation;
    private Map<String, String> timings;

    private String timeFormat = "HH:mm a";

    public Map<String, String> getTimings() {
        return timings;
    }

    public void setTimings(Map<String, String> timings) {
        this.timings = timings;
    }


    public boolean isClosed(){
        SimpleDateFormat format = new SimpleDateFormat(timeFormat, Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("IST"));
        java.util.Date date = null;
        java.util.Date currentDate = getTodayDate();
        try {
            Log.d("drugcorner", "Getting the timings object");
            String closedTime = timings.get("closedTime");
            date = format.parse(closedTime);


        } catch (ParseException e) {
            Log.d("drugcorner", "Error occured parsing date");
            e.printStackTrace();
        }

        boolean value =  date.before(currentDate);
        return value;
    }



    private Date getTodayDate(){
        SimpleDateFormat dateStamp = new SimpleDateFormat(timeFormat, Locale.ENGLISH);
        dateStamp.setTimeZone(TimeZone.getTimeZone("IST"));
        String timeStamp = dateStamp.format(Calendar.getInstance().getTime());

        java.util.Date  date = null;
        try {
            date = dateStamp.parse(timeStamp);
        } catch (ParseException e) {
            Log.d("drugcorner", "Error occured parsing date");
            e.printStackTrace();
        }
        return date;
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

    public List<String> getAllowedPincodes() {
        return allowedPincodes;
    }

    public void setAllowedPincodes(List<String> allowedPincodes) {
        this.allowedPincodes = allowedPincodes;
    }

    public Map<String, String> getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(Map<String, String> geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getOfficeSettingsId() {
        return OfficeSettingsId;
    }

    public void setOfficeSettingsId(String officeSettingsId) {
        OfficeSettingsId = officeSettingsId;
    }

}
