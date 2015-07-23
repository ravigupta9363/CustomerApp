package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView customerName;
    TextView customerPhone;
    TextView customerEmail;
    Button editButton;
    MainActivity mainActivity;
    ArrayList<String> infoList = new ArrayList<String>();
    public static String TAG = "ProfileFragment";
    ProfileFragment profileFragment;
    ArrayList<String> value;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    public ProfileFragment() {
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
        View rootview =  inflater.inflate(R.layout.fragment_profile, container, false);

        Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/OpenSans-Regular.ttf");
        Typeface typeface3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Regular.ttf");

        customerName = (TextView)rootview.findViewById(R.id.fragment_profile_textview1);
        customerEmail = (TextView)rootview.findViewById(R.id.fragment_profile_textview2);
        customerPhone = (TextView)rootview.findViewById(R.id.fragment_profile_textview3);
        editButton = (Button)rootview.findViewById(R.id.fragment_profile_button1);

        customerName.setTypeface(typeface2);
        customerEmail.setTypeface(typeface2);
        customerPhone.setTypeface(typeface2);

        TextView toolbarTitle = (TextView)rootview.findViewById(R.id.fragment_profile_textview4);
        ImageButton toolbarIcon = (ImageButton)rootview.findViewById(R.id.fragment_profile_imagebutton1);
        toolbarTitle.setTypeface(typeface1);

        toolbarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
                disableShowHideAnimation(((ActionBarActivity) getActivity()).getSupportActionBar());
                ((ActionBarActivity) getActivity()).getSupportActionBar().show();
                mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            }
        });

        if(mainActivity.updateUserInfo) {
            customerName.setText(value.get(0));
            customerEmail.setText(value.get(1));
            customerPhone.setText(value.get(2));
            mainActivity.updateUserInfo  = false;
        }

        Log.v(TAG, "Data passed from Child fragment = " + customerEmail.getText());

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                android.support.v4.app.Fragment newFragment4 = new ProfileEditFragment();
                newFragment4.setTargetFragment(profileFragment, mainActivity.FRAGMENT_CODE);
                ft.replace(R.id.fragment_main_container, newFragment4);
                ft.addToBackStack(null); // Ads FirstFragment to the back-stack
                ft.commit();
            }
        });

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
        profileFragment = (ProfileFragment) this;
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == mainActivity.FRAGMENT_CODE && resultCode == Activity.RESULT_OK) {
            if(data != null) {
                value = data.getStringArrayListExtra("updatedArrayList");
                if(value != null) {
                    mainActivity.updateUserInfo = true;
                }
            }
        }
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
