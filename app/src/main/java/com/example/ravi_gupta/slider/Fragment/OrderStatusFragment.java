package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ravi_gupta.slider.Details.OrderStatusDetail;
import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.R;
import com.example.ravi_gupta.slider.TypefaceSpan;

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
        SpannableString s = new SpannableString("Order Status");
        s.setSpan(new TypefaceSpan(mainActivity, "gothic.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, s.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        android.support.v7.app.ActionBar actionBar = mainActivity.getSupportActionBar();
        actionBar.setTitle(s);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_order_status, container, false);

        Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/OpenSans-Regular.ttf");
        Typeface typeface3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Regular.ttf");

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

        date.setTypeface(typeface2);
        time.setTypeface(typeface2);
        orderId.setTypeface(typeface2);
        orderStatusText.setTypeface(typeface1);
        subTotalText.setTypeface(typeface2);
        serviceChargesText.setTypeface(typeface2);
        totalText.setTypeface(typeface2);
        subTotal.setTypeface(typeface2);
        serviceCharge.setTypeface(typeface2);
        total.setTypeface(typeface2);

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
    }

}
