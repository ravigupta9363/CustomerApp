package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ravi_gupta.slider.Database.ProfileDatabase;
import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.Models.Customer;
import com.example.ravi_gupta.slider.R;
import com.example.ravi_gupta.slider.Repository.CustomerRepository;
import com.strongloop.android.loopback.callbacks.VoidCallback;

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
    Button clearButton;
    MainActivity mainActivity;
    ArrayList<String> infoList = new ArrayList<String>();
    public static String TAG = "ProfileFragment";
    ProfileFragment profileFragment;
    ArrayList<String> value;
    ProfileDatabase profileDatabase;
    String updatedName;
    String updatedMail;
    String updatedPhone;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //profileDatabase = new ProfileDatabase(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_profile, container, false);

        Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");

        Drawable drawableProfile = getResources().getDrawable(R.mipmap.dc_profile);
        drawableProfile.setBounds(0, 0, (int) (drawableProfile.getIntrinsicWidth() * 0.7),
                (int) (drawableProfile.getIntrinsicHeight() * 0.7));
        ScaleDrawable sd1 = new ScaleDrawable(drawableProfile, 0, 1f, 1f);

        Drawable drawableEmail = getResources().getDrawable(R.mipmap.dc_about);
        drawableEmail.setBounds(0, 0, (int) (drawableEmail.getIntrinsicWidth() * 0.7),
                (int) (drawableEmail.getIntrinsicHeight() * 0.7));
        ScaleDrawable sd2 = new ScaleDrawable(drawableEmail, 0, 1f, 1f);

        Drawable drawablePhone = getResources().getDrawable(R.mipmap.dc_nav_contact);
        drawablePhone.setBounds(0, 0, (int) (drawablePhone.getIntrinsicWidth() * 0.7),
                (int) (drawablePhone.getIntrinsicHeight() * 0.7));
        ScaleDrawable sd3 = new ScaleDrawable(drawablePhone, 0, 1f, 1f);

        customerName = (TextView)rootview.findViewById(R.id.fragment_profile_textview1);
        customerEmail = (TextView)rootview.findViewById(R.id.fragment_profile_textview2);
        customerPhone = (TextView)rootview.findViewById(R.id.fragment_profile_textview3);
        clearButton = (Button)rootview.findViewById(R.id.fragment_profile_button1);

        customerName.setTypeface(typeface2);
        customerEmail.setTypeface(typeface2);
        customerPhone.setTypeface(typeface2);

        customerName.setCompoundDrawables(sd1.getDrawable(), null, null, null);
        customerEmail.setCompoundDrawables(sd2.getDrawable(), null, null, null);
        customerPhone.setCompoundDrawables(sd3.getDrawable(), null, null, null);

        //ProfileDetail profileDetail = profileDatabase.getProfile();

        /*updatedName = profileDetail.getName();
        updatedMail = profileDetail.getEmail();
        updatedPhone = profileDetail.getPhone();*/
        setProfileData();




        TextView toolbarTitle = (TextView)rootview.findViewById(R.id.fragment_profile_textview4);
        ImageButton toolbarIcon = (ImageButton)rootview.findViewById(R.id.fragment_profile_imagebutton1);
        toolbarTitle.setTypeface(typeface1);

        toolbarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
                mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            }
        });

        /**
         * On Clicking of the clear button
         */
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //profileDatabase.deleteProfile();
                logout();
                mainActivity.replaceFragment(R.id.fragment_profile_button1, profileFragment);
            }
        });
        return rootview;
    }


    private void setProfileData(){
        MainActivity activity = (MainActivity)getActivity();
        CustomerRepository repository = activity.getCustomerRepo();
        Customer customer = repository.getCachedCurrentUser();
        Log.i(TAG, "Setting the profile data");

        if(customer != null) {
            updatedMail = customer.getEmail();
            updatedPhone = customer.getContactNo();
            updatedName = customer.getName();
            customerName.setText(updatedName);
            customerEmail.setText(updatedMail);
            customerPhone.setText(updatedPhone);
        }else{
            /*Dont do anything*/
        }
    }


    private void logout(){
        /*Fill blank strings value first for fast transaction*/
        customerName.setText("");
        customerEmail.setText("");
        customerPhone.setText("");

        final MainActivity activity = (MainActivity)getActivity();

        CustomerRepository repository = activity.getCustomerRepo();
        repository.logout(new VoidCallback(){
            @Override
            public void onSuccess() {

                Log.i(TAG, "User successfully logout from the system..");
                activity.registerInstallation(null);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "Error getting logging out from the server..");
            }
        });
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

  /*  public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == mainActivity.FRAGMENT_CODE && resultCode == Activity.RESULT_OK) {
            if(data != null) {
                List<ProfileDetail> profile = profileDatabase.getProfile();
                for (ProfileDetail profileDetail : profile) {
                    String log = "Id: "+ profileDetail.getName() +" Name: " + profileDetail.getEmail() + " ,Phone: " +
                            profileDetail.getPhone();
                    customerName.setText(profileDetail.getName());
                    customerEmail.setText(profileDetail.getEmail());
                    customerPhone.setText(profileDetail.getPhone());
                    // Writing Contacts to log
                    Log.v("camera ", log);
                }
                if(value != null) {
                    mainActivity.updateUserInfo = true;
                }
            }
        }
    }*/

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
                    return true;
                }
                return false;
            }
        });
    }

}
