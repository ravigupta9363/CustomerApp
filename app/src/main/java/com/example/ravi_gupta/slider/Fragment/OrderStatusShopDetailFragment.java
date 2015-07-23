package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ravi_gupta.slider.Adapter.OrderStatusDetailAdapter;
import com.example.ravi_gupta.slider.Details.OrderStatusDetail;
import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderStatusShopDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderStatusShopDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderStatusShopDetailFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView mListview;
    TextView shopName;
    TextView totalAmount;
    OrderStatusDetailAdapter orderStatusDetailAdapter;
    MainActivity mainActivity;
    ArrayList<OrderStatusDetail> orderStatusDetails = new ArrayList<OrderStatusDetail>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static OrderStatusShopDetailFragment newInstance(String shopName,String totalAmount, ArrayList<OrderStatusDetail> orderStatusDetails) {
        OrderStatusShopDetailFragment fragmentStatusShopDetails = new OrderStatusShopDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putString("shopName", shopName);
        bundle.putString("totalAmount", totalAmount);
        //bundle.putParcelableArrayList("orderDetail", (ArrayList<? extends Parcelable>) orderStatusDetails);
        fragmentStatusShopDetails.setArguments(bundle);

        return fragmentStatusShopDetails;
    }

    public OrderStatusShopDetailFragment() {
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
        View rootview = inflater.inflate(R.layout.fragment_order_status_shop_details, container, false);

        mListview = (ListView) rootview.findViewById(R.id.fragment_status_shop_details_listview);
        shopName = (TextView) rootview.findViewById(R.id.fragment_order_status_shop_details_textview1);
        totalAmount = (TextView) rootview.findViewById(R.id.fragment_order_status_shop_details_textview2);

        Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/OpenSans-Regular.ttf");
        Typeface typeface3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Regular.ttf");

        shopName.setTypeface(typeface2);
        totalAmount.setTypeface(typeface2);

        shopName.setText(getArguments().getString("shopName"));
        totalAmount.setText(getArguments().getString("totalAmount"));

       /* for (OrderStatusDetail orderStatusDetail : orderStatusDetails = getArguments().getParcelableArrayList("orderDetail")) {

        }
        ;*/

        orderStatusDetails.add(new OrderStatusDetail("Crocin",5,10,50,"Piramal Healthcare"));
        orderStatusDetails.add(new OrderStatusDetail("Acolate",10,10,50,"Piramal Healthcare"));
        orderStatusDetails.add(new OrderStatusDetail("Sumo",5,10,50,"Piramal Healthcare"));
        orderStatusDetails.add(new OrderStatusDetail("Nice",20,10,50,"Piramal Healthcare"));
        orderStatusDetails.add(new OrderStatusDetail("Chericoff",5,10,50,"Piramal Healthcare"));

        orderStatusDetailAdapter = new OrderStatusDetailAdapter(getActivity(),R.layout.order_status_medicine_list,orderStatusDetails);
        mListview.setAdapter(orderStatusDetailAdapter);

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

        ((ActionBarActivity) getActivity()).getSupportActionBar().hide();
        disableShowHideAnimation(((ActionBarActivity) getActivity()).getSupportActionBar());
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
