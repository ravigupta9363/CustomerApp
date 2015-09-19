package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ravi_gupta.slider.Database.OrderStatusDataBase;
import com.example.ravi_gupta.slider.Details.OrderStatusDetail;
import com.example.ravi_gupta.slider.GcmIntentService;
import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.Models.Constants;
import com.example.ravi_gupta.slider.Models.Order;
import com.example.ravi_gupta.slider.R;
import com.example.ravi_gupta.slider.Repository.OrderRepository;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderStatusFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderStatusFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String TAG = "OrderStatusFragment" ;
    TextView date;
    TextView time;
    TextView orderId;
    TextView orderStatusText;
    ImageView orderStatusImage;
    ArrayList<OrderStatusDetail> orderStatusDetails = new ArrayList<OrderStatusDetail>();
    MainActivity mainActivity;
    String fragment;
    Button cancelOrder;
    Button home;
    Button retry;
    Button orderNow;
    LinearLayout linearLayout;
    LinearLayout noOrderStatusLayout;
    android.support.v7.widget.Toolbar statusToolbar;
    View view;
    String status;
    OrderStatusDataBase orderStatusDataBase;
    Order order_;
    String id;


    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static OrderStatusFragment newInstance() {
        OrderStatusFragment fragment = new OrderStatusFragment();
        return fragment;
    }

    public OrderStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderStatusDataBase = new OrderStatusDataBase(mainActivity);
        id = orderStatusDataBase.getOrderStatus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_order_status, container, false);

        Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/OpenSans-Regular.ttf");

        fragment = getArguments().getString("fragment");

        date = (TextView) rootview.findViewById(R.id.fragment_order_status_textview1);
        time = (TextView) rootview.findViewById(R.id.fragment_order_status_textview2);
        orderId = (TextView) rootview.findViewById(R.id.fragment_order_status_textview3);
        orderStatusText = (TextView) rootview.findViewById(R.id.fragment_order_status_textview4);
        orderStatusImage = (ImageView) rootview.findViewById(R.id.fragment_order_status_imageview1);
        cancelOrder = (Button) rootview.findViewById(R.id.fragment_order_status_button1);
        home = (Button) rootview.findViewById(R.id.fragment_order_status_button2);
        retry = (Button) rootview.findViewById(R.id.fragment_order_status_button4);
        linearLayout = (LinearLayout) rootview.findViewById(R.id.fragment_order_status_linear_layout);
        noOrderStatusLayout = (LinearLayout) rootview.findViewById(R.id.fragment_order_status_linear_layout2);
        orderNow = (Button) rootview.findViewById(R.id.fragment_order_status_button3);
        statusToolbar = (android.support.v7.widget.Toolbar) rootview.findViewById(R.id.order_status_toolbar);
        view = (View)rootview.findViewById(R.id.order_status_view);

        linearLayout.setVisibility(View.GONE);
        noOrderStatusLayout.setVisibility(View.VISIBLE);
        orderNow.setVisibility(View.VISIBLE);
        statusToolbar.setVisibility(View.VISIBLE);
        view.setVisibility(View.VISIBLE);

        date.setTypeface(typeface2);
        time.setTypeface(typeface2);
        orderId.setTypeface(typeface2);
        orderStatusText.setTypeface(typeface2);
        cancelOrder.setTypeface(typeface2);
        home.setTypeface(typeface2);

        TextView toolbarTitle = (TextView)rootview.findViewById(R.id.fragment_order_status_textview11);
        ImageButton toolbarIcon = (ImageButton)rootview.findViewById(R.id.fragment_order_status_imagebutton1);
        TextView realTimeSystemText = (TextView)rootview.findViewById(R.id.fragment_order_status_textview12);
        TextView onlyOneOrder = (TextView)rootview.findViewById(R.id.fragment_order_status_textview13);
        toolbarTitle.setTypeface(typeface1);
        realTimeSystemText.setTypeface(typeface2);
        onlyOneOrder.setTypeface(typeface2);

        //Drawable orderStatusBackground = rootview.findViewById(R.id.fragment_order_status_background_layout).getBackground();
        //orderStatusBackground.setAlpha(127);


        if (fragment.equals("HomeFragment") || fragment.equals("NotDelivered")) {
            cancelOrder.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            noOrderStatusLayout.setVisibility(View.GONE);
            orderNow.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            statusToolbar.setVisibility(View.GONE);
            toolbarIcon.setVisibility(View.GONE);
        }



       /* final Drawable drawable = getResources().getDrawable(R.mipmap.dc_cancel_order);
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * 0.4),
                (int) (drawable.getIntrinsicHeight() * 0.4));
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 1f, 1f);
        cancelOrder.setCompoundDrawables(sd.getDrawable(), null, null, null);*/

        toolbarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment.equals("HomeFragment")) {
                } else {
                    mainActivity.onBackPressed();
                }
                mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            }
        });

        orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
                mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        });

        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show the loading bar..
                cancelOrder();
                //orderStatusImage.setImageResource(R.drawable.order_cancelled);
                //Open Main Fragment when order has been cancelled or delivered
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Redirect to home",Toast.LENGTH_SHORT).show();
                mainActivity.replaceFragment(R.id.fragment_order_status_button2, null);
            }
        });

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadOrder(id);
                retry.setVisibility(View.GONE);
                cancelOrder.setVisibility(View.VISIBLE);
            }
        });


        /**
         * Load order
         */

        loadOrder(id);

        //http://stackoverflow.com/questions/18413309/how-to-implement-a-viewpager-with-different-fragments-layouts
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
                    if (fragment.equals("HomeFragment")) {
                    } else {
                        mainActivity.onBackPressed();
                    }
                    mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    return true;
                }
                return false;
            }
        });
        new AsyncCaller().execute();
    }




    private void changeStatus(MainActivity activity, final String subject){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                orderStatusText.setText(subject);
                Log.d("serverFarm",subject);
            }//public void run() {
        });
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
            while (true ) {
                //Checking if status has been changed..
                status = GcmIntentService.getStatus();
                if (!(status == null)) {
                    if(status.equals("5003")){
                        changeStatus(mainActivity, "Delivered");
                        return null;
                    }
                    else if(status.equals("5001")){
                        changeStatus(mainActivity, "Preparing");
                    }
                    else if(status.equals("5002")){
                        changeStatus(mainActivity, "At Retailer");
                    }
                    else if(status.equals("5004")){
                        changeStatus(mainActivity, "Cancelled");
                        return null;
                    }
                    else{
                        //do nothing here..
                        /**
                         * If status doesnot belong to any of these category..
                         */

                    }

                }//if

            }//While
        }//doInBackground

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            cancelOrder.setVisibility(View.GONE);
            home.setVisibility(View.VISIBLE);

        }
    }//AsyncCaller


    private void loadStatus(String status){
        if (!(status == null)) {
            if(status.equals("5003")){
                changeStatus(mainActivity, "Delivered");
                orderStatusDataBase.deleteOrderStatus();
                cancelOrder.setVisibility(View.GONE);
                home.setVisibility(View.VISIBLE);

            }
            else if(status.equals("5001")){
                changeStatus(mainActivity, "Preparing");
            }
            else if(status.equals("5002")){
                changeStatus(mainActivity, "At Retailer");
            }
            else if(status.equals("5004")){
                changeStatus(mainActivity, "Cancelled");
                orderStatusDataBase.deleteOrderStatus();
                cancelOrder.setVisibility(View.GONE);
                home.setVisibility(View.VISIBLE);

            }
            else{
                //do nothing here..
                /**
                 * If status doesnot belong to any of these category..
                 */
                changeStatus(mainActivity, "Preparing");
            }

        }//if
    }


    private void loadOrder(final String id){

        OrderRepository orderRepository = mainActivity.restAdapter.createRepository(OrderRepository.class);
        orderRepository.getOrder(id, new ObjectCallback<Order>() {
            @Override
            public void onSuccess(Order order) {
                if(order == null){
                    showRetryButton("Order not found.");
                }
                order_ = order;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                format.setTimeZone(TimeZone.getTimeZone("IST"));
                java.util.Date date_ = null;
                try {
                    date_ = format.parse(order.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String time_ = date_.toString().substring(11, 16);
                //Now parsing time..
                time_ = mainActivity.getActivityHelper().parseISTTime(time_);

                String orderDay = date_.toString().substring(8, 10);
                String orderMonth = date_.toString().substring(4, 7);
                String orderYear = date_.toString().substring(30, 34);
                String actualDate = orderDay + " " + orderMonth.toUpperCase() + " " + orderYear;


                String orderStatus = order.getPrototypeStatusCode();
                loadStatus(orderStatus);
                date.setText(actualDate);
                time.setText(time_);
                orderId.setText(id);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(Constants.TAG, "Error fetching order ");
                Log.e(Constants.TAG, t.toString());
                showRetryButton("Error Loading Order");
            }
        });
    }

    private void showRetryButton(String message){
        orderStatusText.setText(message);
        retry.setVisibility(View.VISIBLE);
        cancelOrder.setVisibility(View.GONE);
        home.setVisibility(View.GONE);
    }

    public void cancelOrder() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                mainActivity);

        alertDialog.setMessage("Cancel Order?");
        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mainActivity.getActivityHelper().launchRingDialog(mainActivity);

                        orderStatusText.setText("Cancelled");
                        order_.setPrototypeStatusCode("5004");
                        order_.save(new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                //do something
                                mainActivity.getActivityHelper().closeLoadingBar();
                                //TODO DELETE ID FROM DATABASE HERE
                                //Delete the order status
                                orderStatusDataBase.deleteOrderStatus();

                                cancelOrder.setVisibility(View.GONE);
                                home.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.e(Constants.TAG, "Error Cancelling Order");
                                Log.e(Constants.TAG, t.getMessage());
                                mainActivity.getActivityHelper().closeLoadingBar();

                            }
                        });

                    }
                });
        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }
}
