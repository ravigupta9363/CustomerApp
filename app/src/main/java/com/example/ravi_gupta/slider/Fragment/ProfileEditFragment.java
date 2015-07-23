package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.R;
import com.example.ravi_gupta.slider.TypefaceSpan;

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
        SpannableString s = new SpannableString("Edit Profile");
        s.setSpan(new TypefaceSpan(mainActivity, "gothic.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, s.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        android.support.v7.app.ActionBar actionBar = mainActivity.getSupportActionBar();
        actionBar.setTitle(s);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        Bundle bundle = getArguments();
        //ArrayList<String> infoUser = bundle.getStringArrayList("infoUser");
        Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/OpenSans-Regular.ttf");
        Typeface typeface3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Regular.ttf");

        customerName = (EditText) rootview.findViewById(R.id.fragment_profile_edit_edittext1);
        customerMail = (EditText) rootview.findViewById(R.id.fragment_profile_edit_edittext2);
        customerPhone = (EditText) rootview.findViewById(R.id.fragment_profile_edit_edittext3);
        saveButton = (Button) rootview.findViewById(R.id.fragment_profile_edit_button1);

        customerName.setTypeface(typeface2);
        customerMail.setTypeface(typeface2);
        customerPhone.setTypeface(typeface2);

        if(mainActivity.updateUserInfoProfileEditFragment == true) {
            customerName.setText(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("UPDATED_NAME", "defaultStringIfNothingFound"));
            customerMail.setText(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("UPDATED_MAIL", "defaultStringIfNothingFound"));
            customerPhone.setText( PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("UPDATED_PHONE", "defaultStringIfNothingFound"));
            Log.v("profile","Bye Bye "+customerName.getText());
        }


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedArrayList = new ArrayList<String>();
                mainActivity.updateUserInfoProfileEditFragment = true;

                updatedName = customerName.getText().toString();
                updatedMail = customerMail.getText().toString();
                updatedPhone = customerPhone.getText().toString();

                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("UPDATED_NAME", updatedName).commit();
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("UPDATED_MAIL", updatedMail).commit();
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("UPDATED_PHONE", updatedPhone).commit();

                updatedArrayList.add(0, updatedName);
                updatedArrayList.add(1,updatedMail);
                updatedArrayList.add(2, updatedPhone);

                Log.v("profile","Hello "+PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("UPDATED_NAME", "defaultStringIfNothingFound"));

                Intent intent = new Intent();
                intent.putExtra("updatedArrayList", updatedArrayList);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                getFragmentManager().popBackStack();

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
