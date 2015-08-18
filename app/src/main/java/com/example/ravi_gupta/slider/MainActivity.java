package com.example.ravi_gupta.slider;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ravi_gupta.slider.Adapter.NavDrawerListAdapter;
import com.example.ravi_gupta.slider.Database.DatabaseHelper;
import com.example.ravi_gupta.slider.Database.ProfileDatabase;
import com.example.ravi_gupta.slider.Details.NavigationDrawerItemDetails;
import com.example.ravi_gupta.slider.Details.PrescriptionDetail;
import com.example.ravi_gupta.slider.Details.ProfileDetail;
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
import com.example.ravi_gupta.slider.Fragment.changeLocationFragment;
import com.example.ravi_gupta.slider.Interface.OnFragmentChange;
import com.example.ravi_gupta.slider.Location.AppLocationService;
import com.strongloop.android.loopback.RestAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
        NoAddressFoundFragment.OnFragmentInteractionListener, TermsAndConditionFragment.OnFragmentInteractionListener{

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
    public DatabaseHelper databaseHelper;
    public ProfileDatabase profileDatabase;
    public int prescriptionId = 0;
    public TextView tv;
    //public String OTP;
    public boolean enableEditText = true;
    public String actionbarTitle = "DRUGCORNER";
    MainFragment mainFragment;
    AppLocationService appLocationService;
    //private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavigationDrawerItemDetails> navDrawerItems;
    private NavDrawerListAdapter adapter;
    int mNotificationId1 = 001;
    int mNotificationId2 = 002;
    String[] latlong = {"323001","122002","302033","122010","122008"};
    String matchPincode;
    String pincode;
    public RestAdapter restAdapter;
    public String baseURL = "http://192.168.1.100:3001";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDrawerLayout = (DrawerLayout) inflater.inflate(R.layout.decor, null); // "null" is important.

        //Making Server Call
        restAdapter = new RestAdapter(getApplicationContext(), baseURL+"/api");
        //ModelRepository shopListRepository = restAdapter.createRepository("shopList");
        //Model shopList = shopListRepository.createObject( ImmutableMap.of("shopName", "Gurgaon Pharmacy") );

        //Checking Pincode lies within area
        appLocationService = new AppLocationService(this);
        Location location = appLocationService
                .getLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            String result = null;
            try {
                List<Address> addressList = geocoder.getFromLocation(
                        latitude, longitude, 1);
                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);
                    final Pattern p = Pattern.compile( "(\\d{6})" );
                    final Matcher m = p.matcher( address.toString() );
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

        for(int i = 0; i < latlong.length-1 ; i++) {
            if(latlong[i].equals(pincode)){
                matchPincode = pincode;
                break;
            }
            else {
                matchPincode = null;
            }

        }

        String status = "Delivered";
        //Checking if Network is connected or Serving in area
        if(haveNetworkConnection() && matchPincode != null && status == "Delivered") {
            replaceFragment(R.layout.fragment_main, null);
       }
        else if(haveNetworkConnection() && matchPincode == null) {
            //replaceFragment(R.layout.fragment_);
            replaceFragment(R.layout.fragment_no_address_found, null);
        }
        else {
            //http://stackoverflow.com/questions/5065039/find-point-in-polygon-php
            getSupportActionBar().hide();
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            replaceFragment(R.layout.fragment_no_internet_connection, null);
        }
        databaseHelper = new DatabaseHelper(this);
        profileDatabase = new ProfileDatabase(this);
        databaseHelper.deleteAllPrescription();

        //Opening fragments from notifications
        final android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(getIntent().getAction().equals("OpenNotificationFragment")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_main_container, new NotificationFragment()).addToBackStack(PastOrderFragment.TAG)
                    .commitAllowingStateLoss();
        }
        else if(getIntent().getAction().equals("OpenStatusFragment")){
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
        }



        // HACK: "steal" the first child of decor view
        //Making navigation drawer full screen
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
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.sections_icons);

        //mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawerListView);

        navDrawerItems = new ArrayList<NavigationDrawerItemDetails>();
        for(int i = 0; i<=9; i++){
            navDrawerItems.add(new NavigationDrawerItemDetails(navMenuTitles[i], navMenuIcons.getResourceId(i, -1)));
        }
        /*navDrawerItems.add(new NavigationDrawerItemDetails(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavigationDrawerItemDetails(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavigationDrawerItemDetails(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavigationDrawerItemDetails(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        navDrawerItems.add(new NavigationDrawerItemDetails(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new NavigationDrawerItemDetails(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        navDrawerItems.add(new NavigationDrawerItemDetails(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
        navDrawerItems.add(new NavigationDrawerItemDetails(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
        navDrawerItems.add(new NavigationDrawerItemDetails(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));*/

        // Recycle the typed array
        navMenuIcons.recycle(); // For Menu Icons

        /*SpannableString s = new SpannableString(actionbarTitle);
        s.setSpan(new TypefaceSpan(this, "OpenSans-Regular.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.rgb(51, 51, 51)), 0, s.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        s.setSpan(new RelativeSizeSpan(0.9f), 0,10, 0);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(s);
        mTitle = s.toString();*///Setting Custom fonts in Action Bar


        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        //getSupportActionBar().setTitle(Html.fromHtml("<font color='#999999'>DrugCorner </font>"));

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

        if (savedInstanceState == null) {
        }
    }

    public boolean haveNetworkConnection() { // Checking internet connection and wifi connection
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
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


    private class SlideMenuClickListener implements //Naviagation menu class
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    public void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        final android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        if (!haveNetworkConnection()) {
            position = 99;
        }
            switch (position) {
                case 0:
               /* new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {*/
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_main_container, new ProfileFragment()).addToBackStack(ProfileFragment.TAG)
                            .commitAllowingStateLoss();
                    //  }
                    // },250);
                    //mDrawerLayout.closeDrawer(mDrawerList);
                    break;
                case 1:
                    //mDrawerLayout.closeDrawer(mDrawerList);
                    if (databaseHelper.getPresciptionCount() > 0) {
                        showOpenPastOrderAlert();
                    } else {
                   /* new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {*/
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_main_container, new PastOrderFragment()).addToBackStack(PastOrderFragment.TAG)
                                .commitAllowingStateLoss();
                     /*   }
                    }, 250);*/
                    }
                    break;
                case 2:
               /* mDrawerLayout.closeDrawer(mDrawerList);
               new Handler().postDelayed(new Runnable() {
               @Override
                public void run() {*/
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_main_container, new NotificationFragment()).addToBackStack(NotificationFragment.TAG)
                            .commitAllowingStateLoss();
              /* }
               },250);*/
                    break;
                case 3:
                    /*mDrawerLayout.closeDrawer(mDrawerList);
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {*/

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
                      /* }
                   },250);*/
                    break;
                case 4:
               /* mDrawerLayout.closeDrawer(mDrawerList);*/
                    Uri uri = Uri.parse("market://details?id=" + "com.cubeactive.qnotelistfree");// this.getPackageName()
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + "com.cubeactive.qnotelistfree")));
                    }
                    break;
                case 5:
                    //mDrawerLayout.closeDrawer(mDrawerList);
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "Here is the share content body";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Drugcorner");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share Via"));
                    break;
                case 6:
                /*mDrawerLayout.closeDrawer(mDrawerList);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {*/
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_main_container, new ContactUsFragment()).addToBackStack(ContactUsFragment.TAG)
                            .commitAllowingStateLoss();
                   /* }
                }, 250);*/
                    break;
                case 7:
                 /*mDrawerLayout.closeDrawer(mDrawerList);
                  new Handler().postDelayed(new Runnable() {
                      @Override
                      public void run() {*/
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_main_container, new AboutUsFragment()).addToBackStack(AboutUsFragment.TAG)
                            .commitAllowingStateLoss();

                     /* }
                  },250);*/
                    break;
                case 8:
                /*mDrawerLayout.closeDrawer(mDrawerList);
               new Handler().postDelayed(new Runnable() {
                   @Override
                    public void run() {*/
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_main_container, new FAQFragment()).addToBackStack(FAQFragment.TAG)
                            .commitAllowingStateLoss();
                  /* }
               },250);*/
                    break;

                case 9:
                /*mDrawerLayout.closeDrawer(mDrawerList);
               new Handler().postDelayed(new Runnable() {
                   @Override
                    public void run() {*/
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_main_container, new TermsAndConditionFragment()).addToBackStack(TermsAndConditionFragment.TAG)
                            .commitAllowingStateLoss();
                  /* }
               },250);*/
                    break;

                case 99:
                /*mDrawerLayout.closeDrawer(mDrawerList);
               new Handler().postDelayed(new Runnable() {
                   @Override
                    public void run() {*/
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

    @Override
    public void takePhoto() {
        if (isExternalStorageWritable()) {
            Calendar cal = Calendar.getInstance();
            File dir = getPicStorageDir("prescription_images");
            File imageFile = new File(dir, cal.getTimeInMillis() + ".jpg");
            fileUri = Uri.fromFile(imageFile);
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

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
                replaceFragment(GALLERY_IMAGE_ACTIVITY_REQUEST_CODE,null);
            }
        }
        else if (requestCode == 1) {
            appLocationService = new AppLocationService(this);
            Location location = appLocationService
                    .getLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                replaceFragment(R.layout.fragment_main,null);
            }
        }
    }

    @Override
    public void replaceFragment(int id, Object object) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(!haveNetworkConnection()) {
            id = R.layout.fragment_no_internet_connection;
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
            switch (id) {

                case R.layout.fragment_main:
                    MainFragment frag1 = (MainFragment) getSupportFragmentManager().
                            findFragmentByTag(MainFragment.TAG);
                    if (frag1 == null) {
                        frag1 = MainFragment.newInstance();
                    }
                    ft.replace(R.id.container, frag1, MainFragment.TAG);
                    ft.commitAllowingStateLoss();
                    break;

                case R.id.shopListview:
                    Fragment newFragment1 = new SendOrderFragment();
                    mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(MainFragment.TAG);
                    ft.replace(R.id.ListFragment, newFragment1, SendOrderFragment.TAG);
                    ft.addToBackStack(SendOrderFragment.TAG); // Ads FirstFragment to the back-stack
                    ft.commit();
                    break;

                case R.id.fragment_main_edittext1:
                    changeLocationFragment frag2 = (changeLocationFragment) getSupportFragmentManager().
                            findFragmentByTag(changeLocationFragment.TAG);
                    //ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
                    if (frag2 == null) {
                        frag2 = changeLocationFragment.newInstance();
                    }
                    ft.replace(R.id.fragment_main_container, frag2, changeLocationFragment.TAG).addToBackStack(null);
                    ft.commitAllowingStateLoss();
                    break;

                case R.id.fragment_change_location_button2:
                    MainFragment frag3 = (MainFragment) getSupportFragmentManager().
                            findFragmentByTag(MainFragment.TAG);
                    updateLocation = 3;
                    if (frag3 == null) {
                        frag3 = MainFragment.newInstance();
                    }
                    ft.replace(R.id.container, frag3, MainFragment.TAG).addToBackStack(null);
                    ft.commitAllowingStateLoss();
                    break;

           /* case R.id.fragment_change_location_edittext:
                GeoSearchResult result = (GeoSearchResult)object;
               // Log.v("Result", "is equals to " + result.getAddress());
                Bundle bundle = new Bundle();
                bundle.putString("newAddress", result.getAddress().toString());
                updateLocation = 1;
                Fragment newFragment2 = new MainFragment();
                ft.replace(R.id.container, newFragment2);
                newFragment2.setArguments(bundle);
                ft.addToBackStack(null);
                ft.commit();
                break;*/

          /*  case R.id.addressListView :
                AddressDetails addressDetails = (AddressDetails)object;
                //
                Bundle bundle2 = new Bundle();
                bundle2.putString("newAddress1", addressDetails.address1.toString());
                bundle2.putString("newAddress2", addressDetails.address2.toString());
                Log.v("Result", "is equals to " + addressDetails.address1.toString());
                updateLocation = 2;
                Fragment newFragment3 = new MainFragment();
                ft.replace(R.id.container, newFragment3);
                newFragment3.setArguments(bundle2);
                ft.addToBackStack(null);
                ft.commit();
                break;*/

                case R.id.fragment_past_order_listview1:
                    OrderStatusFragment frag4 = (OrderStatusFragment) getSupportFragmentManager().
                            findFragmentByTag(OrderStatusFragment.TAG);
                    if (frag4 == null) {
                        frag4 = OrderStatusFragment.newInstance();
                    }
                    ft.replace(R.id.fragment_main_container, frag4, OrderStatusFragment.TAG).addToBackStack(null);
                    ft.commitAllowingStateLoss();
                    break;

                case R.id.fragment_main_imagebutton2:
                    if (databaseHelper.getPresciptionCount() == 0) {
                        CartNoOrdersFragment frag5 = (CartNoOrdersFragment) getSupportFragmentManager().
                                findFragmentByTag(CartNoOrdersFragment.TAG);
                        if (frag5 == null) {
                            frag5 = CartNoOrdersFragment.newInstance();
                        }
                        ft.replace(R.id.fragment_main_container, frag5, CartNoOrdersFragment.TAG).addToBackStack(null);
                        ft.commitAllowingStateLoss();
                    } else {
                        CartFragment frag5 = (CartFragment) getSupportFragmentManager().
                                findFragmentByTag(CartFragment.TAG);
                        if (frag5 == null) {
                            frag5 = CartFragment.newInstance();
                        }
                        ft.replace(R.id.fragment_main_container, frag5, CartFragment.TAG).addToBackStack(null);
                        ft.commitAllowingStateLoss();
                    }
                    break;

                case R.id.fragment_send_order_button1:
                    SendPrescriptionDialog dialog = new SendPrescriptionDialog();
                    dialog.show(getSupportFragmentManager(), SendPrescriptionDialog.TAG);
                    break;

                case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                    CartFragment frag6 = (CartFragment) getSupportFragmentManager().
                            findFragmentByTag(CartFragment.TAG);
                    //Thumbnail is being saved
                    Calendar calendar = Calendar.getInstance();
                    File dir = getPicStorageDir("prescription_thumbnails");
                    File imageFile = new File(dir, calendar.getTimeInMillis() + ".jpeg");

                    File oldFile = new File(fileUri.getPath());
                    Uri thumbnailUri = Uri.fromFile(imageFile);
                    Bitmap bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(oldFile.getPath()),
                            100, 130);
                    try {
                        FileOutputStream out = new FileOutputStream(imageFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    databaseHelper.addPrescription(new PrescriptionDetail(prescriptionId, fileUri, thumbnailUri));
                    prescriptionId++;
                    String cartItems = databaseHelper.getPresciptionCount() + "";
                    mainFragment.cartItems.setText(cartItems);
                    mainFragment.cartItems.setBackgroundColor(Color.rgb(242, 121, 53));
                    break;

                case R.id.prescription_imageview1:
                    Bundle bundle4 = new Bundle();
                    //bundle2.pu("prescription",medicineListAdapter);
                    Uri image = (Uri) object;
                    bundle4.putParcelable("prescription", image);
                    ImageZoomDialog imageZoomDialog = new ImageZoomDialog();
                    imageZoomDialog.setArguments(bundle4);
                    imageZoomDialog.show(getFragmentManager(), ImageZoomDialog.TAG);
                    break;

                case GALLERY_IMAGE_ACTIVITY_REQUEST_CODE:
                    //Thumbnail is being saved
                    String path = getRealPathFromURI(this, fileUri);

                    Calendar calendar1 = Calendar.getInstance();
                    File dir1 = getPicStorageDir("prescription_thumbnails");
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
                    String cartItem = databaseHelper.getPresciptionCount() + "";
                    mainFragment.cartItems.setText(cartItem);
                    mainFragment.cartItems.setBackgroundColor(Color.rgb(242, 121, 53));
                    break;

                case R.id.nextButton:
                    ProfileDetail profileDetail = profileDatabase.getProfile();
                    if (profileDetail.getPhone() == null) {
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
                        IncomingSmsFragment frag7 = (IncomingSmsFragment) getSupportFragmentManager().
                                findFragmentByTag(IncomingSmsFragment.TAG);
                        if (frag7 == null) {
                            frag7 = IncomingSmsFragment.newInstance();
                        }
                        ft.replace(R.id.fragment_main_container, frag7, IncomingSmsFragment.TAG).addToBackStack(null);
                        ft.commitAllowingStateLoss();
                    }
                    break;

                case R.layout.fragment_no_internet_connection:
                    NoInternetConnectionFragment frag8 = (NoInternetConnectionFragment) getSupportFragmentManager().
                            findFragmentByTag(NoInternetConnectionFragment.TAG);
                    if (frag8 == null) {
                        frag8 = NoInternetConnectionFragment.newInstance();
                    }
                    ft.replace(R.id.container, frag8, NoInternetConnectionFragment.TAG);
                    ft.commitAllowingStateLoss();
                    break;

                case R.id.fragment_profile_edit_button1:
                    IncomingSmsFragment frag9 = (IncomingSmsFragment) getSupportFragmentManager().
                            findFragmentByTag(IncomingSmsFragment.TAG);
                    if (frag9 == null) {
                        frag9 = IncomingSmsFragment.newInstance();
                    }
                    ft.replace(R.id.fragment_main_container, frag9, IncomingSmsFragment.TAG).addToBackStack(null);
                    ft.commitAllowingStateLoss();
                    break;

                case R.id.fragment_incoming_sms_button1:
                    LandmarkFragment frag10 = (LandmarkFragment) getSupportFragmentManager().
                            findFragmentByTag(LandmarkFragment.TAG);
                    if (frag10 == null) {
                        frag10 = LandmarkFragment.newInstance();
                    }
                    ft.replace(R.id.fragment_main_container, frag10, LandmarkFragment.TAG).addToBackStack(null);
                    ft.commitAllowingStateLoss();
                    break;

                case R.id.fragment_send_order_button2:
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
                    break;

                case R.id.fragment_confirm_order_button1:
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
                    break;

                case R.id.fragment_landmark_button1:
                    ConfirmOrderFragment frag13 = (ConfirmOrderFragment) getSupportFragmentManager().
                            findFragmentByTag(ConfirmOrderFragment.TAG);
                    if (frag13 == null) {
                        frag13 = ConfirmOrderFragment.newInstance();
                    }
                    ft.replace(R.id.container, frag13, ConfirmOrderFragment.TAG);
                    ft.commitAllowingStateLoss();
                    break;

                case R.id.past_order_layout_imageview1:
                Bundle bundle8 = new Bundle();
                //bundle2.pu("prescription",medicineListAdapter);
                List<Map<String, String>> mapList = (List<Map<String, String>>) object;
                bundle8.putSerializable("prescription", (Serializable) mapList);
                ServerImageZoomDialog serverImageZoomDialog = new ServerImageZoomDialog();
                serverImageZoomDialog.setArguments(bundle8);
                serverImageZoomDialog.show(getFragmentManager(), ServerImageZoomDialog.TAG);
                Log.v("signin", "image  " + mapList);
                    break;

                case R.id.past_order_layout_button2:
                    ProfileDetail profileDetail2 = profileDatabase.getProfile();
                    if (profileDetail2.getPhone() == null) {
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
                        IncomingSmsFragment frag7 = (IncomingSmsFragment) getSupportFragmentManager().
                                findFragmentByTag(IncomingSmsFragment.TAG);
                        if (frag7 == null) {
                            frag7 = IncomingSmsFragment.newInstance();
                        }
                        ft.replace(R.id.fragment_main_container, frag7, IncomingSmsFragment.TAG).addToBackStack(null);
                        ft.commitAllowingStateLoss();
                    }
                    break;

                case R.id.past_order_layout_button1:
                    OrderStatusFragment frag14 = (OrderStatusFragment) getSupportFragmentManager().
                            findFragmentByTag(OrderStatusFragment.TAG);
                    if (frag14 == null) {
                        frag14 = OrderStatusFragment.newInstance();
                    }
                    ft.replace(R.id.fragment_main_container, frag14, OrderStatusFragment.TAG).addToBackStack(null);
                    ft.commitAllowingStateLoss();
                    break;

               /* case R.id.fragment_confirm_order_button2:
                    MainFragment frag15 = (MainFragment) getSupportFragmentManager().
                            findFragmentByTag(MainFragment.TAG);
                    if (frag15 == null) {
                        frag15 = MainFragment.newInstance();
                    }
                    ft.replace(R.id.container, frag15, MainFragment.TAG).addToBackStack(null);
                    ft.commitAllowingStateLoss();
                    break;*/

                case R.layout.fragment_no_address_found:
                    NoAddressFoundFragment frag16 = (NoAddressFoundFragment) getSupportFragmentManager().
                            findFragmentByTag(NoAddressFoundFragment.TAG);
                    if (frag16 == null) {
                        frag16 = NoAddressFoundFragment.newInstance();
                    }
                    ft.replace(R.id.container, frag16, NoAddressFoundFragment.TAG);
                    ft.commitAllowingStateLoss();
                    break;
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/OpenSans-Regular.ttf");
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
        nextButton.setTypeface(typeface);

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //replaceFragment(R.id.shoppingCart, null);
            }
        });

       /* nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("next", "Clicked");
                replaceFragment(R.id.nextButton, null);
            }
        });*/

        return super.onCreateOptionsMenu(menu);
        //return true;
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
        new AsyncCaller().execute();

    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //this method will be running on UI thread
        }

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
                        mainFragment.cartItems.setText(cartItems);
                        mainFragment.cartItems.setBackgroundColor(Color.rgb(204,204,204));
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
                        mainFragment.cartItems.setText(cartItems);
                        mainFragment.cartItems.setBackgroundColor(Color.rgb(204, 204, 204));
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
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void showImageNotification() {

        Drawable d = getResources().getDrawable(R.drawable.pills1);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.dc_feedback)
                        .setAutoCancel(true)
                        .setContentTitle("Drug Corner")
                        .setContentText("Get 10% Off on Apollo Pharmacy and get suprise gift with every order");

        NotificationCompat.BigPictureStyle bigPicStyle = new NotificationCompat.BigPictureStyle();
        bigPicStyle.bigPicture(bitmap);
        bigPicStyle.setBigContentTitle("Drug Corner");
        bigPicStyle.setSummaryText("Get 10% Off on Apollo Pharmacy and get suprise gift with every order");
        mBuilder.setStyle(bigPicStyle);
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setAction("OpenNotificationFragment");
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId1, mBuilder.build());
    }


    public void showStatusNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.dc_feedback)
                        .setAutoCancel(true)
                        .setContentTitle("Order Id DC256649")
                        .setContentText("Your Order has been cancelled as per your request");

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setAction("OpenStatusFragment");

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId2, mBuilder.build());
    }
}
