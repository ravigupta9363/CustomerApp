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
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
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
import com.example.ravi_gupta.slider.MyApplication;
import com.example.ravi_gupta.slider.R;
import com.example.ravi_gupta.slider.ViewPager.ViewPagerCustomDuration;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
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
    ViewPagerCustomDuration viewPager;
    PagerAdapter pagerAdapter;
    int[] sliderItems;
    Timer timer;
    int page = 1;
    EditText disabledocationEditText;
    ImageButton menuButton;
    ImageButton cartButton;
    TextView toolbarTitle;
    public TextView cartItems;
    MainActivity mainActivity;
    //String cartNumber;
    public static String TAG = "MainFragment";
    DatabaseHelper databaseHelper;
    String result;
    MyApplication myApplication;
    List<RequestCreator> requestCreatorList;

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
        //sliderItems = new int[]{R.drawable.slider_three, R.drawable.slider_one, R.drawable.slider_two, R.drawable.slider_three, R.drawable.slider_one};
        myApplication = (MyApplication)mainActivity.getApplication();
        requestCreatorList = myApplication.getImageList();

        databaseHelper = new DatabaseHelper(getActivity());
        MainActivity activity = (MainActivity)getActivity();
        Address address = myApplication.getUpdatedAddress();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
            sb.append(address.getAddressLine(i)).append("\n");
        }
        sb.append(address.getLocality()).append("\n");
        sb.append(address.getPostalCode()).append("\n");
        sb.append(address.getCountryName());
        result = sb.toString();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);

        viewPager = (ViewPagerCustomDuration) rootview.findViewById(R.id.viewPager);
        disabledocationEditText = (EditText) rootview.findViewById(R.id.fragment_main_edittext1);
        menuButton = (ImageButton) rootview.findViewById(R.id.fragment_main_imagebutton1);
        cartButton = (ImageButton) rootview.findViewById(R.id.fragment_main_imagebutton2);
        cartItems = (TextView)rootview.findViewById(R.id.fragment_main_textview2);
        toolbarTitle = (TextView) rootview.findViewById(R.id.fragment_main_textview1);
        pagerAdapter = new ViewPagerAdapter(getActivity(), requestCreatorList, viewPager);

        viewPager.setPadding(40, 0, 40, 0);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(10);
        //http://stackoverflow.com/questions/7395655/set-default-page-for-viewpager-in-android
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1, false);
        viewPager.setScrollDurationFactor(3);
        viewPager.setOffscreenPageLimit(3);
        pageSwitcher(4);
        Log.v("images",requestCreatorList.size()+"");

        disabledocationEditText.setText(result);
        Log.d("address", "Display Address = " + result);


        //String cartItem = databaseHelper.getPresciptionCount()+"";

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


        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        toolbarTitle.setTypeface(typeface2);
        Drawable drawable = getResources().getDrawable(R.mipmap.dc_location);
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * 0.7),
                (int) (drawable.getIntrinsicHeight() * 0.7));
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 1f, 1f);
        disabledocationEditText.setCompoundDrawables(sd.getDrawable(), null, null, null);
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
                            viewPager.setCurrentItem(requestCreatorList.size()-2);
                        }

                        // skip fake page (last), go to first page
                        if (page == requestCreatorList.size()-1) {
                            viewPager.setCurrentItem(0);
                            //notice how this jumps to position 1, and not position 0. Position 0 is the fake page!
                        } else {
                            viewPager.setCurrentItem(page++);
                            if (page == requestCreatorList.size()-1)
                                page = 1;
                        }
                    }
                }
            });

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
        //new AsyncCaller().execute();


    }


}
