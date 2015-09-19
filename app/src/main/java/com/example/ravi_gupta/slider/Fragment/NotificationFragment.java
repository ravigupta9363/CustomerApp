package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ravi_gupta.slider.Adapter.MyRecyclerViewAdapter;
import com.example.ravi_gupta.slider.Database.NotificationDatabase;
import com.example.ravi_gupta.slider.Details.NotificationItemDetail;
import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.R;
import com.example.ravi_gupta.slider.SwipeableRecyclerViewTouchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";
    MainActivity mainActivity;
    public static String TAG = "NotificationFragment";
    ArrayList notificationItemDetails;
    NotificationDatabase notificationDatabase;

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
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        return fragment;
    }

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationDatabase = new NotificationDatabase(mainActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_notification, container, false);

        Typeface typeface1 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/OpenSans-Regular.ttf");
        Typeface typeface3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lato-Regular.ttf");
        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.fragment_notification_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);

        if(notificationDatabase.getNotificationCount() == 0) {
            mRecyclerView.setVisibility(View.GONE);
        }
        else {
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(mRecyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    notificationItemDetails.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                    notificationDatabase.deleteNotification(position+1);
                                    Log.v("Notification",position+"");
                                }
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    notificationItemDetails.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                    notificationDatabase.deleteNotification(position+1);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });

        mRecyclerView.addOnItemTouchListener(swipeTouchListener);//https://github.com/brnunes/SwipeableRecyclerView/blob/master/lib/src/main/java/com/github/brnunes/swipeablerecyclerview/SwipeableRecyclerViewTouchListener.java

        TextView toolbarTitle = (TextView)rootview.findViewById(R.id.fragment_notification_textview4);
        ImageButton toolbarIcon = (ImageButton)rootview.findViewById(R.id.fragment_notification_imagebutton1);
        TextView noNotificationText = (TextView)rootview.findViewById(R.id.fragment_notification_textview1);
        ImageView noNotification = (ImageView)rootview.findViewById(R.id.fragment_notification_imageview1);
        toolbarTitle.setTypeface(typeface1);
        noNotificationText.setTypeface(typeface2);

        noNotification.setVisibility(View.VISIBLE);
        noNotificationText.setVisibility(View.VISIBLE);

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

    private ArrayList<NotificationItemDetail> getDataSet() {
        notificationItemDetails = new ArrayList<NotificationItemDetail>();
        //notificationItemDetails.add(new NotificationItemDetail(0,"Huge Discount On Medicines !!","Now you can get the medicines from your local store at very low cost, If medicines are not available at one shop it will made available to you from other retailer, There is no restriction on the amount of order"));
        //notificationItemDetails.add(new NotificationItemDetail(1,"Huge Discount On Medicines !!","Now you can get the medicines from your local store at very low cost, If medicines are not available at one shop it will made available to you from other retailer, There is no restriction on the amount of order"));
        //notificationItemDetails.add(new NotificationItemDetail(2,"Huge Discount On Medicines !!","Now you can get the medicines from your local store at very low cost, If medicines are not available at one shop it will made available to you from other retailer, There is no restriction on the amount of order"));
        //notificationItemDetails.add(new NotificationItemDetail(3,"Huge Discount On Medicines !!","Now you can get the medicines from your local store at very low cost, If medicines are not available at one shop it will made available to you from other retailer, There is no restriction on the amount of order"));
        //notificationItemDetails.add(new NotificationItemDetail(4,"Huge Discount On Medicines !!", "Now you can get the medicines from your local store at very low cost, If medicines are not available at one shop it will made available to you from other retailer, There is no restriction on the amount of order"));
        //notificationDatabase.addNotification(new NotificationItemDetail(mainActivity.notificationId, "Now you can get the medicines from your local store at very low cost, If medicines are not available at one shop it will made available to you from other retailer, There is no restriction on the amount of order"));
        List<NotificationItemDetail> notificationItemDetailList = notificationDatabase.getAllNotifications();
        for (NotificationItemDetail notificationItemDetail : notificationItemDetailList) {
            notificationItemDetails.add(new NotificationItemDetail(notificationItemDetail.getId(),notificationItemDetail.getNotificationDetail()));
        }
        return notificationItemDetails;
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
