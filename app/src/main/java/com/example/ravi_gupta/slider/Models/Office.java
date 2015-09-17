package com.example.ravi_gupta.slider.Models;

import android.util.Log;

import com.strongloop.android.loopback.Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
        //If timings is not provided..
        if(timings == null){
            Log.e(Constants.TAG, "Timings property obtained null for office object");
            return false;
        }

        //Now matching if current time past the store closing time..
        String closedTime = (String)timings.get("closedTime");
        String openTime   = (String)timings.get("openTime");
        closedTime = parseIndianTime(closedTime);
        openTime = parseIndianTime(openTime);
        boolean result = compareTime(parsedDate, closedTime, openTime);
        return result;
    }




    //Compare if currentTime is past closedTime or is earlier than openTime
    private boolean compareTime(String currentTime, String closedTime, String openTime){
        String pattern = "\\s?(\\d\\d?):(\\d\\d)\\s(.{2,2})\\s?";
        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);
        int currentTimeHour = 0, closedTimeHour = 0, openTimeHour = 0;

        Matcher currTimeMatcher = r.matcher(currentTime);
        Matcher closedTimeMatcher = r.matcher(closedTime);
        Matcher openTimeMatcher = r.matcher(openTime);

        if(currTimeMatcher.find() && closedTimeMatcher.find() && openTimeMatcher.find()){
            currentTimeHour = Integer.parseInt( currTimeMatcher.group(1));
            closedTimeHour = Integer.parseInt( closedTimeMatcher.group(1));
            openTimeHour = Integer.parseInt( openTimeMatcher.group(1) );
            if(currentTimeHour >= closedTimeHour){
                //Show office is closed right now closed..
                int currentTimeMin = Integer.parseInt( currTimeMatcher.group(2));
                int closedTimeMin = Integer.parseInt( closedTimeMatcher.group(2));
                if(closedTimeMin >= currentTimeMin){
                    return false;
                }else {
                    return true;
                }

            }else{
                if(openTimeHour > currentTimeHour){
                    //Show office is closed right now closed..
                    return true;
                }else{
                    /**
                     * SHOW OFFICE IS OPEN RIGHT NOW.
                     */
                    return false;
                }
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
                if(pm.equals("AM")){
                    if(hour == 12){
                        revisedTime = 0 + ":" + m.group(2)  + " " + m.group(3);
                        return revisedTime;
                    }
                }
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
