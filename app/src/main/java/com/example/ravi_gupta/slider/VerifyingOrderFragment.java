package com.example.ravi_gupta.slider;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.ravi_gupta.slider.Repository.NotificationRepository;
import com.strongloop.android.loopback.LocalInstallation;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VerifyingOrderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VerifyingOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerifyingOrderFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    MainActivity mainActivity;
    Button retryButton;
    ProgressBar progressBar;
    TextView textView;
    private NotificationRepository repository;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static String TAG = "VerifyingOrderFragment";

    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static VerifyingOrderFragment newInstance() {
        VerifyingOrderFragment fragment = new VerifyingOrderFragment();
        return fragment;
    }

    public VerifyingOrderFragment() {
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
        View rootview = inflater.inflate(R.layout.fragment_verifying_order, container, false);

        Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface typeface3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Regular.ttf");
        Typeface typeface4 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Allura-Regular.ttf");
        new AsyncCaller().execute();

        textView = (TextView) rootview.findViewById(R.id.fragment_verifying_order_textview1);
        retryButton = (Button) rootview.findViewById(R.id.fragment_verifying_order_button1);
        progressBar = (ProgressBar) rootview.findViewById(R.id.fragment_verifying_order_progressbar1);
        textView.setTypeface(typeface2);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
                progressBar.setVisibility(View.VISIBLE);
                textView.setText("Verifying your order");
                retryButton.setVisibility(View.GONE);
            }
        });
        //Requesting Verification code from server
        sendRequest();

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


    /*Method for sending verification request to the server*/
    public void sendRequest() {
        //================Now sending the request to server for sending the otp====================
        RestAdapter adapter = mainActivity.restAdapter;
        repository = adapter.createRepository(NotificationRepository.class);

        LocalInstallation installation = MainActivity.getInstallation();
        String id = (String)installation.getId();

        repository.requestCode(id, new VoidCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "Error sending OTP Push request to the server.");
                Log.e(TAG, t.toString());
            }

            @Override
            public void onSuccess() {
                Log.i(TAG, "OTP Request send to the server");
            }
        });

    }

    public boolean checkVerificationCode(int code) {
        return true;
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
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    return true;
                }
                return false;
            }
        });
    }




    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        private int code;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread

        }
        @Override
        protected Void doInBackground(Void... params) {
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            Date date       = new Date();
            long initialTime = date.getTime();
            int till = 10;
            //Loop till 5 sec is completed
            while (true ) {
                Date date2 = new Date();
                long curTime = date2.getTime();
                long diff = curTime - initialTime;
                //Getting the difference in seconds..
                diff = diff / 1000 % 60;
                Log.i("drugcorner",diff+"");
                if (diff > (long) till) {
                    break;
                }
                //Checking if the verification code is obtained..
                code = GcmIntentService.getVerificationCode();
                if (code != 0) {
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //this method will be running on UI thread
            if (code != 0) {
                if(checkVerificationCode(code)) {
                    mainActivity.replaceFragment(R.id.fragment_verifying_order_textview1, null);
                }
                Log.i("drugcorner", "Verification code found from fragment interface " + code);
                //add the code to the verification and follow the next step
            }else {
                //Timeout occurs retry the process..
                //If code isn't found then time out occurs..
                //Repeat the verification process in this case by showing a retry button..
                retryButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                textView.setText("Tap to Retry");
                //And recall this async class..
            }

        }

    }

}
