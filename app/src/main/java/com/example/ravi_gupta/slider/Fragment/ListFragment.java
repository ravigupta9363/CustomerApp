package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ravi_gupta.slider.Adapter.ShopListAdapter;
import com.example.ravi_gupta.slider.Details.ShopListDetails;
import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.Models.Constants;
import com.example.ravi_gupta.slider.Models.Office;
import com.example.ravi_gupta.slider.Models.Retailer;
import com.example.ravi_gupta.slider.MyApplication;
import com.example.ravi_gupta.slider.R;
import com.example.ravi_gupta.slider.Repository.RetailerRepository;
import com.strongloop.android.loopback.callbacks.ListCallback;
import com.strongloop.android.loopback.callbacks.ObjectCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends android.support.v4.app.Fragment {
    ListView mListview;
    ShopListAdapter shopListAdapter;
    ArrayList<ShopListDetails> shopListDetailses = new ArrayList<ShopListDetails>();
    MainActivity mainActivity;
    RetailerRepository retailerRepository;
    private OnFragmentInteractionListener mListener;
    private MyApplication application;
    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    public ListFragment() {
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
        View rootview = inflater.inflate(R.layout.fragment_shop_list, container, false);
        MyApplication myApplication = (MyApplication)mainActivity.getApplication();
        retailerRepository = myApplication.getLoopBackAdapter().createRepository(RetailerRepository.class);
        mListview = (ListView) rootview.findViewById(R.id.shopListview);
        setListViewHeightBasedOnChildren(mListview);
        mListview.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        shopListAdapter = new ShopListAdapter(getActivity(),R.layout.shop_list,shopListDetailses);
        mListview.setAdapter(shopListAdapter);
        application = (MyApplication)getActivity().getApplication();
        if(application.getOffice() == null){
            mainActivity.searchOffice(new ObjectCallback<Office>() {
                @Override
                public void onSuccess(Office object) {
                    //Now check office for closing..
                    checkOfficeClosed(object);
                    ListRetailers(object);
                }

                @Override
                public void onError(Throwable t) {
                    Log.e(Constants.TAG, t.toString());
                    Log.e(Constants.TAG, "Error loading office settings from server");
                    //closeLoadingBar();
                    //Show no internet connection..
                    mainActivity.replaceFragment(R.layout.fragment_try_again, null);

                }
            });
        }else{
            Office office =  application.getOffice();
            checkOfficeClosed(office);
            ListRetailers(office);
        }

        return rootview;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private void ListRetailers(final Office office){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<Retailer> retailers = application.getRetailerList();
                if(retailers == null){
                    //TODO ADD RETAILERS FROM SERVER FIRST
                    mainActivity.searchRetailers(office, new ListCallback<Retailer>() {
                        @Override
                        public void onSuccess(List<Retailer> retailers) {
                            showRetailersList(retailers);
                        }

                        @Override
                        public void onError(Throwable t) {
                            Log.e(Constants.TAG, "Error fetching retailers list from server.");
                        }
                    });
                }
                else{
                    showRetailersList(retailers);
                }

            }//public void run() {
        });
    }


    private void showRetailersList(List<Retailer> retailers){
        for (Retailer retailerModel : retailers) {
            Map<String, Object> discount = retailerModel.getDiscount();
            Object allitems = "allitems";
            try {
                shopListDetailses.add(new ShopListDetails(retailerModel.getId(), retailerModel.getName(), (double) ((Integer) discount.get(allitems)).intValue(), retailerModel.getArea(), retailerModel.isClosed(), retailerModel.getReturn(), retailerModel.getFulfillment()));
            } catch (ClassCastException c) {
                shopListDetailses.add(new ShopListDetails(retailerModel.getId(), retailerModel.getName(), (double) discount.get(allitems), retailerModel.getArea(), retailerModel.isClosed(), retailerModel.getReturn(), retailerModel.getFulfillment()));
            }

        }
        shopListAdapter.notifyDataSetChanged();
    }


    private void checkOfficeClosed(Office office){
        //TODO NEEDS TO BE CHECKED FOR FURTHER INSPECTION
        if(office.isClosed() == false) {
            mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ShopListDetails shopListDetails = (ShopListDetails) mListview.getItemAtPosition(position);

                    if (!shopListDetails.IsClosed) {
                        mainActivity.replaceFragment(R.id.shopListview, shopListDetails);
                        application.getOrder(mainActivity).setRetailerId((String)shopListDetails.id);
                    }
                }
            });
        }else{
            Toast.makeText(mainActivity, "Not serving in this particular hour", Toast.LENGTH_SHORT).show();
            mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(mainActivity, "Not serving in this particular hour", Toast.LENGTH_SHORT).show();
                }
            });

        }
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

}
