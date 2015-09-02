package com.example.ravi_gupta.slider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.example.ravi_gupta.slider.Database.DatabaseHelper;
import com.example.ravi_gupta.slider.Database.OrderStatusDataBase;
import com.example.ravi_gupta.slider.Location.AppLocationService;
import com.example.ravi_gupta.slider.Models.Constants;
import com.example.ravi_gupta.slider.Models.Customer;
import com.example.ravi_gupta.slider.Models.Office;
import com.example.ravi_gupta.slider.Models.Retailer;
import com.example.ravi_gupta.slider.Repository.CustomerRepository;
import com.example.ravi_gupta.slider.Repository.OfficeRepository;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.strongloop.android.loopback.Container;
import com.strongloop.android.loopback.ContainerRepository;
import com.strongloop.android.loopback.File;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.loopback.callbacks.ObjectCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by robins on 2/9/15.
 */
public class ActivityHelper {

    /**====================CONSTANTS========================*/
    AppLocationService appLocationService;
    String pincode;
    double longitude;
    double latitude;
    Intent mainIntent;
    //String status = "Delivered";
    OrderStatusDataBase orderStatusDataBase;
    boolean internetConnection = false;
    boolean imageDownloaded = false;
    boolean retailerListFetched = false;
    MainActivity activity;
    MyApplication application;

    private Address updatedAddress;
    public Address getUpdatedAddress() {
        return updatedAddress;
    }

    /**=====================================================*/


