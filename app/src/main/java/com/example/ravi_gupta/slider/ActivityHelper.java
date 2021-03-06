package com.example.ravi_gupta.slider;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.common.collect.ImmutableMap;
import com.strongloop.android.loopback.Container;
import com.strongloop.android.loopback.ContainerRepository;
import com.strongloop.android.loopback.File;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by robins on 2/9/15.
 */
public class ActivityHelper {

    /**====================CONSTANTS========================*/
    AppLocationService appLocationService;
    public String pincode;
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
    public int totalImages;
    public int totalDownloaded;
    private java.io.File localFile;
    private List<java.io.File> fileList = new ArrayList<>();
    /**=====================================================*/


    /**
     * ActivityHelper Constructor
     * @param activity
     * @param application
     */
    public ActivityHelper(MainActivity activity, MyApplication application){
        this.activity = activity;
        this.application = application;
        EventBus.getDefault().register(this);
        EventBus.getDefault().registerSticky(this);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                checkLogin();
                //Your code to run in GUI thread here
                startHelperActivity();
            }//public void run() {
        });

    }

    @Subscriber(tag = Constants.SEND_LAT_LONG)
    private void setLatLong(LatLng latLong) {
        latitude = latLong.latitude;
        longitude = latLong.longitude;
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
        application.setOrder(orderRepository.createObject(ImmutableMap.of("code", 0)), activity);

    }

