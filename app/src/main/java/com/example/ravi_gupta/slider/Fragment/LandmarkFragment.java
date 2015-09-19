package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.MyApplication;
import com.example.ravi_gupta.slider.R;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LandmarkFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LandmarkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LandmarkFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView flatNumberTextView;
    TextView landmarkTextView;
    EditText flatNumberEditText;
    EditText landmarkEditText;
    Button placeOrder;
    MainActivity mainActivity;
    RadioGroup requestCallback;
    RadioButton callBack;
    RadioButton notCallBack;
    String flatNumber;
    String landmark;
    public static String TAG = "LandmarkFragment";
    boolean callCustomer = false;
    Map<String, Boolean> verifyOrder = new HashMap<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static LandmarkFragment newInstance() {
        LandmarkFragment fragment = new LandmarkFragment();
        return fragment;
    }

    public LandmarkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_landmark, container, false);

        Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/OpenSans-Regular.ttf");

        //flatNumberTextView = (TextView)rootview.findViewById(R.id.fragment_landmark_textview1);
        //landmarkTextView = (TextView)rootview.findViewById(R.id.fragment_landmark_textview2);
        final MyApplication application = (MyApplication)mainActivity.getApplication();
        flatNumberEditText = (EditText)rootview.findViewById(R.id.fragment_landmark_edittext1);
        landmarkEditText = (EditText)rootview.findViewById(R.id.fragment_landmark_edittext2);
        placeOrder = (Button) rootview.findViewById(R.id.fragment_landmark_button1);
        requestCallback = (RadioGroup)rootview.findViewById(R.id.fragment_landmark_radio_group);
        callBack = (RadioButton)rootview.findViewById(R.id.fragment_landmark_radio_button1);
        notCallBack = (RadioButton)rootview.findViewById(R.id.fragment_landmark_radio_button2);
        TextView toolbarTitle = (TextView)rootview.findViewById(R.id.fragment_landmark_textview3);
        ImageButton toolbarIcon = (ImageButton)rootview.findViewById(R.id.fragment_landmark_imagebutton1);

        callBack.setText("Send all medicines as prescribed");
        notCallBack.setText("Call me to confirm prescription");


        //Button toolbarButton = (Button)rootview.findViewById(R.id.fragment_cart_no_orders_button1);
        toolbarTitle.setTypeface(typeface1);
        //toolbarButton.setTypeface(typeface);

        toolbarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenKeyboard(flatNumberEditText);
                mainActivity.onBackPressed();
            }
        });

        requestCallback.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                if(id == R.id.fragment_landmark_radio_button1) {
                    callBack.setTextColor(Color.rgb(20, 100, 159));
                    notCallBack.setTextColor(Color.rgb(170, 170, 170));
                    callCustomer = false;
                }
                else if(id == R.id.fragment_landmark_radio_button2) {
                    notCallBack.setTextColor(Color.rgb(20, 100, 159));
                    callBack.setTextColor(Color.rgb(170, 170, 170));
                    callCustomer = true;
                }
            }
        });

        //flatNumberTextView.setTypeface(typeface2);
        //landmarkTextView.setTypeface(typeface2);
        flatNumberEditText.setTypeface(typeface2);
        landmarkEditText.setTypeface(typeface2);
        callBack.setTypeface(typeface2);
        notCallBack.setTypeface(typeface2);
        placeOrder.setTypeface(typeface2);

        final TextInputLayout FlatNumberLayout = (TextInputLayout) rootview.findViewById(R.id.fragment_landmark_layout1);
        final TextInputLayout LandmarkLayout = (TextInputLayout) rootview.findViewById(R.id.fragment_landmark_layout2);
        FlatNumberLayout.setErrorEnabled(true);
        //FlatNumberLayout.setError("This field is mandotary");
        LandmarkLayout.setErrorEnabled(true);

       // http://www.truiton.com/2015/06/android-floating-label-edittext/

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenKeyboard(flatNumberEditText);
                //mainActivity.replaceFragment(R.id.fragment_landmark_button1,null);
                flatNumber = flatNumberEditText.getText().toString();
                landmark = landmarkEditText.getText().toString();
                if (!isFlatNumberValid(flatNumber))
                    FlatNumberLayout.setError("Enter correct format");
                if (!isLandmarkValid(landmark))
                    LandmarkLayout.setError("Enter correct format");
                if (flatNumberEditText.getText().toString().matches(""))
                    FlatNumberLayout.setError("This field is mandatary");
                if (landmarkEditText.getText().toString().matches(""))
                    LandmarkLayout.setError("This field is mandatary");
                if (isFlatNumberValid(flatNumber) && isLandmarkValid(landmark)) {
                    mainActivity.replaceFragment(R.id.fragment_landmark_button1, null);
                }

                /**
                 * Adding data to ORDER Object
                 */
                application.getOrder(mainActivity).setLandmark(landmarkEditText.getText().toString());
                application.getOrder(mainActivity).setFlatNo(flatNumberEditText.getText().toString());
                verifyOrder.put("call",callCustomer);
                application.getOrder(mainActivity).setCallCustomer(verifyOrder);
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

    boolean isFlatNumberValid(String flatNumber) {
        boolean result = flatNumber.matches("^[a-zA-Z0-9\\s,'-/]+$");
        return result;
    }

    boolean isLandmarkValid(String landmark) {
        return landmark.matches("^[a-zA-Z0-9\\s,'-/]+$");
    }

    private void hiddenKeyboard(View v) {
        InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
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
                    hiddenKeyboard(flatNumberEditText);
                    // handle back button's click listener
                    mainActivity.onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item= menu.findItem(R.id.cart);
        item.setEnabled(false);
        item.setVisible(false);
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