    /**
     * ActivityHelper Constructor
     * @param activity
     * @param application
     */
    public ActivityHelper(MainActivity activity, MyApplication application){
        this.activity = activity;
        this.application = application;
        

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                checkLogin();
                //Your code to run in GUI thread here
                startHelperActivity();
            }//public void run() {
        });
    }



    private void checkLogin(){
        //Making Server Call
        activity.restAdapter = new RestAdapter(application, Constants.baseURL + "/api");
        //Now fetching the current logged in user ..
        activity.setCustomerRepo(activity.restAdapter.createRepository(CustomerRepository.class));
        orderStatusDataBase = new OrderStatusDataBase(activity);

        activity.getCustomerRepo().findCurrentUser(new ObjectCallback<Customer>() {
            @Override
            public void onSuccess(Customer customer) {
                // Check device for Play Services APK.
                // If check succeeds, proceed with GCM registration.
                activity.registerInstallation(customer);
            }

            public void onError(Throwable t) {
                Log.i(Constants.TAG, "Error fetching data from the server");
                Log.e(Constants.TAG, t.toString());
                activity.registerInstallation(null);
            }
        });

        activity.databaseHelper = new DatabaseHelper(activity);
        activity.databaseHelper.deleteAllPrescription();
    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            appLocationService = new AppLocationService(activity);
            Location location = appLocationService
                    .getLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                //replaceFragment(R.layout.fragment_main,null);
            }
        }
    }



    public boolean haveNetworkConnection() { // Checking internet connection and wifi connection
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


    public String findNetwork(){
        String pincode = "";
        //Checking Pincode lies within area
        appLocationService = new AppLocationService(activity);
        Location gpsLocation = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
        Location networkLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);
        if (gpsLocation != null || networkLocation != null) {
            if(gpsLocation != null) {
                latitude = gpsLocation.getLatitude();
                longitude = gpsLocation.getLongitude();
            }
            else {
                latitude = networkLocation.getLatitude();
                longitude = networkLocation.getLongitude();
            }
            Geocoder geocoder = new Geocoder(activity.getApplicationContext(), Locale.getDefault());
            try {
                List<Address> addressList = geocoder.getFromLocation(
                        latitude, longitude, 1);
                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);

                    try {
                        List<Address> updatedAddressList = geocoder.getFromLocation(
                                address.getLatitude(), address.getLongitude(), 1);
                        if (updatedAddressList != null && updatedAddressList.size() > 0) {
                            updatedAddress = updatedAddressList.get(0);
                            Log.v("address", "Updated Address = " + updatedAddress + "");
                        }
                    }
                    catch (Exception e) {

                    }
                    final Pattern p = Pattern.compile( "(\\d{6})" );
                    final Matcher m = p.matcher(updatedAddress.toString() );
                    if ( m.find() ) {
                        pincode =  m.group(0);
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(address.getPostalCode());
                }
            }catch (IOException e) {
            }
        } else {
            showLocationAlert();
        }
        return pincode;
    }



    public void showLocationAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setMessage("This app requires google location to be enabled");
        alertDialog.setPositiveButton("Setting",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String action = "com.google.android.gms.location.settings.GOOGLE_LOCATION_SETTINGS";
                        Intent settings = new Intent(action);
                        activity.startActivityForResult(settings, 1);
                        activity.startActivity(settings);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }


    /**
     * Keywords
     * Fragment codes 1 = Order Status Fragment
     * Fragment codes 0 = Main Fragment.
     * Fragment codes 2 = No Address found.
     * Fragment codes 3 = No Internet Access.
     *
     * @return
     */
    public void startHelperActivity(){
        internetConnection = haveNetworkConnection();
        pincode = findNetwork();
        if(internetConnection) {
            final RestAdapter adapter = application.getLoopBackAdapter();

            final OfficeRepository officeRepo = adapter.createRepository(OfficeRepository.class);
            officeRepo.SearchOfficePincode(pincode, new ObjectCallback<Office>() {
                @Override
                public void onSuccess(Office officeObj) {
                    if (officeObj == null) {
                        Log.i(Constants.TAG, "We are not providing service in your area.");
                        //We are not providing service in your area..
                        activity.replaceFragment(R.layout.fragment_no_address_found, null);

                    } else {
                        application.setOffice(officeObj);
                        officeRepo.getRetailers(officeObj.getId(), new ListCallback<Retailer>() {
                            @Override
                            public void onSuccess(List<Retailer> retailerArray) {
                                Log.i(Constants.TAG, "Successfully fetched retailer data from the server");
                                retailerListFetched = true;
                                application.setRetailerList(retailerArray);
                                if(imageDownloaded && retailerListFetched){
                                    //Go to main fragment
                                    resolveRoute();
                                }
                            }
                            @Override
                            public void onError(Throwable t) {
                                Log.d(Constants.TAG, "No retailer found in this area");
                            }
                        });

                        //Download all the images..
                        fetchAllImages(adapter);
                    }
                }

                @Override
                public void onError(Throwable t) {
                    Log.e(Constants.TAG, "Error loading office settings from server");
                    //Show no internet connection..
                    activity.replaceFragment(R.layout.fragment_no_internet_connection, null);
                }
            }); //SearchOfficePincode method
        }else{
            //Show no internet connection..
            activity.replaceFragment(R.layout.fragment_no_internet_connection, null);

        }
    }







    public void fetchAllImages(RestAdapter adapter){
        ContainerRepository containerRepo = adapter.createRepository(ContainerRepository.class);
        containerRepo.get(Constants.imageContainer, new ObjectCallback<Container>() {
            @Override
            public void onSuccess(Container container) {
                container.getAllFiles(new ListCallback<File>() {
                    @Override
                    public void onSuccess(List<File> objects) {
                        imageDownloaded = true;
                        //Now saving the images to the application ..

                        String baseURL = Constants.baseURL;
                        List<RequestCreator> requestCreators = new ArrayList<RequestCreator>();
                        for (File file : objects) {
                            Uri imageUri = Uri.parse(baseURL + "/api/containers/" + file.getContainer() + "/download/" + file.getName());
                            RequestCreator requestCreator = Picasso.with(activity).load(imageUri);
                            requestCreators.add(requestCreator);
                        }

                        application.setImageList(requestCreators);

                        //app.setImageList(imageList);
                        if (imageDownloaded && retailerListFetched) {
                            //Go to main fragment
                            //Now resolving the route..
                            resolveRoute();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(Constants.TAG, "Error fetching all the offers images.");
                    }
                });
            }

            @Override
            public void onError(Throwable t) {
                Log.e(Constants.TAG, "Error founding the containers.");
            }
        });
    }


    public void resolveRoute(){
        //On success
        //Getting the value of delivery status..
        String pendingDelivery = orderStatusDataBase.getOrderStatus();
        Log.d(Constants.TAG, "Checking value of pending delivery =  " );
        if (pendingDelivery == "") {
            //Go to main fragment
            activity.replaceFragment(R.layout.fragment_main, null);

        } else {
            //TODO CHECK VALUE FROM SERVER
            //Check from server and get the status of main settings
            Log.d(Constants.TAG, "Delivery status pending..");
            activity.replaceFragment(R.layout.fragment_order_status,null);
        }
    }//resolveRoute

}
