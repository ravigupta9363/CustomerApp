package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ravi_gupta.slider.Adapter.AddressAdapter;
import com.example.ravi_gupta.slider.Details.AddressDetails;
import com.example.ravi_gupta.slider.Location.AppLocationService;
import com.example.ravi_gupta.slider.Location.DelayAutoCompleteTextView;
import com.example.ravi_gupta.slider.Location.GeoSearchResult;
import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

//Places API : AIzaSyB8ztCQFxZnBcUFbewXTlTgobzOhSe_iBU
//AIzaSyAHjYVrAglJOuY_2Fet5HThg-slqLujVS0
//Browser Key : AIzaSyDjO4nERuBCQ_iFLx1pNTFuWcUAdRSi3u4
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link changeLocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link changeLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class changeLocationFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DelayAutoCompleteTextView locationEdittext;
    ListView mListview;
    AddressAdapter addressAdapter;
    ArrayList<AddressDetails> addressDetailses = new ArrayList<AddressDetails>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MainActivity mainActivity;
    AppLocationService appLocationService;
    public static String TAG = "changeLocationFragment";
    ProgressBar spinner;
    Button useMyLocationButton;
    private Integer THRESHOLD = 2;
    ProgressBar progressBar;
    GeoSearchResult resultToBeAddedOnList;


    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static changeLocationFragment newInstance() {
        changeLocationFragment fragment = new changeLocationFragment();
        return fragment;
    }

    public changeLocationFragment() {
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
        final View rootview = inflater.inflate(R.layout.fragment_change_location, container, false);
        mainActivity = (MainActivity)getActivity();
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/gothic.ttf");

        useMyLocationButton = (Button) rootview.findViewById(R.id.fragment_change_location_button2);
        spinner = (ProgressBar)rootview.findViewById(R.id.progressBar);

        TextView toolbarTitle = (TextView)rootview.findViewById(R.id.fragment_change_location_textview1);
        ImageButton toolbarIcon = (ImageButton)rootview.findViewById(R.id.fragment_change_location_imagebutton1);
        toolbarTitle.setTypeface(typeface);

        toolbarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
                disableShowHideAnimation(((ActionBarActivity) getActivity()).getSupportActionBar());
                ((ActionBarActivity) getActivity()).getSupportActionBar().show();
                mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            }
        });

        useMyLocationButton.setTypeface(typeface);

      /*  locationEdittext = (DelayAutoCompleteTextView) rootview.findViewById(R.id.fragment_change_location_edittext);
        locationEdittext.setDropDownBackgroundResource(R.color.autocomplete_background_color);
        locationEdittext.setThreshold(THRESHOLD);
        locationEdittext.setAdapter(new GeoAutoCompleteAdapter(getActivity())); // 'this' is Activity instance
        locationEdittext.setLoadingIndicator(
                (android.widget.ProgressBar) rootview.findViewById(R.id.fragment_change_pb_loading_indicator));*/
        progressBar = (ProgressBar) rootview.findViewById(R.id.fragment_change_pb_loading_indicator);
      /*  mListview = (ListView) rootview.findViewById(R.id.addressListView);
        addressAdapter = new AddressAdapter(getActivity(),R.layout.address_list,addressDetailses);
        mListview.setAdapter(addressAdapter);*/

        useMyLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // spinner.setVisibility(View.VISIBLE);
                mainActivity.replaceFragment(R.id.fragment_change_location_button2, null);
                disableShowHideAnimation(((ActionBarActivity) getActivity()).getSupportActionBar());
                ((ActionBarActivity) getActivity()).getSupportActionBar().show();
                mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        });

    /*    locationEdittext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                // imm.hideSoftInputFromWindow(fragment_change_location_edittext.getWindowToken(), 0);
                hiddenKeyboard(locationEdittext);
                GeoSearchResult result = (GeoSearchResult) adapterView.getItemAtPosition(position);
                locationEdittext.setText(result.getAddress());
                resultToBeAddedOnList = result;
                Log.v("location3",result+"");
                mainActivity.replaceFragment(R.id.fragment_change_location_edittext, result);


            }
        });*/

      /*  mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.v("Listview1", "List View = " + mListview.getItemAtPosition(position) + "");
                AddressDetails addressDetails = (AddressDetails) mListview.getItemAtPosition(position);

                //Log.v("Listview1", "Shop Name = " + shopListDetails.shopName + "");
                mainActivity.replaceFragment(R.id.addressListView, addressDetails);

            }
        });*/


        return rootview;
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

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    Log.v("location2", bundle + "");
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            //Main.setText(locationAddress);
            //fragment_change_location_edittext.setText(locationAddress);
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        disableShowHideAnimation(((ActionBarActivity) getActivity()).getSupportActionBar());
        ((ActionBarActivity) getActivity()).getSupportActionBar().hide();
        mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        // handle back button's click listener
                        disableShowHideAnimation(((ActionBarActivity) getActivity()).getSupportActionBar());
                        mainActivity.onBackPressed();
                        ((ActionBarActivity) getActivity()).getSupportActionBar().show();
                        mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
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
           // spinner.setVisibility(View.VISIBLE);
            //this method will be running on UI thread
        }
        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here


            //Data List View Suggetion
           /* addressDetailses.add(new AddressDetails("Sector 26","DLF Phase 3, U Block"));
            addressDetailses.add(new AddressDetails("Sector 21","DLF Phase 3, P Block"));
            addressDetailses.add(new AddressDetails("Sector 25", "DLF Phase 2"));
            addressDetailses.add(new AddressDetails("Ghitorni", "New Colony"));
            if(mainActivity.addedToList == false){
                mainActivity.addedToList = true;
            }
            else {
                //Log.v("Result",resultToBeAddedOnList.getAddress());
                //addressDetailses.add(new AddressDetails(resultToBeAddedOnList.getAddress(), ""));
            }*/

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //this method will be running on UI thread
            //spinner.setVisibility(View.GONE);

        }

    }

    private void hiddenKeyboard(View v) {
        InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static void disableShowHideAnimation(ActionBar actionBar) {
        try
        {
            actionBar.getClass().getDeclaredMethod("setShowHideAnimationEnabled", boolean.class).invoke(actionBar, false);
        }
        catch (Exception exception)
        {
            try {
                Field mActionBarField = actionBar.getClass().getSuperclass().getDeclaredField("mActionBar");
                mActionBarField.setAccessible(true);
                Object icsActionBar = mActionBarField.get(actionBar);
                Field mShowHideAnimationEnabledField = icsActionBar.getClass().getDeclaredField("mShowHideAnimationEnabled");
                mShowHideAnimationEnabledField.setAccessible(true);
                mShowHideAnimationEnabledField.set(icsActionBar,false);
                Field mCurrentShowAnimField = icsActionBar.getClass().getDeclaredField("mCurrentShowAnim");
                mCurrentShowAnimField.setAccessible(true);
                mCurrentShowAnimField.set(icsActionBar,null);
            }catch (Exception e){
                //....
            }
        }
    }


}



/*appLocationService = new AppLocationService(getActivity());
                    Location location = appLocationService
                            .getLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        LocationAddress locationAddress = new LocationAddress();
                        locationAddress.getAddressFromLocation(latitude, longitude,
                                getActivity(), new GeocoderHandler());
                    } else {
                        //showSettingsAlert();
                    }*/