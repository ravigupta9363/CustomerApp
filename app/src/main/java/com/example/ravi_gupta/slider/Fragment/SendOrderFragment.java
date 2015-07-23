package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ravi_gupta.slider.Database.DatabaseHelper;
import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.R;
import com.example.ravi_gupta.slider.TypefaceSpan;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SendOrderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SendOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendOrderFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MainActivity mainActivity;
    DatabaseHelper databaseHelper;
    public static String TAG = "SendOrderFragment";

    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static SendOrderFragment newInstance() {
        SendOrderFragment fragment = new SendOrderFragment();
        return fragment;
    }

    public SendOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
        SpannableString s = new SpannableString("Send Prescription");
        s.setSpan(new TypefaceSpan(mainActivity, "gothic.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, s.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        android.support.v7.app.ActionBar actionBar = mainActivity.getSupportActionBar();
        actionBar.setTitle(s);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_send_order, container, false);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface typeface3 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Lato-Regular.ttf");

        Button sendPrescriptionButton = (Button) rootview.findViewById(R.id.fragment_send_order_button1);
        Button repeatOrderButton = (Button) rootview.findViewById(R.id.fragment_send_order_button2);
        Button callUsButton = (Button) rootview.findViewById(R.id.fragment_send_order_button3);

        sendPrescriptionButton.setTypeface(typeface);
        repeatOrderButton.setTypeface(typeface);
        callUsButton.setTypeface(typeface);

        sendPrescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.replaceFragment(R.id.fragment_send_order_button1,null);
            }
        });

        repeatOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.replaceFragment(R.id.fragment_send_order_button2,null);
            }
        });

        callUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:91-9460109363"));
                startActivity(callIntent);
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
        Log.v("Pressed State", "Attached");
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.v("Pressed State", "Attached");
        if (R.id.home == item.getItemId()) {
            //getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        Log.v("Pressed State", "Attached");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SpannableString s = new SpannableString("Drug Corner");
        s.setSpan(new TypefaceSpan(mainActivity, "gothic.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, s.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        android.support.v7.app.ActionBar actionBar = mainActivity.getSupportActionBar();
        actionBar.setTitle(s);
    }
}
