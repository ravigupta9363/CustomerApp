package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ravi_gupta.slider.Adapter.PrescriptionAdapter;
import com.example.ravi_gupta.slider.Database.DatabaseHelper;
import com.example.ravi_gupta.slider.Details.PrescriptionDetail;
import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    GridView gridView;
    PrescriptionAdapter prescriptionAdapter;
    ArrayList<PrescriptionDetail> prescriptionDetails = new ArrayList<PrescriptionDetail>();
    MainActivity mainActivity;
    public static String TAG = "CartFragment";
    DatabaseHelper databaseHelper;
    Uri imagePath;
    Uri thumbnail;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
        return fragment;
    }

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_cart, container, false);
        Uri imageuri = null;
        Uri thumbnailUri = null;


        gridView = (GridView) rootview.findViewById(R.id.fragment_cart_gridview1);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/gothic.ttf");
        TextView toolbarTitle = (TextView)rootview.findViewById(R.id.fragment_cart_textview1);
        ImageButton toolbarIcon = (ImageButton)rootview.findViewById(R.id.fragment_cart_imagebutton1);
        Button toolbarButton = (Button)rootview.findViewById(R.id.fragment_cart_button1);
        toolbarButton.setTypeface(typeface);
        toolbarTitle.setTypeface(typeface2);
        //toolbarButton.setTypeface(typeface);

        toolbarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActionBarActivity) getActivity()).getSupportActionBar().show();
                mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                mainActivity.onBackPressed();
            }
        });

        toolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mainActivity.replaceFragment(R.id.nextButton, null);
            }
        });

        //pastOrdersDetails.add(new PastOrdersDetail("25 AUGUST 2016","02:25 PM","DC1245",320,address,"Delivered"));
        //prescriptionDetails.add(new PrescriptionDetail(imageuri,thumbnailUri));
        //prescriptionDetails.add(new PrescriptionDetail(imageuri,thumbnailUri));
        //prescriptionDetails.add(new PrescriptionDetail(imageuri,thumbnailUri));
        //prescriptionDetails.add(new PrescriptionDetail(imageuri,thumbnailUri));
        Log.v("camera", " Count = " + databaseHelper.getPresciptionCount());
        List<PrescriptionDetail> contacts = databaseHelper.getAllPrescription();
        for (PrescriptionDetail cn : contacts) {
            String log = "Id: "+ cn.getID() +" Name: " + cn.getImageUri() + " ,Phone: " +
                    cn.getThumbnailUri();
            prescriptionDetails.add(new PrescriptionDetail(cn.getID(),cn.getImageUri(),cn.getThumbnailUri()));
            // Writing Contacts to log
            Log.v("camera ", log);
        }
        //addNewPrescription();
        //if(this.imagePath!=null)
            //prescriptionDetails.add(new PrescriptionDetail(this.imagePath,this.thumbnail));


        prescriptionAdapter = new PrescriptionAdapter(getActivity(),R.layout.prescription,prescriptionDetails);
        gridView.setAdapter(prescriptionAdapter);


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
        //databaseHelper = (DatabaseHelper) getActivity();
        mainActivity = (MainActivity)getActivity();
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
        mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    mainActivity.onBackPressed();
                    ((ActionBarActivity) getActivity()).getSupportActionBar().show();
                    mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item= menu.findItem(R.id.cart);
        MenuItem item2 = menu.findItem(R.id.nextButton);
        item.setEnabled(false);
        item.setVisible(false);
        item2.setEnabled(true);
        item2.setVisible(true);

    }
}
