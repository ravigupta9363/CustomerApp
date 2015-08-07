package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ravi_gupta.slider.Database.ProfileDatabase;
import com.example.ravi_gupta.slider.Details.ProfileDetail;
import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileEditFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String TAG = "ProfileEditFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText customerName;
    EditText customerMail;
    EditText customerPhone;
    Button saveButton;
    ArrayList<String> updatedArrayList;
    MainActivity mainActivity;
    String updatedName;
    String updatedMail;
    String updatedPhone;
    ProfileDatabase profileDatabase;
    MainFragment mainFragment;
    String fragment;

    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static ProfileEditFragment newInstance() {
        ProfileEditFragment fragment = new ProfileEditFragment();
        return fragment;
    }

    public ProfileEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileDatabase = new ProfileDatabase(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        fragment = getArguments().getString("fragment");


        //Bundle bundle = getArguments();
        //ArrayList<String> infoUser = bundle.getStringArrayList("infoUser");
        Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/OpenSans-Regular.ttf");
        Typeface typeface3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Regular.ttf");

        /*Drawable drawableProfile = getResources().getDrawable(R.mipmap.dc_profile);
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
        ScaleDrawable sd3 = new ScaleDrawable(drawablePhone, 0, 1f, 1f);*/

        customerName = (EditText) rootview.findViewById(R.id.fragment_profile_edit_edittext1);
        customerMail = (EditText) rootview.findViewById(R.id.fragment_profile_edit_edittext2);
        customerPhone = (EditText) rootview.findViewById(R.id.fragment_profile_edit_edittext3);
        saveButton = (Button) rootview.findViewById(R.id.fragment_profile_edit_button1);
        final TextInputLayout nameLayout = (TextInputLayout) rootview.findViewById(R.id.fragment_profile_edit_layout1);
        final TextInputLayout mailLayout = (TextInputLayout) rootview.findViewById(R.id.fragment_profile_edit_layout2);
        final TextInputLayout phoneLayout = (TextInputLayout) rootview.findViewById(R.id.fragment_profile_edit_layout3);
        nameLayout.setErrorEnabled(true);
        mailLayout.setErrorEnabled(true);
        phoneLayout.setErrorEnabled(true);

        customerName.setTypeface(typeface2);
        customerMail.setTypeface(typeface2);
        customerPhone.setTypeface(typeface2);

        //customerName.setCompoundDrawables(sd1.getDrawable(), null, null, null);
        //customerMail.setCompoundDrawables(sd2.getDrawable(), null, null, null);
        //customerPhone.setCompoundDrawables(sd3.getDrawable(), null, null, null);

        ProfileDetail profileDetail = profileDatabase.getProfile();

            updatedName = profileDetail.getName();
            updatedMail = profileDetail.getEmail();
            updatedPhone = profileDetail.getPhone();


        customerName.setText(updatedName);
        customerMail.setText(updatedMail);
        customerPhone.setText(updatedPhone);

       /* if(mainActivity.updateUserInfoProfileEditFragment == true) {
            /*customerName.setText(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("UPDATED_NAME", "defaultStringIfNothingFound"));
            customerMail.setText(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("UPDATED_MAIL", "defaultStringIfNothingFound"));
            customerPhone.setText( PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("UPDATED_PHONE", "defaultStringIfNothingFound"));
            Log.v("profile","Bye Bye "+customerName.getText());
            List<ProfileDetail> profile = profileDatabase.getProfile();
            for (ProfileDetail profileDetail : profile) {
                String log = "Id: "+ profileDetail.getName() +" Name: " + profileDetail.getEmail() + " ,Phone: " +
                        profileDetail.getPhone();
                updatedName = profileDetail.getName();
                updatedMail = profileDetail.getEmail();
                updatedPhone = profileDetail.getPhone();
                // Writing Contacts to log
                Log.v("camera ", log);
            }
        }*/


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //updatedArrayList = new ArrayList<String>();
                //mainActivity.updateUserInfoProfileEditFragment = true;

                updatedName = customerName.getText().toString();
                updatedMail = customerMail.getText().toString();
                updatedPhone = customerPhone.getText().toString();

                if(!updatedName.matches("[a-zA-Z ]{3,30}"))
                    nameLayout.setError("Enter correct name");
                if(!isEmailValid(updatedMail))
                    mailLayout.setError("Enter correct email");
                if(!isPhoneValid(updatedPhone))
                    phoneLayout.setError("Enter correct number");
                if(updatedName.matches("[a-zA-Z ]{3,30}"))
                    nameLayout.setError("");
                if(isEmailValid(updatedMail))
                    mailLayout.setError("");
                if(isPhoneValid(updatedPhone))
                    phoneLayout.setError("");
                if(updatedName.matches(""))
                    nameLayout.setError("This field is mandotary");
                if(updatedMail.matches(""))
                    mailLayout.setError("This field is mandotary");
                if(updatedPhone.matches(""))
                    phoneLayout.setError("This field is mandotary");


                Log.v("profile", updatedName + updatedMail + updatedPhone);

               /* ProfileDetail profileDetail = profileDatabase.getProfile();

                    updatedName = profileDetail.getName();
                    updatedMail = profileDetail.getEmail();
                    updatedPhone = profileDetail.getPhone();*/

                //mainFragment = (MainFragment) getActivity().getSupportFragmentManager().findFragmentByTag(MainFragment.TAG);
                //Log.v("profile", "Profile " + updatedName + updatedMail + updatedPhone);
                hiddenKeyboard(customerPhone);
                //http://stackoverflow.com/questions/15805555/java-regex-to-validate-full-name-allow-only-spaces-and-letters
                if(updatedName.matches("[a-zA-Z ]{3,30}") && isEmailValid(updatedMail) && isPhoneValid(updatedPhone)) {
                    if (fragment.equals("profileFragment")) {
                        profileDatabase.addProfileData(new ProfileDetail(updatedName, updatedMail, updatedPhone));
                        mainActivity.onBackPressed();
                    }
                    else
                        mainActivity.replaceFragment(R.id.fragment_profile_edit_button1, null);
                }


               /* PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("UPDATED_NAME", updatedName).commit();
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("UPDATED_MAIL", updatedMail).commit();
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("UPDATED_PHONE", updatedPhone).commit();

                updatedArrayList.add(0, updatedName);
                updatedArrayList.add(1,updatedMail);
                updatedArrayList.add(2, updatedPhone);

                Log.v("profile","Hello "+PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("UPDATED_NAME", "defaultStringIfNothingFound"));*/

                //Intent intent = new Intent();
                //getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                //


            }
        });
        TextView toolbarTitle = (TextView)rootview.findViewById(R.id.fragment_profile_edit_textview4);
        ImageButton toolbarIcon = (ImageButton)rootview.findViewById(R.id.fragment_profile_edit_imagebutton1);
        toolbarTitle.setTypeface(typeface1);

        toolbarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenKeyboard(customerPhone);
                mainActivity.onBackPressed();
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

    //http://stackoverflow.com/questions/6119722/how-to-check-edittexts-text-is-email-address-or-not
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    boolean isPhoneValid(CharSequence email) {
        return Patterns.PHONE.matcher(email).matches();
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

    private void hiddenKeyboard(View v) {
        InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
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
                    hiddenKeyboard(customerPhone);
                    mainActivity.onBackPressed();

                    return true;
                }
                return false;
            }
        });
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
