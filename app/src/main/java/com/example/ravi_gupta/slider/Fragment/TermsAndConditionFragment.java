package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.Models.Constants;
import com.example.ravi_gupta.slider.Models.SystemInfo;
import com.example.ravi_gupta.slider.R;
import com.example.ravi_gupta.slider.Repository.SystemInfoRepository;
import com.strongloop.android.loopback.callbacks.ObjectCallback;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TermsAndConditionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TermsAndConditionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TermsAndConditionFragment extends android.support.v4.app.Fragment {
    MainActivity mainActivity;
    public static String TAG = "TermsAndConditionFragment";
    TextView termsAndCondition;
    private OnFragmentInteractionListener mListener;



    // TODO: Rename and change types and number of parameters
    public static TermsAndConditionFragment newInstance() {
        TermsAndConditionFragment fragment = new TermsAndConditionFragment();
        return fragment;
    }



    public TermsAndConditionFragment() {
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
        View rootview = inflater.inflate(R.layout.fragment_terms_and_condition, container, false);

        Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/gothic.ttf");

        TextView toolbarTitle = (TextView)rootview.findViewById(R.id.fragment_terms_and_conditions_textview4);
        ImageButton toolbarIcon = (ImageButton)rootview.findViewById(R.id.fragment_terms_and_conditions_imagebutton1);
        termsAndCondition = (TextView)rootview.findViewById(R.id.fragment_terms_and_conditions_textview1);
        toolbarTitle.setTypeface(typeface1);
        termsAndCondition.setMovementMethod(new ScrollingMovementMethod());

        loadTermsData();

        toolbarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
                mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        });

        return rootview;
    }





    /**
     * Method for loading the faq data
     */
    public void loadTermsData(){
        SystemInfoRepository systemInfoRepository = mainActivity.restAdapter.createRepository(SystemInfoRepository.class);
        systemInfoRepository.getInfo(Constants.termName, new ObjectCallback<SystemInfo>() {
            @Override
            public void onSuccess(SystemInfo systemInfo) {
                if(systemInfo == null){
                    Log.e(Constants.TAG, " TERMS AND COND. Data not present in the server.");
                }else{
                    String termsData = systemInfo.getHtml();
                    termsAndCondition.setText((Html.fromHtml(termsData)));
                }

            }

            @Override
            public void onError(Throwable t) {
                Log.e(Constants.TAG, "An Error  TERMS data from the server.");
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
