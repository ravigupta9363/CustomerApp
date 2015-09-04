package com.example.ravi_gupta.slider;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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


import com.example.ravi_gupta.slider.Database.DatabaseHelper;
import com.example.ravi_gupta.slider.Details.PrescriptionDetail;
import com.example.ravi_gupta.slider.Models.Constants;
import com.example.ravi_gupta.slider.Models.Office;
import com.example.ravi_gupta.slider.Repository.NotificationRepository;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.strongloop.android.loopback.Container;
import com.strongloop.android.loopback.ContainerRepository;
import com.strongloop.android.loopback.File;
import com.strongloop.android.loopback.LocalInstallation;
import com.strongloop.android.loopback.RestAdapter;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.loopback.callbacks.ObjectCallback;
import com.strongloop.android.loopback.callbacks.VoidCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VerifyingOrderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VerifyingOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerifyingOrderFragment extends android.support.v4.app.Fragment {

    MainActivity mainActivity;
    Button retryButton;
    ProgressBar progressBar;
    TextView textView;
    private NotificationRepository repository;
    private DatabaseHelper databaseHelper;
    Bitmap bitmap;
    byte[] byteArray;
    List<String> fileList = new ArrayList<>();
    int totalImageUploaded = 0;
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
        databaseHelper = new DatabaseHelper(mainActivity);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_verifying_order, container, false);
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");

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


    private void uploadPrescription(MainActivity activity, final DatabaseHelper databaseHelper, final int code){
        final List<byte[]> byteArrayList = new ArrayList<>();
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        new  AsyncTask<Void, Void, Void>()
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //this method will be running on UI thread
                progressBar.setVisibility(View.VISIBLE);
                textView.setText("Uploading Prescription..");
            }


            @Override
            protected Void doInBackground(Void... params) {
                List<PrescriptionDetail> prescriptionDetails =  databaseHelper.getAllPrescription();
                for(PrescriptionDetail prescriptionDetail : prescriptionDetails){
                    try {
                        bitmap = BitmapFactory.decodeStream(mainActivity.getContentResolver().openInputStream(prescriptionDetail.getImageUri()));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byteArray = stream.toByteArray();
                    } catch (FileNotFoundException e) {
                        Log.e(Constants.TAG, e.getMessage());
                        e.printStackTrace();
                    }
                    byteArrayList.add(byteArray);


                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                uploadToServer(mainActivity.restAdapter, code, byteArrayList);
                progressBar.setVisibility(View.GONE);
            }

        }.execute(null, null, null);
    }





    private void uploadToServer(RestAdapter adapter, final int code, final List<byte[]> byteArrayList){
        MyApplication app =  (MyApplication)mainActivity.getApplication() ;
        final Office office = app.getOffice();

        ContainerRepository containerRepo = adapter.createRepository(ContainerRepository.class);
        containerRepo.get(Constants.imageContainer, new ObjectCallback<Container>() {
            @Override
            public void onSuccess(Container container) {
                final int listSize = byteArrayList.size();
                for(byte[] bytes : byteArrayList){
                    String fileName = String.valueOf(code) + office.getId().toString();
                    container.upload(fileName, bytes, "image/jpeg",
                            new ObjectCallback<File>() {
                                @Override
                                public void onSuccess(File remoteFile) {
                                    // Update GUI - add remoteFile to the list of documents
                                    fileList.add(remoteFile.getName());
                                    totalImageUploaded++;
                                    if(totalImageUploaded == listSize){
                                        /**
                                         * Call upload order now to the server..
                                         */

                                        Log.d(Constants.TAG, "Successfully images to the server");



                                    }
                                }//onSuccess

                                @Override
                                public void onError(Throwable error) {
                                    // upload failed
                                    Log.e(Constants.TAG, "Error uploading images to the server.");
                                }
                            }
                    );
                }
            }

            @Override
            public void onError(Throwable t) {
                Log.e(Constants.TAG, "Error: Container not found");
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
               // Log.i("drugcorner",diff+"");
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
                   // mainActivity.replaceFragment(R.id.fragment_verifying_order_textview1, null);
                    uploadPrescription(mainActivity, databaseHelper, code);
                }
                Log.d("drugcorner", "Verification code found from fragment interface " + code);
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
    }//AsyncCaller



}
