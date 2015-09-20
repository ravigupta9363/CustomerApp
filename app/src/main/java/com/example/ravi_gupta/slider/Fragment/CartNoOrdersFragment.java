package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CartNoOrdersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartNoOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartNoOrdersFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String TAG = "CartNoOrdersFragment";
    MainActivity mainActivity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static CartNoOrdersFragment newInstance() {
        CartNoOrdersFragment fragment = new CartNoOrdersFragment();
        return fragment;
    }

    public CartNoOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //((ActionBarActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_cart_no_orders, container, false);

        Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/gothic.ttf");

        TextView text = (TextView)rootview.findViewById(R.id.fragment_cart_no_orders_textview1);
        TextView text2 = (TextView)rootview.findViewById(R.id.fragment_cart_no_orders_textview3);
        TextView toolbarTitle = (TextView)rootview.findViewById(R.id.fragment_cart_no_orders_textview2);
        ImageButton toolbarIcon = (ImageButton)rootview.findViewById(R.id.fragment_cart_no_orders_imagebutton1);
        //Button toolbarButton = (Button)rootview.findViewById(R.id.fragment_cart_no_orders_button1);


        toolbarTitle.setTypeface(typeface2);
        //toolbarButton.setTypeface(typeface);

        SpannableString ss = new SpannableString("What are you waiting for \n To add prescription click here or call us and enjoy the day!!");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                mainActivity.onBackPressed();
            }

            public void updateDrawState(TextPaint ds) {// override updateDrawState
                ds.setUnderlineText(false); // set to false to remove underline
            }
        };
        ss.setSpan(clickableSpan, 47, 57, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(20, 100, 159));

        //http://stackoverflow.com/questions/4897349/android-coloring-part-of-a-string-using-textview-settext
        //http://stackoverflow.com/questions/10696986/how-to-set-the-part-of-the-text-view-is-clickable
        // Set the text color for first 4 characters
        ss.setSpan(fcs, 47, 57, Spannable.SPAN_INCLUSIVE_INCLUSIVE);


        String string1 = "<p>No Prescription Yet?</p>";
        String string2 = "";
        text.setText((Html.fromHtml(string1)));
        text2.setText(ss);
        text2.setMovementMethod(LinkMovementMethod.getInstance());
        text.setTypeface(typeface1);
        text2.setTypeface(typeface1);

        toolbarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.onBackPressed();
                mainActivity.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
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
    public void onDestroy() {
        super.onDestroy();
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
