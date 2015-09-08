package com.example.ravi_gupta.slider;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.util.Log;
import android.view.WindowManager;

import com.example.ravi_gupta.slider.Database.OrderStatusDataBase;
import com.example.ravi_gupta.slider.Location.AppLocationService;
import com.example.ravi_gupta.slider.Models.Constants;
import com.example.ravi_gupta.slider.Models.Customer;
import com.example.ravi_gupta.slider.Models.Office;
import com.example.ravi_gupta.slider.Models.Retailer;
import com.example.ravi_gupta.slider.Repository.CustomerRepository;
import com.example.ravi_gupta.slider.Repository.OfficeRepository;
import com.example.ravi_gupta.slider.Repository.OrderRepository;
import com.google.common.collect.ImmutableMap;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

    public ProgressDialog ringProgressDialog;
    public String result;
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
        createRestAdapter();
        createOrderObject();
        //Making Server Call
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
    }

    private void createRestAdapter(){
        activity.restAdapter = new RestAdapter(application, Constants.apiUrl);
        //Now fetching the current logged in user ..
        activity.setCustomerRepo(activity.restAdapter.createRepository(CustomerRepository.class));
    }


    private void createOrderObject(){
        /**
         * Add the order repository
         */

        OrderRepository orderRepository = activity.restAdapter.createRepository(OrderRepository.class);

        application.setOrder(orderRepository.createObject(ImmutableMap.of("code", 0 )) );

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






    public String findNetwork(){
        String pincode = "";
        //Checking Pincode lies within area
        MyApplication application = (MyApplication)activity.getApplication();
        Address address = new Address(Locale.ENGLISH);
        appLocationService = new AppLocationService(activity);
        Map<String, String> latLong = new HashMap<>();
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
                    address = addressList.get(0);

                    try {
                        List<Address> updatedAddressList = geocoder.getFromLocation(
                                address.getLatitude(), address.getLongitude(), 1);
                        if (updatedAddressList != null && updatedAddressList.size() > 0) {
                            application.setUpdatedAddress(updatedAddressList.get(0));
                            Log.v("address", "Updated Address = " + application.getUpdatedAddress() + "");
                            parseAddress();
                        }
                    }
                    catch (Exception e) {

                    }
                    final Pattern p = Pattern.compile( "(\\d{6})" );
                    final Matcher m = p.matcher(application.getUpdatedAddress().toString() );
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
        try{
            latLong.put("lat",address.getLatitude()+"");
            latLong.put("lng", address.getLongitude() + "");
        }
        catch (Exception e){
            Log.e(Constants.TAG,"Error Fetching latitude of the given address..");
            closeLoadingBar();
            //Show no internet connection..
            activity.replaceFragment(R.layout.fragment_no_internet_connection, null);
        }

        application.getOrder().setGoogleAddr(result);
        application.getOrder().setPincode(Integer.parseInt(pincode));
        application.getOrder().setGeoLocation(latLong);
        return pincode;
    }

    public void parseAddress() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < application.getUpdatedAddress().getMaxAddressLineIndex(); i++) {
            sb.append(application.getUpdatedAddress().getAddressLine(i)).append(" ");
        }

        sb.append(application.getUpdatedAddress().getLocality()).append(" ");
        sb.append(application.getUpdatedAddress().getPostalCode()).append(" ");
        sb.append(application.getUpdatedAddress().getCountryName());
        result = sb.toString();
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
        //this method will be running on UI thread
        /**
         * Launching dialog.
         */
        launchRingDialog(activity);

        internetConnection = activity.haveNetworkConnection();
        runOnUiThread(internetConnection);
    }


    public void launchRingDialog(MainActivity activity) {
        ringProgressDialog = ProgressDialog.show(activity,"", "Please wait...", true);
        ringProgressDialog.setCancelable(true);
        //ringProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        ringProgressDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void launchRingDialog(MainActivity activity, String body) {
        ringProgressDialog = ProgressDialog.show(activity,"", body, true);
        ringProgressDialog.setCancelable(false);
    }


    public void setProgressBarMessage(String body){
        ringProgressDialog.setMessage(body);
    }




    public void closeLoadingBar(){
        ringProgressDialog.dismiss();
    }


    /**
     * Getting the primary email address of the user
     * @param context
     * @return String EmailAddress
     */
    public static String getEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);
        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }

    /**
     * Getting the account details of the google
     * @param accountManager
     * @return
     */
    private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        Log.i(Constants.TAG, account.toString());

        return account;
    }


    private void  runOnUiThread(final boolean internetConnection){

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (internetConnection) {

                    pincode = findNetwork();
                    final RestAdapter adapter = application.getLoopBackAdapter();

                    final OfficeRepository officeRepo = adapter.createRepository(OfficeRepository.class);
                    officeRepo.SearchOfficePincode(pincode, new ObjectCallback<Office>() {
                        @Override
                        public void onSuccess(Office officeObj) {
                            if (officeObj.getName() == null) {
                                Log.i(Constants.TAG, "We are not providing service in your area.");
                                closeLoadingBar();
                                //We are not providing service in your area....
                                activity.replaceFragment(R.layout.fragment_no_address_found, null);

                            } else {
                                application.setOffice(officeObj);

                                //Now setting the office id to the order...
                                application.getOrder().setOfficeId((String) officeObj.getId());


                                officeRepo.getRetailers(officeObj.getId(), new ListCallback<Retailer>() {
                                    @Override
                                    public void onSuccess(List<Retailer> retailerArray) {
                                        Log.i(Constants.TAG, "Successfully fetched retailer data from the server");
                                        retailerListFetched = true;
                                        application.setRetailerList(retailerArray);
                                        if (imageDownloaded && retailerListFetched) {
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
                            closeLoadingBar();
                            //Show no internet connection..
                            activity.replaceFragment(R.layout.fragment_no_internet_connection, null);
                        }
                    }); //SearchOfficePincode method
                } else {
                    closeLoadingBar();
                    //Show no internet connection..
                    activity.replaceFragment(R.layout.fragment_no_internet_connection, null);

                }
            }
        });

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

                        List<RequestCreator> requestCreators = new ArrayList<RequestCreator>();
                        for (File file : objects) {
                            Uri imageUri = Uri.parse(Constants.apiUrl  + "/containers/" + file.getContainer() + "/download/" + file.getName());
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
        closeLoadingBar();
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
