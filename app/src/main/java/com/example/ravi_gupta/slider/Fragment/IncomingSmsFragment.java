package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
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
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.UserRepository;
import com.strongloop.android.loopback.callbacks.VoidCallback;

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
    TextView validationFailed;
    public static EditText otpEdittext;
    Button resendCode;
    MainActivity mainActivity;
    IncomingSms incomingSms;
    public static String OTP;
    private BroadcastReceiver receiver;
    ProfileDatabase profileDatabase;
    ProfileEditFragment profileEditFragment;
    String fragment;
    int sum = 0;
    /*Getting the profile details*/
    private String number;
    private String email;
    private String name;
    private CustomerRepository repository;
    //boolean errorOccured = false;



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
      //  profileEditFragment = (ProfileEditFragment)getActivity().getSupportFragmentManager().findFragmentByTag(ProfileEditFragment.TAG);
        Log.v("Data",profileEditFragment+"");

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
        final ProfileDetail profileDetail = profileDatabase.getProfile();

        titlebarBackButton = (ImageButton)rootview.findViewById(R.id.fragment_incoming_sms_imagebutton1);
        titlebarTitle = (TextView)rootview.findViewById(R.id.fragment_incoming_sms_textview1);
        nextButton = (Button)rootview.findViewById(R.id.fragment_incoming_sms_button1);
        detectingSmsText = (TextView)rootview.findViewById(R.id.fragment_incoming_sms_textview2);
        phoneNumber = (TextView)rootview.findViewById(R.id.fragment_incoming_sms_textview3);
        orText = (TextView)rootview.findViewById(R.id.fragment_incoming_sms_textview4);
        manuallyEntryText = (TextView)rootview.findViewById(R.id.fragment_incoming_sms_textview5);
        validationFailed =  (TextView)rootview.findViewById(R.id.fragment_incoming_sms_textview6);
        otpEdittext = (EditText)rootview.findViewById(R.id.fragment_incoming_sms_edittext1);
        resendCode = (Button)rootview.findViewById(R.id.fragment_incoming_sms_button2);

        titlebarTitle.setTypeface(typeface1);
        nextButton.setTypeface(typeface2);
        detectingSmsText.setTypeface(typeface2);
        phoneNumber.setTypeface(typeface2);
        orText.setTypeface(typeface2);
        manuallyEntryText.setTypeface(typeface2);
        resendCode.setTypeface(typeface2);
        otpEdittext.setTypeface(typeface2);


        /**
         * Loading the loading bar
         */
        mainActivity.getActivityHelper().launchRingDialog(mainActivity);

        phoneNumber.setText("+91 " + mainActivity.tempPhone);

        /*Setting the profile details*/
        number = mainActivity.tempPhone;
        email  = mainActivity.tempEmail;
        name   = mainActivity.tempName;
        //Log.v("Data",profileEditFragment.getTempEmail());

        //Request code..
        requestCodeOTP();


        addChangeListener();


        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCodeOTP();
                /**
                 * Loading the loading bar
                 */
                mainActivity.getActivityHelper().launchRingDialog(mainActivity);

                resendCode.setVisibility(View.GONE);
                validationFailed.setVisibility(View.GONE);
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




    /**
     * Now sending the request to server for sending the otp
     */
    public void requestCodeOTP(){
        otpEdittext.setText("");
        repository = mainActivity.getCustomerRepo();
        //CustomerRepository customerRepo = new CustomerRepository();
        repository.requestCode(mainActivity.tempPhone, new VoidCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e(TAG, t.toString());
                Log.e(TAG, "ERROR REQUESTING VERIFICATION CODE TO THE SERVER");
                //Toast.makeText(getActivity(), "ERROR REQUESTING", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess()
            {
                Log.i(TAG, "OTP Request send to the server");


                //Toast.makeText(getActivity(), "REQUEST SEND", Toast.LENGTH_SHORT).show();
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

    public TextWatcher getTextWatcher() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int codeLength = start + count;

                String code = otpEdittext.getText().toString().trim();
                if (codeLength == 4) {
                    hiddenKeyboard(otpEdittext);

                    //Now registering the customer with OTP verification code given..
                    repository.OtpLogin(number, email, name, code, new UserRepository.LoginCallback<Customer>() {
                        @Override
                        public void onSuccess(AccessToken token, Customer currentUser) {

                            mainActivity.registerInstallation(currentUser);
                            /**
                             * Close the loading bar
                             */
                            mainActivity.getActivityHelper().closeLoadingBar();

                            //errorOccured = false;
                            //Registration done successfully.
                            if (fragment.equals("ProfileFragment") || fragment.equals("DirectHomeFragment") || fragment.equals("addProfile")) {
                                mainActivity.onBackPressed();
                            } else if (fragment.equals("CartFragment") || fragment.equals("PastOrderFragment")) {
                                mainActivity.replaceFragment(R.id.fragment_incoming_sms_button1, null);
                            }
                        }

                        @Override
                        public void onError(Throwable t) {

                            //If OTP validation fails..
                            //Email exists or internet connection not avail..
                            Log.d(TAG, "Error occured in File IncomingSmsFragment");
                            Log.d(TAG, t.getMessage());
                            /**
                             * Close the loading bar
                             */
                            mainActivity.getActivityHelper().closeLoadingBar();

                            if (t.getMessage().equals("Unprocessable Entity")) {
                                resendCode.setVisibility(View.GONE);
                                mainActivity.replaceFragment(R.id.fragment_incoming_sms_textview4, fragment);
                            } else if (t.getMessage().equals("Unauthorized")) {
                                //errorOccured = true;

                                resendCode.setVisibility(View.VISIBLE);
                                validationFailed.setVisibility(View.VISIBLE);
                            } else {

                                resendCode.setVisibility(View.VISIBLE);
                            }

                        }
                    });
                }
            }//onTextChanged


            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        return textWatcher;
    }

    public void addChangeListener() {
        otpEdittext.addTextChangedListener(getTextWatcher());
    }

    public void removeChangeListener() {
        otpEdittext.removeTextChangedListener(getTextWatcher());
        otpEdittext.setText("");
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
