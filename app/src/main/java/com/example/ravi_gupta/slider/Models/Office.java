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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /***
     *
     * @return true if office is closed and false is office is closed.
     */
    public boolean isClosed(){

        String parsedDate = getTodayDate();

        String closedTime = timings.get("closedTime");
        closedTime = parseIndianTime(closedTime);
        boolean result = compareTime(parsedDate, closedTime);
        return result;
    }

    //Compare if currentTime is past givenTime
    private boolean compareTime(String currentTime, String givenTime){
        String pattern = "\\s?(\\d\\d?):(\\d\\d)\\s(.{2,2})\\s?";
        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        int currentTimeHour = 0, givenTimeHour = 0;

        Matcher currTimeMatcher = r.matcher(currentTime);
        Matcher givenTimeMatcher = r.matcher(givenTime);
        if(currTimeMatcher.find() && givenTimeMatcher.find()){
            currentTimeHour = Integer.parseInt( currTimeMatcher.group(1));
            givenTimeHour = Integer.parseInt( givenTimeMatcher.group(1));
            if(currentTimeHour >= givenTimeHour){
                //Show office closed option
                return true;
            }else{
                return false;
            }
        }else{
            Log.e(Constants.TAG, "Error parsing time in office closing time in page office.java");
            //TODO THROW ERROR HERE INSTEAD OF RETURN STATEMENT
            return false;
        }

    }


    private String parseIndianTime(String time){
        String pattern = "\\s?(\\d\\d?):(\\d\\d)\\s(.{2,2})\\s?";
        String revisedTime = time;
        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);
        int hour = 0;
        // Now create matcher object.
        Matcher m = r.matcher(time);
        if (m.find( )) {
            hour = Integer.parseInt( m.group(1));
            Log.d(Constants.TAG, m.group(3));
            String pm =  m.group(3);
            if(pm.equals("PM")){
                //Get 24 hour time..
                int newTime = hour + 12;
                //Now revised time..
                revisedTime = newTime + ":" + m.group(2)  + " " + m.group(3);
                return revisedTime;
            }else{
             return time;
            }
        }else{
            return time;
        }

    }



    private String getTodayDate(){
        DateFormat dateFormat = new SimpleDateFormat(timeFormat);
        Date date = new Date();
        String dateParse = dateFormat.format(date);
      /*  SimpleDateFormat dateStamp = new SimpleDateFormat(timeFormat, Locale.ENGLISH);
        dateStamp.setTimeZone(TimeZone.getTimeZone("IST"));
        String timeStamp = dateStamp.format(Calendar.getInstance().getTime());

        java.util.Date  date = null;
        try {
            date = dateStamp.parse(timeStamp);
        } catch (ParseException e) {
            Log.d("drugcorner", "Error occured parsing date");
            e.printStackTrace();
        }*/
        return dateParse;
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
