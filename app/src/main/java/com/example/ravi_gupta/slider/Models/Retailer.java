package com.example.ravi_gupta.slider.Models;

import android.location.Geocoder;
import android.util.Log;

import com.strongloop.android.loopback.Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ravi-Gupta on 8/12/2015.
 */


public class Retailer extends Model {

    private String name;
    private int fulfillement;
    private String area;
    private Map<String, Object> discount;

    public boolean isReturn() {
        return isReturn;
    }

    public void setIsReturn(boolean isReturn) {
        this.isReturn = isReturn;
    }

    private boolean isReturn;
    private String ownerName;
    private String ownerContact;
    private String address;
    private String contact;
    private String licenceNumber;
    private int totalOrders;
    private int totalSales;
    private Date created;
    private int maxPackingTime;
    private int minPackingTime;
    private int openIssues;
    private String status;
    private Map<String, Object> timings;
    private int pincode;
    private Geocoder mapLocation;
    private String timeFormat = "HH:mm a";


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

        //Now matching the day if today is the closing day for the retailers..
        List<String> closingDayList = (List)timings.get("closed");
        boolean closingDay = compareDay(closingDayList);
        if(closingDay){
            return true;
        }

        //Now matching if current time past the store closing time..
        String closedTime = (String)timings.get("closedTime");
        String openTime   = (String)timings.get("openTime");
        closedTime = parseIndianTime(closedTime);
        openTime = parseIndianTime(openTime);
        boolean result = compareTime(parsedDate, closedTime, openTime);
        return result;
    }




    private String getTodayDate(){
        DateFormat dateFormat = new SimpleDateFormat(timeFormat);
        Date date = new Date();
        String dateParse = dateFormat.format(date);
        return dateParse;
    }





    /**
     *
     * @param dayName
     * @return return true if given day array matches today day
     */
    private boolean compareDay(List<String> dayName){
        if(dayName == null){
            return false;
        }
        //Getting the today day name
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        // full name form of the day
        String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        boolean dayFound = false;
        /**
         * Now starting the matching of day
         */
        for(String day : dayName){
            String pattDay = day.toUpperCase();
            String pattTodayDay = today.toUpperCase();
            if(pattTodayDay.equals(pattDay)){
                dayFound = true;
                break;
            }
        }

        return dayFound;
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




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFulfillment() {
        return fulfillement;
    }

    public void setFulfillment(int fulfillement) {
        this.fulfillement = fulfillement;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Map<String, Object> getDiscount() {
        return discount;
    }

    public void setDiscount(Map<String, Object> discount) {
        this.discount = discount;
    }

    public boolean getReturn() {
        boolean returnAvail = isReturn;
        return returnAvail;
    }

    public void setReturn() {
        this.isReturn = isReturn;
    }
    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerContact() {
        return ownerContact;
    }

    public void setOwnerContact(String ownerContact) {
        this.ownerContact = ownerContact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getMaxPackingTime() {
        return maxPackingTime;
    }

    public void setMaxPackingTime(int maxPackingTime) {
        this.maxPackingTime = maxPackingTime;
    }

    public int getMinPackingTime() {
        return minPackingTime;
    }

    public void setMinPackingTime(int minPackingTime) {
        this.minPackingTime = minPackingTime;
    }

    public int getOpenIssues() {
        return openIssues;
    }

    public void setOpenIssues(int openIssues) {
        this.openIssues = openIssues;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> getTimings() {
        return timings;
    }

    public void setTimings(Map<String, Object> timings) {
        this.timings = timings;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public Geocoder getMapLocation() {
        return mapLocation;
    }

    public void setMapLocation(Geocoder mapLocation) {
        this.mapLocation = mapLocation;
    }

}
