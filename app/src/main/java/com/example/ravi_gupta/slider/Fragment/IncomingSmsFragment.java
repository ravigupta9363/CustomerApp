package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ravi_gupta.slider.IncomingSms;
import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.R;


public class IncomingSmsFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String TAG = "IncomingSmsFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ImageButton titlebarBackButton;
    TextView titlebarTitle;
    Button nextButton;
    TextView detectingSmsText;
    TextView phoneNumber;
    TextView orText;
    TextView manuallyEntryText;
    EditText otpEdittext;
    Button resendCode;
    MainActivity mainActivity;
    IncomingSms incomingSms;
    ProgressBar progressBar;



    // TODO: Rename and change types and number of parameters
    public static IncomingSmsFragment newInstance() {
        IncomingSmsFragment fragment = new IncomingSmsFragment();
        return fragment;
    }

    public IncomingSmsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //incomingSms = new IncomingSms();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_incoming_sms, container, false);
        Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/OpenSans-Regular.ttf");
        Typeface typeface3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Regular.ttf");

        titlebarBackButton = (ImageButton)rootview.findViewById(R.id.fragment_incoming_sms_imagebutton1);
        titlebarTitle = (TextView)rootview.findViewById(R.id.fragment_incoming_sms_textview1);
        nextButton = (Button)rootview.findViewById(R.id.fragment_incoming_sms_button1);
        detectingSmsText = (TextView)rootview.findViewById(R.id.fragment_incoming_sms_textview2);
        phoneNumber = (TextView)rootview.findViewById(R.id.fragment_incoming_sms_textview3);
        orText = (TextView)rootview.findViewById(R.id.fragment_incoming_sms_textview4);
        manuallyEntryText = (TextView)rootview.findViewById(R.id.fragment_incoming_sms_textview5);
        otpEdittext = (EditText)rootview.findViewById(R.id.fragment_incoming_sms_edittext1);
        resendCode = (Button)rootview.findViewById(R.id.fragment_incoming_sms_button2);
        progressBar = (ProgressBar) rootview.findViewById(R.id.fragment_incoming_sms_loading_indicator);

        titlebarTitle.setTypeface(typeface1);
        nextButton.setTypeface(typeface2);
        detectingSmsText.setTypeface(typeface2);
        phoneNumber.setTypeface(typeface2);
        orText.setTypeface(typeface2);
        manuallyEntryText.setTypeface(typeface2);
        resendCode.setTypeface(typeface2);
        progressBar.setVisibility(View.VISIBLE);

       /* while (incomingSms.m.find()) {
            Log.v("SmsReceiver", "Hello Ji" + incomingSms.m.group());
            manuallyEntryText.setText(incomingSms.m.group());
        }*/

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.replaceFragment(R.id.fragment_incoming_sms_button1, null);
            }
        });

        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Sent", Toast.LENGTH_SHORT).show();
            }
        });

        titlebarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
            }
        });

        //incomingSms.onReceive(getActivity(),null);


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
