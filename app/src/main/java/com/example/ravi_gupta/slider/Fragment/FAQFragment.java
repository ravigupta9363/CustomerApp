package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
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
 * {@link FAQFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FAQFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FAQFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView faq;
    MainActivity mainActivity;
    public static String TAG = "FAQFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FAQFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FAQFragment newInstance(String param1, String param2) {
        FAQFragment fragment = new FAQFragment();
        return fragment;
    }

    public FAQFragment() {
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
        View rootview = inflater.inflate(R.layout.fragment_faq, container, false);
        Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface typeface3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Regular.ttf");
        faq = (TextView)rootview.findViewById(R.id.fragment_faq_textview1);

        TextView toolbarTitle = (TextView)rootview.findViewById(R.id.fragment_faq_textview4);
        ImageButton toolbarIcon = (ImageButton)rootview.findViewById(R.id.fragment_faq_imagebutton1);
        toolbarTitle.setTypeface(typeface1);

        toolbarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
                mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            }
        });

        faq.setTypeface(typeface2);
        faq.setMovementMethod(new ScrollingMovementMethod());
        /**
         * Loading the faq data
         */
        loadFaqData();

        /*String faqData = "<p><b>Why is Myntra shutting down its website?</b><br />" +
                " Myntra has made this move with an eye towards the future," +
                " where mobiles will outgrow desktops both in terms of computing power and penetration." +
                " Myntra believes that switching to a mobile platform gives you the freedom to shop anytime," +
                " anywhere. It fits in perfectly with Myntra’s aim to create customer delight," +
                " as mobiles allow us to create a personalized experience to cater to your unique look good needs.</p>" +

                "<p><b>Why is Myntra also shutting down its m-site?</b><br />" +
                " Myntra shall provide a better shopping experience on the Mobile App than it could through any mobile browser." +
                " Myntra Mobile App allows Myntra to give you a high quality personalized experience across all supported platforms.</p>" +

                "<p><b>How can I withdraw money from my cashback account?</b><br />" +
                " Cashback will be credited to the payment source – debit/credit card from which the payment was" +
                " made for pre-paid orders or via NEFT in case of COD orders. For cashback accrued from COD orders," +
                " please write an email to support@myntra.com from your registered email address along with your bank account" +
                " details (Name of the bank, bank account number, bank customer name, IFSC code, account type - savings or current)" +
                " and we will transfer your cashback to your bank account.</p>" +

                "<p><b>I don’t have access to my registered email address. How can I withdraw money from my cashback account?</b><br />" +
                " Please call us at +91-80-43541999 and our customer care executives will help you." +
                " We will call back on your registered phone number with us to authenticate your identity before transferring your" +
                " cashback to your bank account. REFER TO DETAILED COUPON AND CASHBACK POLICY.</p>" +

                "<p><b>I was given a coupon when you cancelled my last order. How can I use it?</b><br />" +
                " To use your coupon, you will have to continue shopping with us on the app." +
                " In case, you don’t have access to the App currently, don’t worry, your coupons are safe with us." +
                " Whenever you download the app in future, you will be able to use the coupon (unless it has expired)." +
                " REFER TO DETAILED COUPON AND CASHBACK POLICY.</p>" +

                "<p><b>I have accumulated Myntra Points in my account. How can I redeem them?</b><br />" +
                " To redeem Myntra Points, you will have to continue shopping with us on the App." +
                " In case, you don’t have access to the App, don’t worry, your Myntra Points are safe with us," +
                ", whenever you download the App in future. REFER TO DETAILED COUPON AND CASHBACK POLICY.</p>";

        faq.setText((Html.fromHtml(faqData)));*/

        return rootview;
    }


    /**
     * Method for loading the faq data
     */
    public void loadFaqData(){
        SystemInfoRepository systemInfoRepository = mainActivity.restAdapter.createRepository(SystemInfoRepository.class);
        systemInfoRepository.getInfo(Constants.faqName, new ObjectCallback<SystemInfo>() {
            @Override
            public void onSuccess(SystemInfo systemInfo) {
                if(systemInfo == null){
                    Log.e(Constants.TAG, "FAQ data not present in the server.");
                }else{
                    String faqData = systemInfo.getHtml();
                    faq.setText((Html.fromHtml(faqData)));
                }
            }

            @Override
            public void onError(Throwable t) {
                Log.e(Constants.TAG, "An Error  FAQ data from the server.");
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
