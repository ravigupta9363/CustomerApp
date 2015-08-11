package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ravi_gupta.slider.Details.OrderStatusDetail;
import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

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
    ViewPager pager;
    TextView date;
    TextView time;
    TextView orderId;
    TextView orderStatusText;
    TextView subTotalText;
    TextView subTotal;
    TextView serviceChargesText;
    TextView serviceCharge;
    TextView totalText;
    TextView total;
    ImageView orderStatusImage;
    ArrayList<OrderStatusDetail> orderStatusDetails = new ArrayList<OrderStatusDetail>();
    MainActivity mainActivity;
    String fragment;
    Button cancelOrder;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_order_status, container, false);

        Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/OpenSans-Regular.ttf");
        Typeface typeface3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Regular.ttf");
        Typeface typeface4 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Allura-Regular.ttf");

        fragment = getArguments().getString("fragment");

        date = (TextView) rootview.findViewById(R.id.fragment_order_status_textview1);
        time = (TextView) rootview.findViewById(R.id.fragment_order_status_textview2);
        orderId = (TextView) rootview.findViewById(R.id.fragment_order_status_textview3);
        orderStatusText = (TextView) rootview.findViewById(R.id.fragment_order_status_textview4);
        subTotalText = (TextView) rootview.findViewById(R.id.fragment_order_status_textview5);
        subTotal = (TextView) rootview.findViewById(R.id.fragment_order_status_textview6);
        serviceChargesText = (TextView) rootview.findViewById(R.id.fragment_order_status_textview7);
        serviceCharge = (TextView) rootview.findViewById(R.id.fragment_order_status_textview8);
        totalText = (TextView) rootview.findViewById(R.id.fragment_order_status_textview9);
        total = (TextView) rootview.findViewById(R.id.fragment_order_status_textview10);
        orderStatusImage = (ImageView) rootview.findViewById(R.id.fragment_order_status_imageview1);
        cancelOrder = (Button) rootview.findViewById(R.id.fragment_order_status_button1);

        date.setTypeface(typeface2);
        time.setTypeface(typeface2);
        orderId.setTypeface(typeface2);
        orderStatusText.setTypeface(typeface2);
        subTotalText.setTypeface(typeface2);
        serviceChargesText.setTypeface(typeface2);
        totalText.setTypeface(typeface2);
        subTotal.setTypeface(typeface2);
        serviceCharge.setTypeface(typeface2);
        total.setTypeface(typeface2);
        cancelOrder.setTypeface(typeface2);

        TextView toolbarTitle = (TextView)rootview.findViewById(R.id.fragment_order_status_textview11);
        ImageButton toolbarIcon = (ImageButton)rootview.findViewById(R.id.fragment_order_status_imagebutton1);
        TextView realTimeSystemText = (TextView)rootview.findViewById(R.id.fragment_order_status_textview12);
        toolbarTitle.setTypeface(typeface1);
        realTimeSystemText.setTypeface(typeface4);

        final Drawable drawable = getResources().getDrawable(R.mipmap.dc_cancel_order);
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * 0.4),
                (int) (drawable.getIntrinsicHeight() * 0.4));
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 1f, 1f);

        cancelOrder.setCompoundDrawables(sd.getDrawable(), null, null, null);

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

        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("cancelOrder","Your Order has been cancelled");
                orderStatusText.setText("Your Order has been cancelled as per your request");
                cancelOrder.setVisibility(View.GONE);
                orderStatusImage.setImageResource(R.drawable.dc_order_cancelled);
                //Open Main Fragment when order has been cancelled or delivered
            }
        });

        //http://stackoverflow.com/questions/18413309/how-to-implement-a-viewpager-with-different-fragments-layouts
        pager = (ViewPager) rootview.findViewById(R.id.fragment_order_status_viewPager1);
        pager.setAdapter(new MyPagerAdapter(getFragmentManager()));
        return rootview;

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int pos) {
            switch(pos) {

                case 0:
                    orderStatusDetails.add(new OrderStatusDetail("Crocin",5,10,50,"Piramal"));
                    orderStatusDetails.add(new OrderStatusDetail("Crocin",5,10,50,"Piramal"));
                    orderStatusDetails.add(new OrderStatusDetail("Crocin",5,10,50,"Piramal"));
                    orderStatusDetails.add(new OrderStatusDetail("Crocin",5,10,50,"Piramal"));
                    return OrderStatusShopDetailFragment.newInstance("Apollo Pharmacy","120/-",orderStatusDetails);

                case 1: orderStatusDetails.add(new OrderStatusDetail("Crocin",5,10,50,"Piramal"));
                    orderStatusDetails.add(new OrderStatusDetail("Crocin",5,10,50,"Piramal"));
                    orderStatusDetails.add(new OrderStatusDetail("Crocin",5,10,50,"Piramal"));
                    orderStatusDetails.add(new OrderStatusDetail("Crocin",5,10,50,"Piramal"));
                    return OrderStatusShopDetailFragment.newInstance("Gupta Pharmacy","130/-",orderStatusDetails);

                //case 2: return ThirdFragment.newInstance("ThirdFragment, Instance 1");
                //case 3: return ThirdFragment.newInstance("ThirdFragment, Instance 2");
                //case 4: return ThirdFragment.newInstance("ThirdFragment, Instance 3");
                default: orderStatusDetails.add(new OrderStatusDetail("Crocin",5,10,50,"Piramal"));
                    orderStatusDetails.add(new OrderStatusDetail("Crocin",5,10,50,"Piramal"));
                    orderStatusDetails.add(new OrderStatusDetail("Crocin",5,10,50,"Piramal"));
                    orderStatusDetails.add(new OrderStatusDetail("Crocin",5,10,50,"Piramal"));
                    return OrderStatusShopDetailFragment.newInstance("Apollo Pharmacy","120/-",orderStatusDetails);
            }
        }

        @Override
        public int getCount() {
            return 2;
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
                    if (fragment.equals("HomeFragment")) {
                    }
                    else {
                        mainActivity.onBackPressed();
                    }
                    mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    return true;
                }
                return false;
            }
        });
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
