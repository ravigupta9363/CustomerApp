package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ravi_gupta.slider.Adapter.PastOrderAdapter;
import com.example.ravi_gupta.slider.Details.PastOrdersDetail;
import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.Models.Constants;
import com.example.ravi_gupta.slider.Models.Order;
import com.example.ravi_gupta.slider.R;
import com.example.ravi_gupta.slider.Repository.OrderRepository;
import com.strongloop.android.loopback.callbacks.ListCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PastOrderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PastOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PastOrderFragment extends android.support.v4.app.Fragment {

    ListView mListview;
    PastOrderAdapter pastOrderAdapter;
    ArrayList<PastOrdersDetail> pastOrdersDetails = new ArrayList<PastOrdersDetail>();
    MainActivity mainActivity;
    public static String TAG = "PastOrderFragment";
    String fragment;
    OrderRepository orderRepository;
    List<Map<String, String>> prescriptionStatic = null;
    ImageView noPastOrder;
    TextView noPastOrderText;
    View rootview;
    ProgressBar progressBar;
    Button addProfileButton;


    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static PastOrderFragment newInstance() {
        PastOrderFragment fragment = new PastOrderFragment();
        return fragment;
    }

    public PastOrderFragment() {
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
        rootview = inflater.inflate(R.layout.fragment_past_order, container, false);
       // fragment = getArguments().getString("fragment");
        Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/OpenSans-Regular.ttf");
        //Typeface typeface3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Regular.ttf");
        //new AsyncCaller().execute();
        mListview = (ListView) rootview.findViewById(R.id.fragment_past_order_listview1);

        TextView toolbarTitle = (TextView)rootview.findViewById(R.id.fragment_past_order_textview4);
        ImageButton toolbarIcon = (ImageButton)rootview.findViewById(R.id.fragment_past_order_imagebutton1);
        noPastOrder = (ImageView)rootview.findViewById(R.id.fragment_past_order_imageview1);
        noPastOrderText = (TextView)rootview.findViewById(R.id.fragment_past_order_textview1);
        //progressBar = (ProgressBar)rootview.findViewById(R.id.fragment_past_order_progressbar1);
        addProfileButton = (Button) rootview.findViewById(R.id.fragment_past_order_button1);
        toolbarTitle.setTypeface(typeface1);
        noPastOrderText.setTypeface(typeface2);
        pastOrderAdapter = new PastOrderAdapter(getActivity(),R.layout.past_order_layout,pastOrdersDetails);
        mListview.setAdapter(pastOrderAdapter);

        toolbarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
                mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            }
        });

        addProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.replaceFragment(R.id.fragment_past_order_button1,"addProfile");
            }
        });

        return rootview;
    }



    private void loadPastOrder(){
        /**
         * Show the loading bar..
         */
        mainActivity.getActivityHelper().launchRingDialog(mainActivity);
        //progressBar.setVisibility(View.VISIBLE);
        Object userId = mainActivity.getCustomerRepo().getCurrentUserId();
        if(!(userId == null)) {
            mainActivity.getCustomerRepo().getOrders(userId, new ListCallback<Order>() {
                @Override
                public void onSuccess(final List<Order> orderList) {
//                Move to async task
                    if (orderList.size() == 0) {
                        noPastOrder.setVisibility(View.VISIBLE);
                        noPastOrderText.setVisibility(View.VISIBLE);
                        mListview.setVisibility(View.GONE);
                    } else {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (Order order : orderList) {

                                    noPastOrder.setVisibility(View.GONE);
                                    noPastOrderText.setVisibility(View.GONE);
                                    mListview.setVisibility(View.VISIBLE);

                                    List<Map<String, String>> prescription = order.getPrescription();
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                                    format.setTimeZone(TimeZone.getTimeZone("IST"));
                                    java.util.Date date = null;
                                    try {
                                        date = format.parse(order.getDate());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    String time = date.toString().substring(12, 16);
                                    String orderDay = date.toString().substring(8, 10);
                                    String orderMonth = date.toString().substring(4, 7);
                                    String orderYear = date.toString().substring(30, 34);
                                    String actualDate = orderDay + " " + orderMonth.toUpperCase() + " " + orderYear;

                                    pastOrdersDetails.add(new PastOrdersDetail(actualDate, time, order.getId().toString(), order.getGoogleAddr(), prescription, true));
                                }
                                pastOrderAdapter.notifyDataSetChanged();
                            }//public void run() {
                        });
                    }

                    /**
                     * Close the loading bar
                     */
                    mainActivity.getActivityHelper().closeLoadingBar();
                }

                @Override
                public void onError(Throwable t) {

                    Log.d(TAG, "Error fetching order for customer");
                    noPastOrderText.setText("Unable to connect to server");
                    noPastOrder.setVisibility(View.VISIBLE);
                    noPastOrder.setImageResource(R.drawable.order_cancelled);
                    noPastOrderText.setVisibility(View.VISIBLE);
                    mListview.setVisibility(View.GONE);

                    /**
                     * Close the loading bar
                     */
                    mainActivity.getActivityHelper().closeLoadingBar();
                }
            });
        }else{
            /**
             * Close the loading bar
             */
            mainActivity.getActivityHelper().closeLoadingBar();
            Log.d(Constants.TAG, "Customers not logged in.");
            addProfileButton.setVisibility(View.VISIBLE);
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
        super.onResume();

        mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    mainActivity.onBackPressed();
                    mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                   /* if(!fragment.equals("repeatOrder")) {

                    }*/
                    return true;
                }
                return false;
            }
        });
        //Load order from the database..
        loadPastOrder();
    }





    /*private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //spinner.setVisibility(View.VISIBLE);
            //this method will be running on UI thread
        }
        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here

            *//*mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });*//*

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //this method will be running on UI thread
            //spinner.setVisibility(View.GONE);
        }

    }*/
}
