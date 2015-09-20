package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ravi_gupta.slider.Adapter.OrderStatusImageSliderAdapter;
import com.example.ravi_gupta.slider.Location.AppLocationService;
import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NoAddressFoundFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NoAddressFoundFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoAddressFoundFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView text;
    Button exitButton;
    Button refreshButton;
    MainActivity mainActivity;
    boolean forward = true;
    AppLocationService appLocationService;
    public static String TAG = "NoAddressFoundFragment";
    OrderStatusImageSliderAdapter orderStatusImageSliderAdapter;
    com.example.ravi_gupta.slider.ViewPager.ViewPagerCustomDuration scViewPager;
    int page = 1;
    Timer timer;
    int[] mResources = {
            R.mipmap.no_address_found_background,
            R.mipmap.sc1,
            R.mipmap.sc2,
            R.mipmap.sc3,
    };

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static NoAddressFoundFragment newInstance() {
        NoAddressFoundFragment fragment = new NoAddressFoundFragment();
        return fragment;
    }

    public NoAddressFoundFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_no_address_found, container, false);
        Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface typeface3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Regular.ttf");

        text = (TextView) rootview.findViewById(R.id.fragment_no_address_found_textview1);
        exitButton = (Button) rootview.findViewById(R.id.fragment_no_address_found_button1);
        refreshButton = (Button) rootview.findViewById(R.id.fragment_no_address_found_button2);

        orderStatusImageSliderAdapter = new OrderStatusImageSliderAdapter(mainActivity,mResources);
        scViewPager = (com.example.ravi_gupta.slider.ViewPager.ViewPagerCustomDuration) rootview.findViewById(R.id.fragment_no_address_found_pager);
        scViewPager.setAdapter(orderStatusImageSliderAdapter);
        scViewPager.setCurrentItem(0, false);
        scViewPager.setScrollDurationFactor(5);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check device for Play Services APK.
                pageSwitcher(4);
            }
        }, 5000);


        text.setTypeface(typeface2);
        exitButton.setTypeface(typeface2);
        refreshButton.setTypeface(typeface2);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.getActivityHelper().startHelperActivity();
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
            if(mainActivity == null) {
                return;
            }
            else {
                mainActivity.runOnUiThread(new Runnable() {
                    public void run() {

                        if(forward == true){
                            scViewPager.setCurrentItem(page);
                            if(page == mResources.length)
                            {
                                forward = false;
                            }
                            page++;

                        }
                        if(forward == false) {
                            page = page - 1;
                            scViewPager.setCurrentItem(page);
                            if(page == 0)
                            {
                                forward = true;
                            }
                        }

                    }
                });
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
    public void onResume() {
        mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        super.onResume();
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

}
