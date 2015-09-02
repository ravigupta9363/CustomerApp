package com.example.ravi_gupta.slider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;

import com.example.ravi_gupta.slider.Database.OrderStatusDataBase;
import com.example.ravi_gupta.slider.Location.AppLocationService;
import com.example.ravi_gupta.slider.Models.Constants;
import com.example.ravi_gupta.slider.Models.Office;
import com.example.ravi_gupta.slider.Models.Retailer;
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
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1500;
    AppLocationService appLocationService;
    //String[] latlong = {"323001","122002","302033","122010","122008","512272"};
    //public String matchPincode;
    String pincode;
    double longitude;
    double latitude;
    public Address updatedAddress;
    Intent mainIntent;
    //String status = "Delivered";
    String TAG = "drugcorner";
    OrderStatusDataBase orderStatusDataBase;
    boolean internetConnection = false;
    boolean imageDownloaded = false;
    boolean retailerListFetched = false;
    SplashActivity that;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        that = this;
        /* Create an Intent that will start the Menu-Activity. */
        mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        orderStatusDataBase = new OrderStatusDataBase(this);

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Your code to run in GUI thread here

            }//public void run() {
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public boolean haveNetworkConnection() { // Checking internet connection and wifi connection
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
        appLocationService = new AppLocationService(this);
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
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                this);
        alertDialog.setMessage("This app requires google location to be enabled");
        alertDialog.setPositiveButton("Setting",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String action = "com.google.android.gms.location.settings.GOOGLE_LOCATION_SETTINGS";
                        Intent settings = new Intent(action);
                        startActivityForResult(settings, 1);
                        startActivity(settings);
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



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            appLocationService = new AppLocationService(this);
            Location location = appLocationService
                    .getLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                //replaceFragment(R.layout.fragment_main,null);
            }
        }
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
    private void startSplashActivity(){
        internetConnection = haveNetworkConnection();
        pincode = findNetwork();
        if(internetConnection) {
            final MyApplication app = (MyApplication) getApplication();
            final RestAdapter adapter = app.getLoopBackAdapter();

            final OfficeRepository officeRepo = adapter.createRepository(OfficeRepository.class);
            officeRepo.SearchOfficePincode(pincode, new ObjectCallback<Office>() {
                @Override
                public void onSuccess(Office officeObj) {
                    if (officeObj == null) {
                        Log.i(TAG, "We are not providing service in your area.");
                        //We are not providing service in your area..
                        mainIntent.putExtra("keyFragment", 2);
                        endActivity();
                    } else {
                        app.setOffice(officeObj);

                        officeRepo.getRetailers(officeObj.getId(), new ListCallback<Retailer>() {
                            @Override
                            public void onSuccess(List<Retailer> retailerArray) {
                                Log.i(TAG, "Successfully fetched retailer data from the server");
                                retailerListFetched = true;
                                app.setRetailerList(retailerArray);
                                if(imageDownloaded && retailerListFetched){
                                    //Go to main fragment
                                    resolveRoute();
                                }
                            }
                            @Override
                            public void onError(Throwable t) {
                                Log.d(TAG, "No retailer found in this area");
                            }
                        });

                        //Download all the images..
                        fetchAllImages(adapter);
                    }
                }

                @Override
                public void onError(Throwable t) {
                    Log.e(TAG, "Error loading office settings from server");
                    //Show no internet connection..
                    mainIntent.putExtra("keyFragment", 3);
                    endActivity();
                }
            }); //SearchOfficePincode method
        }else{
            //Show no internet connection..
            mainIntent.putExtra("keyFragment", 3);
            endActivity();
        }
    }








    public void endActivity(){
        //Start the main activity
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();
    }


    public void fetchAllImages(RestAdapter adapter){
        ContainerRepository containerRepo = adapter.createRepository(ContainerRepository.class);
        containerRepo.get(Constants.imageContainer, new ObjectCallback<Container>() {
            @Override
            public void onSuccess(Container container) {
                container.getAllFiles(new ListCallback<File>() {
                    @Override
                    public void onSuccess(List<File> objects) {
                        GetAllImages getAllImages = new GetAllImages(objects);
                        getAllImages.execute();
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


    public RequestCreator downloadImages(File remoteFile) {
        String baseURL = Constants.baseURL;
        Uri imageUri = Uri.parse(baseURL + "/api/containers/" + remoteFile.getContainer() + "/download/" + remoteFile.getName());
        return Picasso.with(that).load(imageUri);
    }



    private class GetAllImages extends AsyncTask<Void, Void, Void>
    {
        public List<File> files;
        public List<RequestCreator> imageList;
        public GetAllImages(List<File> files){
            this.files = files;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread..
        }

        @Override
        protected Void doInBackground(Void... params) {
            for(File file : files){
                imageList.add(downloadImages(file));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //this method will be running on UI thread
            //Adding the imageFetchvalue to the true..
            Log.d(TAG, "All images downloaded successfully..");
            imageDownloaded = true;
            //Now saving the images to the application ..
            final MyApplication app = (MyApplication) getApplication();
            app.setImageList(imageList);
            if(imageDownloaded && retailerListFetched){
                //Go to main fragment
                //Now resolving the route..
                resolveRoute();
            }
        }

    }



    public void resolveRoute(){
        //On success
        //Getting the value of delivery status..
        String pendingDelivery = orderStatusDataBase.getOrderStatus();
        Log.d(TAG, "Checking value of pending delivery =  " + pendingDelivery);
        if (pendingDelivery == null) {
            //Go to main fragment
            mainIntent.putExtra("keyFragment", 0);
        } else {
            //TODO CHECK VALUE FROM SERVER
            //Check from server and get the status of main settings
            Log.d(TAG, "Delivery status pending..");
            mainIntent.putExtra("keyFragment", 1);
        }

        endActivity();
    }






}