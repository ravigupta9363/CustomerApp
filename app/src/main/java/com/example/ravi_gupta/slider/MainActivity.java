
package com.example.ravi_gupta.slider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ravi_gupta.slider.Adapter.NavDrawerListAdapter;
import com.example.ravi_gupta.slider.Database.DatabaseHelper;
import com.example.ravi_gupta.slider.Database.OrderStatusDataBase;
import com.example.ravi_gupta.slider.Database.ProfileDatabase;
import com.example.ravi_gupta.slider.Details.NavigationDrawerItemDetails;
import com.example.ravi_gupta.slider.Details.PrescriptionDetail;
import com.example.ravi_gupta.slider.Dialog.ImageZoomDialog;
import com.example.ravi_gupta.slider.Dialog.SendPrescriptionDialog;
import com.example.ravi_gupta.slider.Dialog.ServerImageZoomDialog;
import com.example.ravi_gupta.slider.Fragment.AboutUsFragment;
import com.example.ravi_gupta.slider.Fragment.CartFragment;
import com.example.ravi_gupta.slider.Fragment.CartNoOrdersFragment;
import com.example.ravi_gupta.slider.Fragment.ConfirmOrderFragment;
import com.example.ravi_gupta.slider.Fragment.ContactUsFragment;
import com.example.ravi_gupta.slider.Fragment.FAQFragment;
import com.example.ravi_gupta.slider.Fragment.IncomingSmsFragment;
import com.example.ravi_gupta.slider.Fragment.LandmarkFragment;
import com.example.ravi_gupta.slider.Fragment.ListFragment;
import com.example.ravi_gupta.slider.Fragment.MainFragment;
import com.example.ravi_gupta.slider.Fragment.NoAddressFoundFragment;
import com.example.ravi_gupta.slider.Fragment.NoInternetConnectionFragment;
import com.example.ravi_gupta.slider.Fragment.NotificationFragment;
import com.example.ravi_gupta.slider.Fragment.OrderStatusFragment;
import com.example.ravi_gupta.slider.Fragment.OrderStatusShopDetailFragment;
import com.example.ravi_gupta.slider.Fragment.PastOrderFragment;
import com.example.ravi_gupta.slider.Fragment.ProfileEditFragment;
import com.example.ravi_gupta.slider.Fragment.ProfileFragment;
import com.example.ravi_gupta.slider.Fragment.SendOrderFragment;
import com.example.ravi_gupta.slider.Fragment.TermsAndConditionFragment;
import com.example.ravi_gupta.slider.Fragment.TryAgain;
import com.example.ravi_gupta.slider.Fragment.changeLocationFragment;
import com.example.ravi_gupta.slider.Interface.OnFragmentChange;
import com.example.ravi_gupta.slider.Location.AppLocationService;
import com.example.ravi_gupta.slider.Models.Constants;
import com.example.ravi_gupta.slider.Models.Customer;
import com.example.ravi_gupta.slider.Models.Office;
import com.example.ravi_gupta.slider.Models.Retailer;
import com.example.ravi_gupta.slider.Repository.CustomerRepository;
import com.example.ravi_gupta.slider.Repository.OfficeRepository;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.strongloop.android.loopback.LocalInstallation;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends ActionBarActivity implements ListFragment.OnFragmentInteractionListener, OnFragmentChange,
        SendOrderFragment.OnFragmentInteractionListener, changeLocationFragment.OnFragmentInteractionListener,
        MainFragment.OnFragmentInteractionListener, AboutUsFragment.OnFragmentInteractionListener,
        FAQFragment.OnFragmentInteractionListener, ContactUsFragment.OnFragmentInteractionListener,
        NotificationFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
        ProfileEditFragment.OnFragmentInteractionListener, OrderStatusFragment.OnFragmentInteractionListener,
        OrderStatusShopDetailFragment.OnFragmentInteractionListener, PastOrderFragment.OnFragmentInteractionListener,
        CartFragment.OnFragmentInteractionListener, SendPrescriptionDialog.Callback, LandmarkFragment.OnFragmentInteractionListener,
        CartNoOrdersFragment.OnFragmentInteractionListener, NoInternetConnectionFragment.OnFragmentInteractionListener,
        IncomingSmsFragment.OnFragmentInteractionListener, ConfirmOrderFragment.OnFragmentInteractionListener,
        NoAddressFoundFragment.OnFragmentInteractionListener, TermsAndConditionFragment.OnFragmentInteractionListener,
        VerifyingOrderFragment.OnFragmentInteractionListener, TryAgain.OnFragmentInteractionListener, FragmentManager.OnBackStackChangedListener{

    public int updateLocation = 0;
    //public boolean updateUserInfo = false;
    //public boolean updateUserInfoProfileEditFragment = false;
    //public boolean addedToList = false;
    public DrawerLayout mDrawerLayout;
    public ListView mDrawerList;
    public ActionBarDrawerToggle mDrawerToggle;
    //public int FRAGMENT_CODE = 0;
    //private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
    private Uri fileUri;
    private final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private final int GALLERY_IMAGE_ACTIVITY_REQUEST_CODE = 101;
    private final int QUICK_ORDER = 102;
    private final int LOCATION_ALERT = 103;
    public DatabaseHelper databaseHelper;
    public ProfileDatabase profileDatabase;
    public int prescriptionId = 0;
    public int notificationId = 0;
    public TextView tv;
    //public String OTP;
    public boolean enableEditText = true;
    public String actionbarTitle = Constants.appName;
    MainFragment mainFragment;
    AppLocationService appLocationService;
    //private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavigationDrawerItemDetails> navDrawerItems;
    private NavDrawerListAdapter adapter;
    public RestAdapter restAdapter;

    public String tempName;
    public String tempEmail;
    public String tempPhone;
    public boolean invalidEmail = false;
    private static LocalInstallation installation;
    public LinearLayout linearLayoutSplash;
    public OrderStatusDataBase orderStatusDataBase;
    public ProgressDialog mainActivityProgressDialog;
    double latitude;
    double longitude;
    String pincode = "";
    public ActivityHelper getActivityHelper() {
        return activityHelper;
    }

    ActivityHelper activityHelper;


    public void setCustomerRepo(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }


    private CustomerRepository customerRepo;




    /**
     *
     * @return CustomerRepository instance
     */
    public CustomerRepository getCustomerRepo() {
        return customerRepo;
    }


    public static LocalInstallation getInstallation() {
        return installation;
    }

    /*Push Implementation*/
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    GoogleCloudMessaging gcm;
    Context context;
    public Address updatedAddress;
    String regid;
    String deviceId;
    ProgressBar mainProgressBar;
    TextView appName;
    MainActivity that;
    public FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        context = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDrawerLayout = (DrawerLayout) inflater.inflate(R.layout.decor, null); // "null" is important.
        databaseHelper = new DatabaseHelper(this);
        orderStatusDataBase = new OrderStatusDataBase(this);
        databaseHelper.deleteAllPrescription();
        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
        View child = decor.getChildAt(0);
        decor.removeView(child);
        LinearLayout container = (LinearLayout) mDrawerLayout.findViewById(R.id.mContainer); // This is the container we defined just now.
        container.addView(child);
        // Make the drawer replace the first child
        decor.addView(mDrawerLayout);
        getSupportActionBar().setElevation(0);//Removing Shaodow
        //Putting childs in Navigation drawer
        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.sections_title);
        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.sections_icons);
        //mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawerListView);
        navDrawerItems = new ArrayList<>();
        for(int i = 0; i<=9; i++){
            navDrawerItems.add(new NavigationDrawerItemDetails(navMenuTitles[i], navMenuIcons.getResourceId(i, -1)));
        }
        // Recycle the typed array
        navMenuIcons.recycle(); // For Menu Icons
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);
        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.mipmap.dc_menu, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        appName = (TextView)findViewById(R.id.activity_main_textview1);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.fragment_main_fab);
        floatingActionButton.setVisibility(View.GONE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               quickOrder();
                //floatingActionButton.setVisibility(View.GONE);
            }
        });
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Museo300-Regular.otf");
        appName.setTypeface(typeface);
        that = this;
        linearLayoutSplash = (LinearLayout) findViewById(R.id.activity_main_linear_layout);


        final MyApplication app = (MyApplication) getApplication();

        String id = orderStatusDataBase.getOrderStatus();
        if(id.equals("")) {
            String splash = app.getShowSplash(this);
            try {
                if(splash.equals(Constants.splash)){
                    //Reset value of splash to show splash from next time...
                    app.setShowSplash(null, this);
                    //Dont show splash here in this line of code..
                    linearLayoutSplash.setVisibility(View.GONE);
                    //Show loading bar..
                    activityLoadingBar();

                }
            }
            catch (NullPointerException e){
                //Show splash here in this line of code..
                linearLayoutSplash.setVisibility(View.VISIBLE);
            }
        }




        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check device for Play Services APK.
                checkPlayServices();
                activityHelper = new ActivityHelper(that, app);
            }
        }, 200);
    }


    /**
     * TESTING
     * @return
     */
    public String findNetwork(double latitude, double longitude){
        //String pincode = "";
        /*double latitude;
        double longitude;*/
        //Checking Pincode lies within area
        MyApplication application = (MyApplication)getApplication();
        Address address = new Address(Locale.ENGLISH);
        appLocationService = new AppLocationService(this);
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
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> addressList = geocoder.getFromLocation(
                        latitude, longitude, 1);
                if (addressList != null && addressList.size() > 0) {
                    address = addressList.get(0);

                    try {
                        List<Address> updatedAddressList = geocoder.getFromLocation(
                                address.getLatitude(), address.getLongitude(), 1);
                        if (updatedAddressList.size() > 0) {
                            application.setUpdatedAddress(updatedAddressList.get(0), this);
                            Log.v("address", "Updated Address = " + application.getUpdatedAddress(this) + "");
                            //result = parseAddress();
                        }
                        else{

                            //Show try again as address not found
                            this.replaceFragment(R.layout.fragment_try_again, null);
                        }
                    }
                    catch (Exception e) {
                        Log.e(Constants.TAG, "Error getting address info.");
                        throw e;

                    }
                    final Pattern p = Pattern.compile( "(\\d{6})" );
                    final Matcher m = p.matcher(application.getUpdatedAddress(this).toString() );
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
            //Show no internet connection..
            replaceFragment(R.layout.fragment_no_internet_connection, null);
        }

        try{
            application.getOrder(this).setGoogleAddr(parseAddress());
            application.getOrder(this).setPincode(Integer.parseInt(pincode));
            application.getOrder(this).setGeoLocation(latLong);
        }catch (Exception e){
            Log.e(Constants.TAG, "Error fetching pincode from the address.");
            //TODO SHOW ANOTHER FRAGMENT HERE..
            //We are not providing service in your area....
            this.replaceFragment(R.layout.fragment_try_again, null);
        }
        return pincode;
    }


    public String parseAddress() {
        MyApplication application = (MyApplication)getApplication();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < application.getUpdatedAddress(this).getMaxAddressLineIndex(); i++) {
            sb.append(application.getUpdatedAddress(this).getAddressLine(i)).append(" ");
        }

        sb.append(application.getUpdatedAddress(this).getLocality()).append(" ");
        sb.append(application.getUpdatedAddress(this).getPostalCode()).append(" ");
        sb.append(application.getUpdatedAddress(this).getCountryName());
        return sb.toString();
    }


    public void searchOffice(final ObjectCallback<Office> callback){
        final MyApplication app = (MyApplication)getApplication();
        final RestAdapter adapter = app.getLoopBackAdapter();
        final OfficeRepository officeRepo = adapter.createRepository(OfficeRepository.class);

        officeRepo.SearchOfficePincode(pincode, new ObjectCallback<Office>() {
            @Override
            public void onSuccess(Office object) {
                app.setOffice(object);
                callback.onSuccess(object);
            }
            @Override
            public void onError(Throwable t) {
                Log.e(Constants.TAG, t.toString());
                Log.e(Constants.TAG, "Error loading office settings from server");
                try {
                    activityHelper.closeLoadingBar();
                } catch (Exception e) {
                    Log.e(Constants.TAG, "Loading bar instance is not defined. MainActivity");
                }

                //Show no internet connection..
                replaceFragment(R.layout.fragment_try_again, null);
                //Now calling the callback
                callback.onError(t);
            }
        });
    }


    public void searchRetailers(Office officeObj, final ListCallback<Retailer> callback){
        final MyApplication app = (MyApplication)getApplication();
        final RestAdapter adapter = app.getLoopBackAdapter();
        final OfficeRepository officeRepo = adapter.createRepository(OfficeRepository.class);
        officeRepo.getRetailers(officeObj.getId(), new ListCallback<Retailer>() {
            @Override
            public void onSuccess(List<Retailer> retailerArray) {
                Log.i(Constants.TAG, "Successfully fetched retailer data from the server");
                app.setRetailerList(retailerArray);
                callback.onSuccess(retailerArray);
            }

            @Override
            public void onError(Throwable t) {
                Log.d(Constants.TAG, "No retailer found in this area");
                callback.onError(t);
            }
        });
    }


    public void registerInstallation(Customer customer){
        if (checkPlayServices()) {
            if (customer != null) {
                // logged in
                Log.d(Constants.TAG, "User logged in successfully");
                updateRegistration((String) customer.getId());
            } else {
                // anonymous user
                Log.d(Constants.TAG, "User not logged in ");
                updateRegistration("anonymous");
            }
        } else {
            Log.e(Constants.TAG, "No valid Google Play Services APK found.");
        }
    }



    /**
     * Updates the registration for push notifications.
     */
    public void updateRegistration(String userId) {
        gcm = GoogleCloudMessaging.getInstance(this);

        // 1. Grab the shared RestAdapter instance.
        final MyApplication app = (MyApplication) getApplication();
        final RestAdapter adapter = app.getLoopBackAdapter();

        // 2. Create LocalInstallation instance
        final LocalInstallation installation =  new LocalInstallation(context, adapter);

        // 3. Update Installation properties that were not pre-filled
        /*TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        LOOPBACK_APP_ID = mngr.getDeviceId();*/
        // Enter the id of the LoopBack Application
        installation.setAppId(Constants.LOOPBACK_APP_ID);
       /* Log.i(TAG, LOOPBACK_APP_ID);*/
        // Substitute a real id of the user logged in this application
        installation.setUserId(userId);

        // 4. Check if we have a valid GCM registration id
        if (installation.getDeviceToken() != null) {
            // 5a. We have a valid GCM token, all we need to do now
            //     is to save the installation to the server
            saveInstallation(installation);
        } else {
            // 5b. We don't have a valid GCM token. Get one from GCM
            // and save the installation afterwards.
            registerInBackground(installation);
        }
    }



    /**
     * Checks the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public boolean checkPlayServices() {
        final int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(Constants.TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }



    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID in the provided LocalInstallation
     */
    private void registerInBackground(final LocalInstallation installation) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(final Void... params) {
                try {
                    final String regid = gcm.register(Constants.SENDER_ID);
                    installation.setDeviceToken(regid);
                    return "Device registered, registration ID=" + regid;
                } catch (final IOException ex) {
                    Log.e(Constants.TAG, "GCM registration failed.", ex);
                    return "Cannot register with GCM:" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
            }

            @Override
            protected void onPostExecute(final String msg) {

                saveInstallation(installation);
            }
        }.execute(null, null, null);
    }

    /**
     * Saves the Installation to the LoopBack server and reports the result.
     * @param installation_
     */
    void saveInstallation(final LocalInstallation installation_) {
        installation_.save(new VoidCallback() {

            @Override
            public void onSuccess() {
                final Object id = installation_.getId();
                installation = installation_;
                final String msg = "Installation saved with id " + id;
                Log.i(Constants.TAG, msg);

            }

            @Override
            public void onError(final Throwable t) {
                Log.e(Constants.TAG, "Error saving Installation.", t);

            }
        });
    }

    private class SlideMenuClickListener implements //Naviagation menu class
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }


    public void activityLoadingBar() {
        mainActivityProgressDialog = ProgressDialog.show(this, "", "Loading Home...", true);
        mainActivityProgressDialog.setCancelable(false);
        mainActivityProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        mainActivityProgressDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    public void activityCloseLoadingBar(){
        mainActivityProgressDialog.dismiss();
    }


    public void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        final android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        if (!haveNetworkConnection()) {
            position = 99;
        }
        switch (position) {
            case 0:
                Customer user = customerRepo.getCachedCurrentUser();
                Log.i(Constants.TAG, "Getting current id");
                //Log.i(TAG, user.getEmail());
                if(user == null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ProfileEditFragment profileEditFragment = (ProfileEditFragment) getSupportFragmentManager().
                            findFragmentByTag(ProfileEditFragment.TAG);
                    if (profileEditFragment == null) {
                        profileEditFragment = ProfileEditFragment.newInstance();
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("fragment", "DirectHomeFragment");
                    profileEditFragment.setArguments(bundle);
                    ft.replace(R.id.fragment_main_container, profileEditFragment, ProfileEditFragment.TAG).addToBackStack(ProfileEditFragment.TAG);
                    ft.commitAllowingStateLoss();
                }
                else {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_main_container, new ProfileFragment()).addToBackStack(ProfileFragment.TAG)
                            .commitAllowingStateLoss();
                }

                break;
            case 1:
                if (databaseHelper.getPresciptionCount() > 0) {
                    showOpenPastOrderAlert();
                } else {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_main_container, new PastOrderFragment()).addToBackStack(PastOrderFragment.TAG)
                            .commitAllowingStateLoss();
                }
                break;
            case 2:

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_main_container, new NotificationFragment()).addToBackStack(NotificationFragment.TAG)
                        .commitAllowingStateLoss();
                break;
            case 3:

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                OrderStatusFragment orderStatusFragment = (OrderStatusFragment) getSupportFragmentManager().
                        findFragmentByTag(OrderStatusFragment.TAG);
                if (orderStatusFragment == null) {
                    orderStatusFragment = OrderStatusFragment.newInstance();
                }
                Bundle bundle = new Bundle();
                bundle.putString("fragment", "StatusFragment");
                orderStatusFragment.setArguments(bundle);
                ft.replace(R.id.fragment_main_container, orderStatusFragment, OrderStatusFragment.TAG).addToBackStack(null);
                ft.commitAllowingStateLoss();

                break;
            case 4:

                Uri uri = Uri.parse("market://details?id=" + Constants.appPlayStoreLink);// this.getPackageName()
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + Constants.appPlayStoreLink)));
                }
                break;
            case 5:

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = Constants.appShareText;
                shareBody = shareBody + "http://play.google.com/store/apps/details?id=" + Constants.appPlayStoreLink;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Drugcorner");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Via"));
                break;
            case 6:

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_main_container, new ContactUsFragment()).addToBackStack(ContactUsFragment.TAG)
                        .commitAllowingStateLoss();

                break;
            case 7:

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_main_container, new AboutUsFragment()).addToBackStack(AboutUsFragment.TAG)
                        .commitAllowingStateLoss();
                break;
            case 8:

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_main_container, new FAQFragment()).addToBackStack(FAQFragment.TAG)
                        .commitAllowingStateLoss();
                break;

            case 9:

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_main_container, new TermsAndConditionFragment()).addToBackStack(TermsAndConditionFragment.TAG)
                        .commitAllowingStateLoss();

                break;

            case 99:
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                mDrawerLayout.closeDrawer(mDrawerList);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_main_container, new NoInternetConnectionFragment()).addToBackStack(NoInternetConnectionFragment.TAG)
                        .commitAllowingStateLoss();

            default:
                break;
        }

        if (fragment != null) {
            //FragmentManager fragmentManager = getFragmentManager();
            //fragmentManager.beginTransaction()
            //      .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //================================================================

    public void quickOrder() {
        if (isExternalStorageWritable()) {
            Calendar cal = Calendar.getInstance();
            File dir = getPicStorageDir("Drugcorner");
            File imageFile = new File(dir, cal.getTimeInMillis() + ".jpg");
            fileUri = Uri.fromFile(imageFile);
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            if (i.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(i, QUICK_ORDER);
            }

        }
    }

    @Override
    public void takePhoto() {
        if (isExternalStorageWritable()) {
            Calendar cal = Calendar.getInstance();
            File dir = getPicStorageDir("Drugcorner");
            File imageFile = new File(dir, cal.getTimeInMillis() + ".jpg");
            fileUri = Uri.fromFile(imageFile);
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            if (i.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (fileUri != null) {
            outState.putString("cameraImageUri", fileUri.toString());
        }
        if(pincode != null){
            outState.putString("pincode", pincode);
        }
        try {
            outState.putString("latitude", String.valueOf(latitude) );
            outState.putString("longitude", String.valueOf(longitude));
        }catch (Exception e){
            Log.e(Constants.TAG, "Error saving lat and long values in saveInstance mainActivity");
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("cameraImageUri")) {
            fileUri = Uri.parse(savedInstanceState.getString("cameraImageUri"));
        }
        if (savedInstanceState.containsKey("pincode")) {
            pincode = savedInstanceState.getString("pincode");
        }
        try{
            if (savedInstanceState.containsKey("latitude")) {
                latitude = Double.parseDouble(savedInstanceState.getString("latitude"));
            }
            if (savedInstanceState.containsKey("latitude")) {
                longitude = Double.parseDouble(savedInstanceState.getString("longitude"));
            }
        }catch (Exception e){
            Log.e(Constants.TAG, "Null value found for lat and long onRestoreInstance MainActivity");
        }
    }//onRestoreInstanceState


    @Override
    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if(resultCode == RESULT_OK)
                replaceFragment(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE, null);
        }
        else if(requestCode == GALLERY_IMAGE_ACTIVITY_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                fileUri = data.getData();
                replaceFragment(GALLERY_IMAGE_ACTIVITY_REQUEST_CODE, null);
            }
        }
        else if(requestCode == QUICK_ORDER) {
            if(resultCode == RESULT_OK) {
                replaceFragment(QUICK_ORDER, null);
                Log.v("quickOrder", "I am Called");
            }
        }
        else if (requestCode == LOCATION_ALERT) {
            Log.v("GONE","Location has been Detected");
            getActivityHelper().launchRingDialog(this);
            getActivityHelper().startHelperActivity();
        }
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



    @Override
    public void replaceFragment(int id, Object object) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        MyApplication app = (MyApplication)getApplication();
        //ActivityHelper helper = new ActivityHelper(this, app);
        if(!haveNetworkConnection()) {
            id = R.layout.fragment_no_internet_connection;
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        switch (id) {

            case R.layout.fragment_main:
                fragmentMain(ft);
                break;

            case R.id.shopListview:
                fragmentShopListView(ft);
                break;

            case R.id.fragment_main_edittext1:
                fragmentMainEditText1(ft);
                break;

            case R.id.fragment_change_location_button2:
                fragmentChangeLocationButton2(ft);
                break;

            case R.id.fragment_past_order_listview1:
                fragmentPastOrderListView1(ft);
                break;

            case R.id.fragment_main_imagebutton2:
                fragmentMainImageButton2(ft);
                break;

            case R.id.fragment_send_order_button1:
                fragmentSendOrderButton1();
                break;

            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                CaptureImageActivityRequestCode();
                break;

            case QUICK_ORDER:
                cameraQuickOrder(ft);
                break;

            case R.id.prescription_imageview1:
                prescriptionImageView1Fragment(object);
                break;

            case GALLERY_IMAGE_ACTIVITY_REQUEST_CODE:
                GalleryImageActivityRequestCodeFragment();
                break;

            case R.id.nextButton:
                fragmentNextButton(ft);
                break;

            case R.layout.fragment_no_internet_connection:
                noInternetConnectionFragment(ft);
                break;

            case R.id.fragment_profile_edit_button1:
                profileEditButton1Fragment(ft, object);
                break;

            case R.id.fragment_incoming_sms_button1:
                incomingSmsButton1Fragment(ft);
                break;

            case R.id.fragment_incoming_sms_textview5:
                verifyingQuickOrder(ft);
                break;

            case R.id.fragment_send_order_button2:
                sendOrderButton2Fragment(ft);
                break;

            case R.id.fragment_confirm_order_button1:
                confirmOrderButton1Fragment(ft);
                break;

            case R.id.fragment_landmark_button1:
                FragmentLandmarkButton1Fragment(ft);
                break;

            case R.id.past_order_layout_imageview1:
                pastOrderLayoutImageView1Fragment(ft, object);
                break;

            case R.id.past_order_layout_button2:
                pastOrderLayoutButton2Fragment(ft);
                break;

            case R.id.past_order_layout_button1:
                pastOrderLayoutButton1Fragment(ft);
                break;

            case R.layout.fragment_no_address_found:
                noAddressFoundFragment(ft);
                break;

            case R.id.fragment_profile_button1:
                profileButtonFragment(ft);
                break;

            case R.id.fragment_verifying_order_textview1:
                verifyingOrderFragment(ft);
                break;

            case R.id.fragment_order_status_button2:
                orderStatusFragment();
                break;

            case R.id.fragment_past_order_button1:
                addProfile(ft,object);
                break;

            case R.layout.fragment_order_status:
                openOrderStatus(ft,object);
                break;

            case R.layout.fragment_try_again:
                tryAgain(ft);
                break;

            case R.id.fragment_incoming_sms_textview4:
                invalidEmail(ft,object);

        }
    }




    /*==================REPLACE FRAGMENTS METHOD AREA==============================*/
    private void fragmentMain(FragmentTransaction ft){
        MainFragment frag1 = (MainFragment) getSupportFragmentManager().
                findFragmentByTag(MainFragment.TAG);
        if (frag1 == null) {
            frag1 = MainFragment.newInstance();
        }
        ft.replace(R.id.container, frag1, MainFragment.TAG);
        ft.commitAllowingStateLoss();
    }

    private void fragmentShopListView(FragmentTransaction ft){
        Fragment newFragment1 = new SendOrderFragment();
        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(MainFragment.TAG);
        ft.replace(R.id.ListFragment, newFragment1, SendOrderFragment.TAG);
        ft.addToBackStack(SendOrderFragment.TAG); // Ads FirstFragment to the back-stack
        ft.commit();
    }

    private void fragmentMainEditText1(FragmentTransaction ft){
        changeLocationFragment frag2 = (changeLocationFragment) getSupportFragmentManager().
                findFragmentByTag(changeLocationFragment.TAG);
        //ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
        if (frag2 == null) {
            frag2 = changeLocationFragment.newInstance();
        }
        ft.replace(R.id.fragment_main_container, frag2, changeLocationFragment.TAG).addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    private void fragmentChangeLocationButton2(FragmentTransaction ft){
        MainFragment frag3 = (MainFragment) getSupportFragmentManager().
                findFragmentByTag(MainFragment.TAG);
        updateLocation = 3;
        if (frag3 == null) {
            frag3 = MainFragment.newInstance();
        }
        ft.replace(R.id.container, frag3, MainFragment.TAG).addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    private void fragmentPastOrderListView1(FragmentTransaction ft){
        OrderStatusFragment frag4 = (OrderStatusFragment) getSupportFragmentManager().
                findFragmentByTag(OrderStatusFragment.TAG);
        if (frag4 == null) {
            frag4 = OrderStatusFragment.newInstance();
        }
        ft.replace(R.id.fragment_main_container, frag4, OrderStatusFragment.TAG).addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    private void fragmentMainImageButton2(FragmentTransaction ft){
        int prescriptionCount = databaseHelper.getPresciptionCount();
        if (prescriptionCount == 0) {
            Fragment newFragment = new CartNoOrdersFragment();
            ft.replace(R.id.fragment_main_container, newFragment, CartNoOrdersFragment.TAG);
            ft.addToBackStack(CartNoOrdersFragment.TAG); // Ads FirstFragment to the back-stack
            ft.commit();
        } else {
            CartFragment frag5 = (CartFragment) getSupportFragmentManager().
                    findFragmentByTag(CartFragment.TAG);
            if (frag5 == null) {
                frag5 = CartFragment.newInstance();
            }
            ft.replace(R.id.fragment_main_container, frag5, CartFragment.TAG).addToBackStack(null);
            ft.commitAllowingStateLoss();
        }
    }

    private void fragmentSendOrderButton1(){
        SendPrescriptionDialog dialog = new SendPrescriptionDialog();
        dialog.show(getSupportFragmentManager(), SendPrescriptionDialog.TAG);
    }

    private void CaptureImageActivityRequestCode(){
        //Thumbnail is being saved
        Calendar calendar = Calendar.getInstance();
        File dir = getPicStorageDir(Constants.imageStorageDir);
        File imageFile = new File(dir, calendar.getTimeInMillis() + ".jpeg");

        File oldFile = new File(fileUri.getPath());
        Uri thumbnailUri = Uri.fromFile(imageFile);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inTempStorage = new byte[24*1024];
        options.inJustDecodeBounds = false;
        options.inSampleSize=8;
        Bitmap bitmap2 =BitmapFactory.decodeFile(oldFile.getPath(),options);
        Bitmap bitmap = ThumbnailUtils.extractThumbnail(bitmap2, 100, 130);
        try {
            FileOutputStream out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            bitmap.recycle();
            bitmap = null;
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }
        databaseHelper.addPrescription(new PrescriptionDetail(prescriptionId, fileUri, thumbnailUri));
        prescriptionId++;
    }



    private void prescriptionImageView1Fragment(Object object){
        Bundle bundle4 = new Bundle();
        //bundle2.pu("prescription",medicineListAdapter);
        Uri image = (Uri) object;
        bundle4.putParcelable("prescription", image);
        ImageZoomDialog imageZoomDialog = new ImageZoomDialog();
        imageZoomDialog.setArguments(bundle4);
        imageZoomDialog.show(getFragmentManager(), ImageZoomDialog.TAG);
    }



    private void GalleryImageActivityRequestCodeFragment(){
        //Thumbnail is being saved
        String path = getRealPathFromURI(this, fileUri);

        Calendar calendar1 = Calendar.getInstance();
        File dir1 = getPicStorageDir(Constants.imageStorageDir);
        File imageFile1 = new File(dir1, calendar1.getTimeInMillis() + ".jpeg");
        File oldFile1 = new File(path);
        Uri thumbnailUri1 = Uri.fromFile(imageFile1);
        Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(oldFile1.getAbsolutePath()),
                100, 130);
        try {
            FileOutputStream out = new FileOutputStream(imageFile1);
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        databaseHelper.addPrescription(new PrescriptionDetail(prescriptionId, fileUri, thumbnailUri1));
        prescriptionId++;
    }



    private void fragmentNextButton(FragmentTransaction ft){
        Customer customer = getCustomerRepo().getCachedCurrentUser();
        if (customer == null) {
            ProfileEditFragment frag7 = (ProfileEditFragment) getSupportFragmentManager().
                    findFragmentByTag(ProfileEditFragment.TAG);
            if (frag7 == null) {
                frag7 = ProfileEditFragment.newInstance();
            }
            Bundle bundle5 = new Bundle();
            bundle5.putString("fragment", "cartFragment");
            frag7.setArguments(bundle5);
            ft.replace(R.id.fragment_main_container, frag7, ProfileEditFragment.TAG).addToBackStack(null);
            ft.commitAllowingStateLoss();
        } else {
            LandmarkFragment frag7 = (LandmarkFragment) getSupportFragmentManager().
                    findFragmentByTag(LandmarkFragment.TAG);
            if (frag7 == null) {
                frag7 = LandmarkFragment.newInstance();
            }
            ft.replace(R.id.fragment_main_container, frag7, LandmarkFragment.TAG).addToBackStack(null);
            ft.commitAllowingStateLoss();
        }
    }

    private void noInternetConnectionFragment(FragmentTransaction ft){
        NoInternetConnectionFragment frag8 = (NoInternetConnectionFragment) getSupportFragmentManager().
                findFragmentByTag(NoInternetConnectionFragment.TAG);
        if (frag8 == null) {
            frag8 = NoInternetConnectionFragment.newInstance();
        }
        ft.replace(R.id.container, frag8, NoInternetConnectionFragment.TAG);
        ft.commitAllowingStateLoss();
    }

    private void profileEditButton1Fragment(FragmentTransaction ft, Object object) {
        onBackPressed();
        IncomingSmsFragment frag9 = (IncomingSmsFragment) getSupportFragmentManager().
                findFragmentByTag(IncomingSmsFragment.TAG);
        if (frag9 == null) {
            frag9 = IncomingSmsFragment.newInstance();
        }
        String fragment = (String) object;
        Bundle bundle9 = new Bundle();
        bundle9.putString("fragment", fragment);
        frag9.setArguments(bundle9);
        ft.replace(R.id.fragment_main_container, frag9, IncomingSmsFragment.TAG).addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    private void incomingSmsButton1Fragment(FragmentTransaction ft){
        onBackPressed();
        LandmarkFragment frag10 = (LandmarkFragment) getSupportFragmentManager().
                findFragmentByTag(LandmarkFragment.TAG);
        if (frag10 == null) {
            frag10 = LandmarkFragment.newInstance();
        }
        ft.replace(R.id.fragment_main_container, frag10, LandmarkFragment.TAG).addToBackStack(null);
        ft.commitAllowingStateLoss();
    }



    private void sendOrderButton2Fragment(FragmentTransaction ft){
        if (databaseHelper.getPresciptionCount() > 0) {
            showOpenPastOrderAlert();
        } else {
            PastOrderFragment frag11 = (PastOrderFragment) getSupportFragmentManager().
                    findFragmentByTag(PastOrderFragment.TAG);
            if (frag11 == null) {
                frag11 = PastOrderFragment.newInstance();
            }
            Bundle bundle5 = new Bundle();
            bundle5.putString("fragment", "repeatOrder");
            frag11.setArguments(bundle5);
            ft.replace(R.id.fragment_main_container, frag11, PastOrderFragment.TAG).addToBackStack(null);
            ft.commitAllowingStateLoss();
        }
    }


    private void confirmOrderButton1Fragment(FragmentTransaction ft){
        OrderStatusFragment frag12 = (OrderStatusFragment) getSupportFragmentManager().
                findFragmentByTag(OrderStatusFragment.TAG);
        if (frag12 == null) {
            frag12 = OrderStatusFragment.newInstance();
        }
        Bundle bundle7 = new Bundle();
        bundle7.putString("fragment", "HomeFragment");
        frag12.setArguments(bundle7);
        ft.replace(R.id.container, frag12, OrderStatusFragment.TAG).addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    private void pastOrderLayoutImageView1Fragment(FragmentTransaction ft, Object object){
        Bundle bundle8 = new Bundle();
        //bundle2.pu("prescription",medicineListAdapter);
        List<Map<String, String>> mapList = (List<Map<String, String>>) object;
        bundle8.putSerializable("prescription", (Serializable) mapList);
        ServerImageZoomDialog serverImageZoomDialog = new ServerImageZoomDialog();
        serverImageZoomDialog.setArguments(bundle8);
        serverImageZoomDialog.show(getFragmentManager(), ServerImageZoomDialog.TAG);
        Log.v("signin", "image  " + mapList);
    }

    private void FragmentLandmarkButton1Fragment(FragmentTransaction ft){
        VerifyingOrderFragment frag13 = (VerifyingOrderFragment) getSupportFragmentManager().
                findFragmentByTag(VerifyingOrderFragment.TAG);
        if (frag13 == null) {
            frag13 = VerifyingOrderFragment.newInstance();
        }
        ft.replace(R.id.fragment_main_container, frag13, VerifyingOrderFragment.TAG).addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    private void pastOrderLayoutButton2Fragment(FragmentTransaction ft){
        Customer customer = getCustomerRepo().getCachedCurrentUser();
        if (customer == null) {
            ProfileEditFragment frag7 = (ProfileEditFragment) getSupportFragmentManager().
                    findFragmentByTag(ProfileEditFragment.TAG);
            if (frag7 == null) {
                frag7 = ProfileEditFragment.newInstance();
            }
            Bundle bundle6 = new Bundle();
            bundle6.putString("fragment", "pastOrderFragment");
            frag7.setArguments(bundle6);
            ft.replace(R.id.fragment_main_container, frag7, ProfileEditFragment.TAG).addToBackStack(null);
            ft.commitAllowingStateLoss();
        } else {
            LandmarkFragment frag7 = (LandmarkFragment) getSupportFragmentManager().
                    findFragmentByTag(LandmarkFragment.TAG);
            if (frag7 == null) {
                frag7 = LandmarkFragment.newInstance();
            }
            ft.replace(R.id.fragment_main_container, frag7, LandmarkFragment.TAG).addToBackStack(null);
            ft.commitAllowingStateLoss();
        }
    }

    private void pastOrderLayoutButton1Fragment(FragmentTransaction ft){
        OrderStatusFragment frag14 = (OrderStatusFragment) getSupportFragmentManager().
                findFragmentByTag(OrderStatusFragment.TAG);
        if (frag14 == null) {
            frag14 = OrderStatusFragment.newInstance();
        }
        ft.replace(R.id.fragment_main_container, frag14, OrderStatusFragment.TAG).addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    private void noAddressFoundFragment(FragmentTransaction ft){
        NoAddressFoundFragment frag16 = (NoAddressFoundFragment) getSupportFragmentManager().
                findFragmentByTag(NoAddressFoundFragment.TAG);
        if (frag16 == null) {
            frag16 = NoAddressFoundFragment.newInstance();
        }
        ft.replace(R.id.container, frag16, NoAddressFoundFragment.TAG);
        ft.commitAllowingStateLoss();
    }

    private void profileButtonFragment(FragmentTransaction ft){
        ProfileEditFragment frag17 = (ProfileEditFragment) getSupportFragmentManager().
                findFragmentByTag(ProfileEditFragment.TAG);
        if (frag17 == null) {
            frag17 = ProfileEditFragment.newInstance();
        }
        ft.replace(R.id.fragment_main_container, frag17, ProfileEditFragment.TAG).addToBackStack(null);
        Bundle bundle10 = new Bundle();
        bundle10.putString("fragment", "profileFragment");
        frag17.setArguments(bundle10);
        ft.commitAllowingStateLoss();
    }

    private void verifyingOrderFragment(FragmentTransaction ft){
        OrderStatusFragment frag12 = (OrderStatusFragment) getSupportFragmentManager().
                findFragmentByTag(OrderStatusFragment.TAG);
        if (frag12 == null) {
            frag12 = OrderStatusFragment.newInstance();
        }
        Bundle bundle7 = new Bundle();
        bundle7.putString("fragment", "HomeFragment");
        frag12.setArguments(bundle7);
        ft.replace(R.id.container, frag12, OrderStatusFragment.TAG).addToBackStack(null);
        ft.commitAllowingStateLoss();
    }



    private void orderStatusFragment(){
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();

        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    private void invalidEmail(FragmentTransaction ft, Object object){
        onBackPressed();
        ProfileEditFragment profileEditFragment = (ProfileEditFragment) getSupportFragmentManager().
                findFragmentByTag(ProfileEditFragment.TAG);
        if (profileEditFragment == null) {
            profileEditFragment = ProfileEditFragment.newInstance();
        }
        String fragment = (String) object;
        Bundle bundle = new Bundle();
        bundle.putString("fragment", fragment);
        profileEditFragment.setArguments(bundle);
        invalidEmail = true;
        ft.replace(R.id.fragment_main_container, profileEditFragment, ProfileEditFragment.TAG).addToBackStack(ProfileEditFragment.TAG);
        ft.commitAllowingStateLoss();
    }

    private void addProfile(FragmentTransaction ft,Object object) {
        ProfileEditFragment profileEditFragment = (ProfileEditFragment) getSupportFragmentManager().
                findFragmentByTag(ProfileEditFragment.TAG);
        if (profileEditFragment == null) {
            profileEditFragment = ProfileEditFragment.newInstance();
        }
        String fragment = (String) object;
        Bundle bundle = new Bundle();
        bundle.putString("fragment", fragment);
        profileEditFragment.setArguments(bundle);
        ft.replace(R.id.fragment_main_container, profileEditFragment, ProfileEditFragment.TAG).addToBackStack(ProfileEditFragment.TAG);
        ft.commitAllowingStateLoss();
    }

    private void openOrderStatus(FragmentTransaction ft,Object object) {
        OrderStatusFragment frag14 = (OrderStatusFragment) getSupportFragmentManager().
                findFragmentByTag(OrderStatusFragment.TAG);
        if (frag14 == null) {
            frag14 = OrderStatusFragment.newInstance();
        }
        Bundle bundle7 = new Bundle();
        bundle7.putString("fragment", "HomeFragment");
        frag14.setArguments(bundle7);
        ft.replace(R.id.container, frag14, OrderStatusFragment.TAG);
        ft.commitAllowingStateLoss();
    }

    private void verifyingQuickOrder(FragmentTransaction ft) {
        VerifyingOrderFragment frag13 = (VerifyingOrderFragment) getSupportFragmentManager().
                findFragmentByTag(VerifyingOrderFragment.TAG);
        if (frag13 == null) {
            frag13 = VerifyingOrderFragment.newInstance();
        }
        ft.replace(R.id.fragment_main_container, frag13, VerifyingOrderFragment.TAG).addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    private void cameraQuickOrder(FragmentTransaction ft) {
        Customer customer = getCustomerRepo().getCachedCurrentUser();
        if (customer == null) {
            ProfileEditFragment frag7 = (ProfileEditFragment) getSupportFragmentManager().
                    findFragmentByTag(ProfileEditFragment.TAG);
            if (frag7 == null) {
                frag7 = ProfileEditFragment.newInstance();
            }
            Bundle bundle5 = new Bundle();
            bundle5.putString("fragment", "QuickFragment");
            frag7.setArguments(bundle5);
            ft.replace(R.id.fragment_main_container, frag7, ProfileEditFragment.TAG).addToBackStack(null);
            ft.commitAllowingStateLoss();
        } else {
            VerifyingOrderFragment frag13 = (VerifyingOrderFragment) getSupportFragmentManager().
                    findFragmentByTag(VerifyingOrderFragment.TAG);
            if (frag13 == null) {
                frag13 = VerifyingOrderFragment.newInstance();
            }
            ft.replace(R.id.fragment_main_container, frag13, VerifyingOrderFragment.TAG).addToBackStack(null);
            ft.commitAllowingStateLoss();
        }

    }

    private void tryAgain(FragmentTransaction ft) {

        TryAgain frag14 = (TryAgain) getSupportFragmentManager().
                findFragmentByTag(TryAgain.TAG);
        if (frag14 == null) {
            frag14 = TryAgain.newInstance();
        }
        ft.replace(R.id.container, frag14, TryAgain.TAG);
        ft.commitAllowingStateLoss();

    }

    /*=================REPLACE FRAGMENT METHOD AREA ENDS==========================*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.cart);
        MenuItem item2 = menu.findItem(R.id.nextButton);
        MenuItemCompat.setActionView(item, R.layout.shopping_cart);
        MenuItemCompat.setActionView(item2, R.layout.next_button);
        RelativeLayout badgeLayout = (RelativeLayout) MenuItemCompat.getActionView(item);
        tv = (TextView) badgeLayout.findViewById(R.id.shopping_cart_textview1);
        ImageButton ib = (ImageButton) badgeLayout.findViewById(R.id.shoppingCart);
        String cartItems = databaseHelper.getPresciptionCount()+"";
        tv.setText(cartItems);
        Button nextButton = (Button) MenuItemCompat.getActionView(item2);
        item2.setVisible(false);
        item2.setEnabled(false);
        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/OpenSans-Regular.ttf");
        nextButton.setTypeface(typeface);

        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //new AsyncCaller().execute();


    }


    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();
        SendOrderFragment sendOrderFragment = (SendOrderFragment)getSupportFragmentManager().findFragmentByTag(SendOrderFragment.TAG);
        //MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(MainFragment.TAG);
        if (count == 0) {
            super.onBackPressed();
        }
        else {
            if(count == 1 && sendOrderFragment != null && databaseHelper.getPresciptionCount() > 0) {
                showSettingsAlert();
            } else {
                getSupportFragmentManager().popBackStack();

            }
        }

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
                        startActivityForResult(settings, LOCATION_ALERT);
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


    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                this);

        alertDialog.setMessage("Discard Prescription?");
        alertDialog.setPositiveButton("Discard",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getSupportFragmentManager().popBackStack();
                        databaseHelper.deleteAllPrescription();
                        String cartItems = databaseHelper.getPresciptionCount() + "";
                        mainFragment.getCartItems().setText(cartItems);
                        mainFragment.getCartItems().setBackgroundColor(Color.rgb(204, 204, 204));
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


    public void showOpenPastOrderAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                this);

        alertDialog.setMessage("Discard Prescription?");
        alertDialog.setPositiveButton("Discard",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        PastOrderFragment frag = (PastOrderFragment) getSupportFragmentManager().
                                findFragmentByTag(PastOrderFragment.TAG);
                        if (frag == null) {
                            frag = PastOrderFragment.newInstance();
                        }
                        Bundle bundle5 = new Bundle();
                        bundle5.putString("fragment", "repeatOrder");
                        frag.setArguments(bundle5);
                        ft.replace(R.id.fragment_main_container, frag, PastOrderFragment.TAG).addToBackStack(null);
                        ft.commitAllowingStateLoss();
                        databaseHelper.deleteAllPrescription();
                        String cartItems = databaseHelper.getPresciptionCount() + "";
                        mainFragment.getCartItems().setText(cartItems);
                        mainFragment.getCartItems().setBackgroundColor(Color.rgb(204, 204, 204));
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

    public File getPicStorageDir(String dirName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES), dirName);
        file.mkdirs();
        return file;
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        String path = "";
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    // Send an upstream message.
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackStackChanged() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            floatingActionButton.setVisibility(View.GONE);
                Log.v("GONE", "BACK STACK");
        }
        else {
               floatingActionButton.setVisibility(View.VISIBLE);
                Log.v("GONE", "BACK STACK 2");
        }
    }

    public void showFloatingButton() {
            floatingActionButton.setVisibility(View.VISIBLE);
            Log.v("GONE", "BACK STACK 2");
    }

    public void hideFloatingButton() {
           floatingActionButton.setVisibility(View.GONE);
            Log.v("GONE", "BACK STACK 2");
    }
}