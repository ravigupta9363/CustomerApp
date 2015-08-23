package com.example.ravi_gupta.slider.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.ravi_gupta.slider.Adapter.ShopListAdapter;
import com.example.ravi_gupta.slider.Details.ShopListDetails;
import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.Models.Retailer;
import com.example.ravi_gupta.slider.R;
import com.example.ravi_gupta.slider.Repository.RetailerRepository;
import com.strongloop.android.loopback.callbacks.ListCallback;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    ListView mListview;
    ShopListAdapter shopListAdapter;
    ArrayList<ShopListDetails> shopListDetailses = new ArrayList<ShopListDetails>();
    MainActivity mainActivity;
    ProgressBar spinner;
    RetailerRepository retailerRepository;

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
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
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
        mainActivity = (MainActivity) getActivity();
        retailerRepository = mainActivity.restAdapter.createRepository(RetailerRepository.class);
        mListview = (ListView) rootview.findViewById(R.id.shopListview);
        spinner=(ProgressBar)rootview.findViewById(R.id.progressBar);
        new AsyncCaller().execute();

        shopListAdapter = new ShopListAdapter(getActivity(),R.layout.shop_list,shopListDetailses);
        mListview.setAdapter(shopListAdapter);

        retailerRepository.findAll(new ListCallback<Retailer>() {
            @Override
            public void onSuccess(List<Retailer> retailerModelList) {
                for(Retailer retailerModel : retailerModelList) {
                    Map<String, Integer> discount = retailerModel.getDiscount();
                    Object allitems = "allitems";
                    shopListDetailses.add(new ShopListDetails(retailerModel.getName(),discount.get(allitems) , retailerModel.getArea(), true, retailerModel.getReturn(), retailerModel.getFulfillment()));
                }
            }

            public void onError(Throwable t) {
                // handle the error
                Log.v("server","Error");
                Log.v("server",t+"");
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
    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spinner.setVisibility(View.VISIBLE);
            //this method will be running on UI thread
        }
        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here

            /*retailerRepository.findAll(new ListCallback<Retailer>() {

                @Override
                public void onSuccess(List<Retailer> objects) {
                    Log.v("server",objects+"");
                }

                public void onError(Throwable t) {
                    // handle the error
                }
            });
            */
            shopListDetailses.add(new ShopListDetails("Apollo Pharmacy",7,"P Block",true,true,99));
            shopListDetailses.add(new ShopListDetails("Gupta Pharmacy",5,"U Block",true,false,84));
            shopListDetailses.add(new ShopListDetails("Jindal Pharmacy",5,"Panchghami",true,true,45));
            shopListDetailses.add(new ShopListDetails("First Pharmacy", 3, "Sector 26", false, true, 33));


            mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Log.v("Listview1", "List View = " + mListview.getItemAtPosition(position) + "");
                    ShopListDetails shopListDetails =(ShopListDetails) mListview.getItemAtPosition(position);
                    if(shopListDetails.Isopen)
                     mainActivity.replaceFragment(R.id.shopListview, shopListDetails);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //this method will be running on UI thread
            spinner.setVisibility(View.GONE);
        }

    }

}
