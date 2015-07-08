package com.example.ravi_gupta.slider;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ravi_gupta.slider.Details.AddressDetails;
import com.example.ravi_gupta.slider.Fragment.ListFragment;
import com.example.ravi_gupta.slider.Fragment.MainFragment;
import com.example.ravi_gupta.slider.Fragment.SendOrderFragment;
import com.example.ravi_gupta.slider.Fragment.changeLocationFragment;
import com.example.ravi_gupta.slider.Interface.OnFragmentChange;
import com.example.ravi_gupta.slider.Location.GeoSearchResult;


public class MainActivity extends AppCompatActivity implements ListFragment.OnFragmentInteractionListener, OnFragmentChange,
        SendOrderFragment.OnFragmentInteractionListener, changeLocationFragment.OnFragmentInteractionListener,
        MainFragment.OnFragmentInteractionListener{

    public int updateLocation = 0;
    public boolean addedToList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragment(R.layout.fragment_main,null);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void replaceFragment(int id, Object object) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (id) {

            case R.layout.fragment_main :
                MainFragment frag6 = (MainFragment) getSupportFragmentManager().
                        findFragmentByTag(MainFragment.TAG);
                if (frag6 == null) {
                    frag6 = MainFragment.newInstance();
                }
                ft.replace(R.id.container, frag6, MainFragment.TAG);
                ft.commitAllowingStateLoss();
                break;

            case R.id.shopListview :
                Fragment newFragment = new SendOrderFragment();
                ft.replace(R.id.ListFragment, newFragment);
                ft.addToBackStack(null); // Ads FirstFragment to the back-stack
                ft.commit();
                break;

            case R.id.fragment_main_edittext1 :
                changeLocationFragment frag4 = (changeLocationFragment) getSupportFragmentManager().
                        findFragmentByTag(changeLocationFragment.TAG);
                if (frag4 == null) {
                    frag4 = changeLocationFragment.newInstance();
                }
                ft.replace(R.id.fragment_main_container, frag4, changeLocationFragment.TAG).addToBackStack(null);
                Log.v("Result", "Called2");
                ft.commitAllowingStateLoss();
                break;

            case R.id.fragment_change_location_button2 :
                MainFragment frag5 = (MainFragment) getSupportFragmentManager().
                        findFragmentByTag(MainFragment.TAG);
                updateLocation = 3;
                if (frag5 == null) {
                    frag5 = MainFragment.newInstance();
                }
                ft.replace(R.id.container, frag5, MainFragment.TAG).addToBackStack(null);
                ft.commitAllowingStateLoss();
                break;

            case R.id.fragment_change_location_edittext:
                GeoSearchResult result = (GeoSearchResult)object;
               // Log.v("Result", "is equals to " + result.getAddress());
                Bundle bundle = new Bundle();
                bundle.putString("newAddress", result.getAddress().toString());
                updateLocation = 1;
                Fragment newFragment4 = new MainFragment();
                ft.replace(R.id.container, newFragment4);
                newFragment4.setArguments(bundle);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.addressListView :
                AddressDetails addressDetails = (AddressDetails)object;
                //
                Bundle bundle2 = new Bundle();
                bundle2.putString("newAddress1", addressDetails.address1.toString());
                bundle2.putString("newAddress2", addressDetails.address2.toString());
                Log.v("Result", "is equals to " + addressDetails.address1.toString());
                updateLocation = 2;
                Fragment newFragment5 = new MainFragment();
                ft.replace(R.id.container, newFragment5);
                newFragment5.setArguments(bundle2);
                ft.addToBackStack(null);
                ft.commit();
                break;
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        new AsyncCaller().execute();

    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //this method will be running on UI thread

            pdLoading.dismiss();
        }

    }


}
