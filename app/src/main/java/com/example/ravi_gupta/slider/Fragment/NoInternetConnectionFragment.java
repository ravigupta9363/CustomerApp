package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NoInternetConnectionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NoInternetConnectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoInternetConnectionFragment extends android.support.v4.app.Fragment {

    MainActivity mainActivity;
    ProgressBar progressBar;

    public static String TAG = "NoInternetConnectionFragment";
    NoInternetConnectionFragment that;
    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static NoInternetConnectionFragment newInstance() {
        NoInternetConnectionFragment fragment = new NoInternetConnectionFragment();

        return fragment;
    }

    public NoInternetConnectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        that = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_no_internet_connection, container, false);

        TextView textView = (TextView)rootview.findViewById(R.id.fragment_no_internet_connection_textview1);
        Button retryButton = (Button)rootview.findViewById(R.id.fragment_no_internet_connection_button1);
        progressBar = (ProgressBar)rootview.findViewById(R.id.fragment_no_internet_connection_progressbar1);
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/OpenSans-Regular.ttf");

        textView.setTypeface(typeface2);
        retryButton.setTypeface(typeface2);
        progressBar.setVisibility(View.GONE);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //runActivityHelperInThread();
                //Running the main activity...
                mainActivity.getActivityHelper().startHelperActivity();
            }
        });

        return rootview;
    }

/*
    private void runActivityHelperInThread() {

        new AsyncTask<Void, Void, Void>()
        {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }
            @Override
            protected Void doInBackground(Void... params) {
                //this method will be running on background thread so don't update UI frome here
                //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
                mainActivity.getActivityHelper().startHelperActivity();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
            }

        }.execute(null, null, null);
    }*/




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
        mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        super.onResume();
    }
}
