package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ravi_gupta.slider.Adapter.ViewPagerAdapter;
import com.example.ravi_gupta.slider.Database.DatabaseHelper;
import com.example.ravi_gupta.slider.Location.AppLocationService;
import com.example.ravi_gupta.slider.Location.LocationAddress;
import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.R;
import com.example.ravi_gupta.slider.ViewPager.ViewPagerCustomDuration;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 * FFFEFC
 */
public class MainFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ViewPagerCustomDuration viewPager;
    PagerAdapter pagerAdapter;
    int[] sliderItems;
    Timer timer;
    String newAddress;
    String address1;
    String address2;
    int page = 1;
    AppLocationService appLocationService;
    EditText disabledocationEditText;
    ImageButton menuButton;
    ImageButton cartButton;
    TextView toolbarTitle;
    public TextView cartItems;
    boolean isItemClickable;
    MainActivity mainActivity;
    String cartNumber;
    public static String TAG = "MainFragment";
    DatabaseHelper databaseHelper;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
        sliderItems = new int[]{R.drawable.small_slider3,R.drawable.small_slider1, R.drawable.small_slider2, R.drawable.small_slider3, R.drawable.small_slider1};
        appLocationService = new AppLocationService(getActivity());
        Location gpsLocation = appLocationService
                .getLocation(LocationManager.GPS_PROVIDER);
        if (gpsLocation != null) {
            double latitude = gpsLocation.getLatitude();
            double longitude = gpsLocation.getLongitude();
            // String result = "Latitude: " + gpsLocation.getLatitude() +
            //    " Longitude: " + gpsLocation.getLongitude();
    //fragment_change_location_edittext.setText(result);
        } else {
            showSettingsAlert();
        }

        Location location = appLocationService
                .getLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LocationAddress locationAddress = new LocationAddress();
            locationAddress.getAddressFromLocation(latitude, longitude,
                    getActivity().getApplicationContext(), new GeocoderHandler());
        } else {
            showSettingsAlert();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);

        Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface typeface3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Regular.ttf");

        Drawable drawable = getResources().getDrawable(R.mipmap.dc_location);
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * 0.7),
                (int) (drawable.getIntrinsicHeight() * 0.7));
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 1f, 1f);


        viewPager = (ViewPagerCustomDuration) rootview.findViewById(R.id.viewPager);
        disabledocationEditText = (EditText) rootview.findViewById(R.id.fragment_main_edittext1);
        menuButton = (ImageButton) rootview.findViewById(R.id.fragment_main_imagebutton1);
        cartButton = (ImageButton) rootview.findViewById(R.id.fragment_main_imagebutton2);
        cartItems = (TextView)rootview.findViewById(R.id.fragment_main_textview2);
        toolbarTitle = (TextView) rootview.findViewById(R.id.fragment_main_textview1);
        pagerAdapter = new ViewPagerAdapter(getActivity(), sliderItems, viewPager);

        viewPager.setPadding(40, 0, 40, 0);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(10);
        //http://stackoverflow.com/questions/7395655/set-default-page-for-viewpager-in-android
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1, false);
        viewPager.setScrollDurationFactor(3);
        viewPager.setOffscreenPageLimit(3);
        pageSwitcher(4);

        disabledocationEditText.setCompoundDrawables(sd.getDrawable(), null, null, null);

        toolbarTitle.setTypeface(typeface2);
        String cartItem = databaseHelper.getPresciptionCount()+"";

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.mDrawerLayout.openDrawer(mainActivity.mDrawerList);
            }
        });

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.replaceFragment(R.id.fragment_main_imagebutton2,null);
            }
        });

        disabledocationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mainActivity.replaceFragment(R.id.fragment_main_edittext1, null);
               // Log.v("Result","Called");
                if(mainActivity.enableEditText == true)
                showLocationAlert();
            }
        });
        return rootview;
    }

    public void pageSwitcher(int seconds) {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay
        // in
        // milliseconds
    }

    // this is an inner class...
    class RemindTask extends TimerTask {
        @Override
        public void run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            if(getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                    if (page > 9999) { // In my case the number of pages are 5
                        timer.cancel();
                        // Showing a toast for just testing purpose
                    } else {
                        if (page == 0) {
                            viewPager.setCurrentItem(sliderItems.length-2);
                        }

                        // skip fake page (last), go to first page
                        if (page == sliderItems.length-1) {
                            viewPager.setCurrentItem(0);
                            //notice how this jumps to position 1, and not position 0. Position 0 is the fake page!
                        } else {
                            viewPager.setCurrentItem(page++);
                            if (page == sliderItems.length-1)
                                page = 1;
                        }
                    }
                }
            });

        }
    }

    public void showLocationAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                getActivity());
        alertDialog.setMessage("Use my location from google maps?");
        alertDialog.setPositiveButton("Update",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Location location = appLocationService
                                .getLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            LocationAddress locationAddress = new LocationAddress();
                            locationAddress.getAddressFromLocation(latitude, longitude,
                                    getActivity().getApplicationContext(), new GeocoderHandler());
                        } else {
                            //showSettingsAlert();
                        }
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
                getActivity());
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        getActivity().startActivity(intent);
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

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();

                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            switch (mainActivity.updateLocation) {

                case 0 :  disabledocationEditText.setText(locationAddress);
                    break;
                case 1 : newAddress = getArguments().getString("newAddress");
                        disabledocationEditText.setText(newAddress);
                    break;
                case 2 : address1 = getArguments().getString("newAddress1");
                         address2 = getArguments().getString("newAddress2");
                         disabledocationEditText.setText(address1+", "+address2);
                    break;
                case 3 : disabledocationEditText.setText(locationAddress);
                    break;
            }

        }
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) getActivity();
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private void hiddenKeyboard(View v) {
        InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //((ActionBarActivity) getActivity()).getSupportActionBar().show();
        getView().setFocusableInTouchMode(true);
            getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    mainActivity.onBackPressed();
                    return true;
                }
                return false;
                }
        });
        new AsyncCaller().execute();


    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
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


}


/* SpannableString s = new SpannableString(mainActivity.actionbarTitle);
        s.setSpan(new TypefaceSpan(mainActivity, "OpenSans-Regular.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.rgb(51,51,51)), 0, s.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        s.setSpan(new RelativeSizeSpan(0.9f), 0,10, 0);
        android.support.v7.app.ActionBar actionBar = mainActivity.getSupportActionBar();
        actionBar.setTitle(s);*/