/*
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data ) {
        if (requestCode == 1) {
            //  TODO: check service again after calling setting activity
            appLocationService = new AppLocationService(activity);
            Location location = appLocationService
                    .getLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                //replaceFragment(R.layout.fragment_main,null);
            }
        }
    }*/


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
        //launchRingDialog(activity);

        //start your activity here
        //internetConnection = activity.haveNetworkConnection();
        runOnUiThread();


    }


    public void launchRingDialog(MainActivity activity) {
        ringProgressDialog = ProgressDialog.show(activity,"", "Please wait...", true);
        ringProgressDialog.setCancelable(true);
        ringProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        ringProgressDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void launchRingDialog(MainActivity activity, String body) {
        ringProgressDialog = ProgressDialog.show(activity,"", body, true);
        ringProgressDialog.setCancelable(true);
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





    private void  runOnUiThread(){

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                appLocationService = new AppLocationService(activity);
                Location gpsLocation = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
                Location networkLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);
                if (gpsLocation != null || networkLocation != null) {
                    if (gpsLocation != null) {
                        latitude = gpsLocation.getLatitude();
                        longitude = gpsLocation.getLongitude();
                    } else {
                        latitude = networkLocation.getLatitude();
                        longitude = networkLocation.getLongitude();
                    }
                }

                Log.v("myAdd",latitude+" "+longitude);
                pincode = activity.findNetwork(latitude, longitude);
                Log.v("myAdd",pincode);
                final RestAdapter adapter = application.getLoopBackAdapter();

                final OfficeRepository officeRepo = adapter.createRepository(OfficeRepository.class);
                officeRepo.SearchOfficePincode(pincode, new ObjectCallback<Office>() {
                    @Override
                    public void onSuccess(Office officeObj) {
                        if (officeObj.getName() == null) {
                            Log.i(Constants.TAG, "We are not providing service in your area.");
                            activity.replaceFragment(R.layout.fragment_try_again, null);
                            try {
                                closeLoadingBar();
                            } catch (Exception e) {
                                Log.e(Constants.TAG, "Loading bar instance is not defined. ActivityHelper");
                            }
                            //We are not providing service in your area....
                            //activity.replaceFragment(R.layout.fragment_no_address_found, null);

                        } else {
                            application.setOffice(officeObj);
                            //Now setting the office id to the order...
                            application.getOrder(activity).setOfficeId((String) officeObj.getId());

                            officeRepo.getRetailers(officeObj.getId(), new ListCallback<Retailer>() {
                                @Override
                                public void onSuccess(List<Retailer> retailerArray) {
                                    Log.i(Constants.TAG, "Successfully fetched retailer data from the server");
                                    retailerListFetched = true;
                                    application.setRetailerList(retailerArray);
                                    if (retailerListFetched) {
                                        //Go to main fragment
                                        resolveRoute();
                                    }
                                }

                                @Override
                                public void onError(Throwable t) {
                                    Log.d(Constants.TAG, "No retailer found in this area");
                                    try {
                                        closeLoadingBar();
                                    } catch (Exception e) {
                                        Log.e(Constants.TAG, "Loading bar instance is not defined. ActivityHelper");
                                    }
                                    //Show no internet connection..
                                    //activity.replaceFragment(R.layout.fragment_try_again, null);
                                    //activity.replaceFragment(R.layout.fragment_main, null);

                                }
                            });

                            //Download all the images..
                            //fetchAllImages(adapter);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(Constants.TAG, t.toString());
                        Log.e(Constants.TAG, "Error loading office settings from server");
                        //closeLoadingBar();
                        try {
                            closeLoadingBar();
                        } catch (Exception e) {
                            Log.e(Constants.TAG, "Loading bar instance is not defined. ActivityHelper");
                        }
                        //Show no internet connection..
                        //activity.replaceFragment(R.layout.fragment_try_again, null);
                        activity.replaceFragment(R.layout.fragment_main, null);
                    }
                }); //SearchOfficePincode method


                try {
                    closeLoadingBar();
                } catch (Exception e) {
                    Log.e(Constants.TAG, "Loading bar instance is not defined. ActivityHelper");
                }
                //closeLoadingBar();
                //Show no internet connection..
                //activity.replaceFragment(R.layout.fragment_main, null);
            }
        });

    }


    public void fetchAllImages(final RestAdapter adapter){

        ContainerRepository containerRepo = adapter.createRepository(ContainerRepository.class);
        containerRepo.get(Constants.imageContainer, new ObjectCallback<Container>() {
            @Override
            public void onSuccess(Container container) {
                container.getAllFiles(new ListCallback<File>() {
                    @Override
                    public void onSuccess(List<File> objects) {
                        totalImages = objects.size();

                        for (File file : objects) {
                            downloadImages(file);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                        Log.e(Constants.TAG, "Error fetching all the offers images.");
                        Log.e(Constants.TAG, t.toString());
                        activity.replaceFragment(R.layout.fragment_try_again, null);


                    }
                });
            }

            @Override
            public void onError(Throwable t) {
                Log.e(Constants.TAG, "Error founding the containers.");
            }
        });
    }




    public void downloadImages(File remoteFile){
        remoteFile.setUrl(Constants.apiUrl + "/containers/" + remoteFile.getContainer() + "/download/" + remoteFile.getName());
        String fileName = activity.getApplicationInfo().dataDir + "/" + remoteFile.getName();
        localFile = new java.io.File( fileName);
        fileList.add(localFile);
        remoteFile.download(localFile, new VoidCallback() {

            @Override
            public void onSuccess() {
                MyApplication app = (MyApplication) activity.getApplication();
                Date date = new Date();
                //long time = date.getTime();
                //String timeString = "offersImage" + timeString.toString() + ".png";
                java.io.File tempFile = fileList.get(totalDownloaded);
                app.getImageFileArray().add(tempFile);
                // localFile contains the content
                Log.d(Constants.TAG, localFile.getAbsolutePath());
                //Increment the value..
                totalDownloaded++;
                if (totalImages == totalDownloaded) {
                    imageDownloaded = true;
                    if (imageDownloaded && retailerListFetched) {
                        //Go to main fragment
                        //Now resolving the route..
                        resolveRoute();
                    }
                }
            }


            @Override
            public void onError(Throwable error) {
                // download failed
                Log.e(Constants.TAG, "Error downloading images files..");
                Log.e(Constants.TAG, error.toString());
                activity.replaceFragment(R.layout.fragment_try_again, null);
            }
        });
    }


    public void resolveRoute(){
        //On success
        try{
            activity.activityCloseLoadingBar();
            //Also try to close the loading bar of the activity..

        }catch (Exception e){
            Log.e(Constants.TAG, "Error activityCloseLoadingBar loading bar instance has not been created. in ActivityHelper resolveroute");
        }
        try{
            closeLoadingBar();
        }
        catch(Exception e){
            Log.e(Constants.TAG, "Error closeLoadingbar instance has not been created. In ActivityHelper fragment");
        }


        //Getting the value of delivery status..
        String pendingDelivery = orderStatusDataBase.getOrderStatus();
        Log.d(Constants.TAG, "Checking value of pending delivery =  ");
        if (pendingDelivery == "") {
            //Go to main fragment
            //TODO DISPLAY FIRST TIME USER SIGNUP/LOGIN SCREEN
            activity.replaceFragment(R.layout.fragment_main, null);

        } else {
            //TODO CHECK VALUE FROM SERVER
            //Check from server and get the status of main settings
            Log.d(Constants.TAG, "Delivery status pending..");
            activity.replaceFragment(R.layout.fragment_order_status, null);
        }
    }//resolveRoute


    /**
     * Parse indian ist time to am and pm time
     * @param time format 12:00 || 18:00 ||08:00
     * @return
     */
    public String parseISTTime(String time){
        String pattern = "\\s?(\\d\\d?):(\\d\\d)\\s?";
        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);
        int hour;
        int min;
        String type = "AM" ;
        // Now create matcher object.
        Matcher m = r.matcher(time);
        if (m.find()) {
            hour = Integer.parseInt( m.group(1));
            min  = Integer.parseInt( m.group(2));

            if(hour >= 12){
                type = "PM";
                if(hour > 12){
                    hour = hour - 12;
                }
            }

            if(min < 10){
                return "" + Integer.toString(hour) + ":" + "0" +  Integer.toString(min) + " " + type;
            }else{
                return "" + Integer.toString(hour) + ":" +   Integer.toString(min) + " " + type;
            }

        }
        else{
            Log.e(Constants.TAG, "Error parsing time");
            return time;
        }
    }





    /**
     * Converts texts to camel case characters..
     * @param text
     * @return
     */
    public static String toCamelCase(String text){
        String[] parts = text.split(" ");
        String camelCaseString = "";
        for (String part : parts){
            if(camelCaseString.equals("")){
                camelCaseString = camelCaseString + toProperCase(part);
            }else{
                camelCaseString = camelCaseString + " " + toProperCase(part);
            }

        }
        return camelCaseString;
    }

    static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }

}
