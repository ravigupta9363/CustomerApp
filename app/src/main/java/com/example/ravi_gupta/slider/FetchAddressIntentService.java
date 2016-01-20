package com.example.ravi_gupta.slider;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.example.ravi_gupta.slider.Models.Constants;
import com.google.android.gms.maps.model.LatLng;

import org.simple.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ravi-Gupta on 12/8/2015.
 * In this service current location of user is fetched from the geocoder and send to the fragment
 * by which this service is called
 */
public class FetchAddressIntentService extends IntentService {

    private MainActivity.AddressResultReceiver mReceiver;

    public FetchAddressIntentService() {
        super("FetchAddressIntentServices");

    }

    public FetchAddressIntentService(String name) {
        super(name);
    }

    /**
     * Overridden method onHandleIntent() is used to fetch the location by using geocoding
     * technique
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        mReceiver = new MainActivity.AddressResultReceiver(new Handler());
        String errorMessage = "";
        // Get the location passed to this service through an extra.
        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
        }
        catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = "Service not available";
            Log.e(Constants.TAG, errorMessage, ioException);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = "Invalid Lat Long";
            Log.e(Constants.TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "No Address Found";
                Log.e(Constants.TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        }
        else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();
            LatLng latlong = new LatLng(location.getLatitude(),location.getLongitude());
            EventBus.getDefault().post(latlong, Constants.SEND_LAT_LONG);
            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(Constants.TAG, "Address Found");
            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                    TextUtils.join(" ",
                            addressFragments));
        }
    }

    /**
     * This method is used to deliver result to the fragment which called it
     * @param resultCode
     * @param message
     */
    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
}
