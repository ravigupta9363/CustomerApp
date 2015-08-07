package com.example.ravi_gupta.slider.Location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ravi-Gupta on 8/5/2015.
 */
public class Pincode {
    private static final String TAG = "LocationAddress";
    static String pincodeLocation;

    public static String getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> addressList = geocoder.getFromLocation(
                            latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        Log.v("locality", address + "");
                        StringBuilder sb = new StringBuilder();
                        /*for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append("\n");
                            Log.v("locality", "Address = "+address.getAddressLine(i) + "");
                        }
                        sb.append(address.getLocality()).append("\n");
                        Log.v("locality",address.getPostalCode()+"");*/
                        sb.append(address.getPostalCode()).append("\n");
                        //sb.append(address.getCountryName());
                        Log.v("pincode","Hello"+sb.toString());
                        pincodeLocation = sb.toString();
                        Log.v("pincode","Hello"+pincodeLocation);
                        result = sb.toString();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable to connect", e);
                } finally {
                    Message message = Message.obtain();
                    //message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        //result = "Latitude: " + latitude + " Longitude: " + longitude +
                        //"\n\nAddress:\n" + result;
                        bundle.putString("address", result);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = //"Latitude: " + latitude + " Longitude: " + longitude +
                                "Tap to refresh your Address";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                   // message.sendToTarget();
                }
            }
        };
        thread.start();
        return pincodeLocation;
    }
}
