package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ravi_gupta.slider.Database.ProfileDatabase;
import com.example.ravi_gupta.slider.Details.ProfileDetail;
import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.Models.Customer;
import com.example.ravi_gupta.slider.R;
import com.example.ravi_gupta.slider.Repository.CustomerRepository;
import com.strongloop.android.loopback.AccessToken;
import com.strongloop.android.loopback.UserRepository;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class IncomingSmsFragment extends android.support.v4.app.Fragment {

    public static String TAG = "drugcorner";



    private OnFragmentInteractionListener mListener;
    ImageButton titlebarBackButton;
    TextView titlebarTitle;
    Button nextButton;
    TextView detectingSmsText;
    TextView phoneNumber;
    TextView orText;
    TextView manuallyEntryText;
    public static EditText otpEdittext;
    Button resendCode;
    MainActivity mainActivity;
    IncomingSms incomingSms;
    public static ProgressBar progressBar;
    public static String OTP;
    private BroadcastReceiver receiver;
    ProfileDatabase profileDatabase;
    String fragment;
    int sum = 0;
    /*Getting the profile details*/
    private String number;
    private String email;
    private String name;


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
        profileDatabase = new ProfileDatabase(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_incoming_sms, container, false);
        Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface typeface3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Regular.ttf");
        fragment = getArguments().getString("fragment");
        ProfileDetail profileDetail = profileDatabase.getProfile();

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
        otpEdittext.setTypeface(typeface2);
        progressBar.setVisibility(View.VISIBLE);

        phoneNumber.setText(profileDetail.getPhone());

        /*Setting the profile details*/
        number = profileDetail.getPhone();
        email  = profileDetail.getEmail();
        name   = profileDetail.getName();

        //================Now sending the request to server for sending the otp=====================
        CustomerRepository customerRepo = new CustomerRepository();
        customerRepo.requestCode(number, new VoidCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e(TAG, t.toString());
            }

            @Override
            public void onSuccess() {
                Log.i(TAG, "OTP Request send to the server");
            }
        });



        otpEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sum = sum + count;
                if (manuallyEntryText.getText() != null && sum == 4) {
                    hiddenKeyboard(manuallyEntryText);
                    HashMap<String, String> credentials = new HashMap<String, String>();
                    credentials.put("number", number);
                    credentials.put("email", email);
                    credentials.put("name", name);

                    CustomerRepository customerRepo = new CustomerRepository();
                    //Now registering the customer with OTP verification code given..
                    customerRepo.registerWithOTP(credentials, (String)manuallyEntryText.getText(), new UserRepository.LoginCallback<Customer>(){
                        @Override
                        public void onSuccess(AccessToken token, Customer currentUser) {
                            //Registration done successfully.
                            if (fragment.equals("ProfileFragment")) {
                                mainActivity.onBackPressed();
                            } else if (fragment.equals("CartFragment")) {
                                mainActivity.replaceFragment(R.id.fragment_incoming_sms_button1, null);
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            //If OTP validation fails..

                        }
                    });


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(manuallyEntryText.getText() != null) {
                    if(fragment.equals("ProfileFragment")) {
                        mainActivity.onBackPressed();
                    }
                    else if(fragment.equals("CartFragment")) {
                        mainActivity.replaceFragment(R.id.fragment_incoming_sms_button1, null);
                    }

                }
                //validate data from server
            }
        });*/

        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Sent", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        titlebarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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

        new AsyncCaller().execute();
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


    private void hiddenKeyboard(View v) {
        InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }







    /*AsyncCaller class for sending the OTP request message to the server*/
    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        ProfileDetail profileDetail = profileDatabase.getProfile();
        String phoneNumber = profileDetail.getPhone();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread

        }

        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //this method will be running on UI thread
        }

    }

    public static class IncomingSms extends BroadcastReceiver {

        // Get the object of SmsManager
        final SmsManager sms = SmsManager.getDefault();
        public Matcher m;

        @Override
        public void onReceive(Context context, Intent intent) {

            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();

            try {

                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();

                        //Log.v("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                        Pattern p = Pattern.compile("\\b\\d{4}\\b");
                        m = p.matcher(message);
                        while (m.find()) {
                            //Log.v("SmsReceiver","Hello "+m.group());
                            progressBar.setVisibility(View.GONE);
                            OTP = m.group().toString();
                            otpEdittext.setText(OTP);

                        }
                        // Show Alert
                        //int duration = Toast.LENGTH_LONG;
                        //Toast toast = Toast.makeText(context,
                                //"senderNum: "+ senderNum + ", message: " + message, duration);
                        //toast.show();

                    } // end for loop
                } // bundle is null

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" +e);

            }
        }

    }

}
