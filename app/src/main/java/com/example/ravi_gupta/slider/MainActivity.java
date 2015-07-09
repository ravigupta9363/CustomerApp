package com.example.ravi_gupta.slider;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.ravi_gupta.slider.Adapter.NavDrawerListAdapter;
import com.example.ravi_gupta.slider.Details.AddressDetails;
import com.example.ravi_gupta.slider.Details.NavigationDrawerItemDetails;
import com.example.ravi_gupta.slider.Fragment.ListFragment;
import com.example.ravi_gupta.slider.Fragment.MainFragment;
import com.example.ravi_gupta.slider.Fragment.SendOrderFragment;
import com.example.ravi_gupta.slider.Fragment.changeLocationFragment;
import com.example.ravi_gupta.slider.Interface.OnFragmentChange;
import com.example.ravi_gupta.slider.Location.GeoSearchResult;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements ListFragment.OnFragmentInteractionListener, OnFragmentChange,
        SendOrderFragment.OnFragmentInteractionListener, changeLocationFragment.OnFragmentInteractionListener,
        MainFragment.OnFragmentInteractionListener{

    public int updateLocation = 0;
    public boolean addedToList = false;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavigationDrawerItemDetails> navDrawerItems;
    private NavDrawerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragment(R.layout.fragment_main,null);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDrawerLayout = (DrawerLayout) inflater.inflate(R.layout.decor, null); // "null" is important.

        // HACK: "steal" the first child of decor view
        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
        View child = decor.getChildAt(0);
        decor.removeView(child);
        LinearLayout container = (LinearLayout) mDrawerLayout.findViewById(R.id.mContainer); // This is the container we defined just now.
        container.addView(child);

        // Make the drawer replace the first child
        decor.addView(mDrawerLayout);
        //android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        //TextView toolbarText = (TextView)findViewById(R.id.toolbarText);

        //Typeface typeFace=Typeface.createFromAsset(toolbar.getContext().getAssets(), "fonts/gothic.ttf");
        //toolbarText.setTypeface(typeFace);


        getSupportActionBar().setElevation(0);

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.sections_title);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.sections_icons);

        //mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawerListView);

        navDrawerItems = new ArrayList<NavigationDrawerItemDetails>();

        navDrawerItems.add(new NavigationDrawerItemDetails(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavigationDrawerItemDetails(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavigationDrawerItemDetails(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavigationDrawerItemDetails(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        navDrawerItems.add(new NavigationDrawerItemDetails(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new NavigationDrawerItemDetails(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));

        // Recycle the typed array
        navMenuIcons.recycle();



        //Typeface typeFace=Typeface.createFromAsset(getAssets(), "fonts/gothic.ttf");

        SpannableString s = new SpannableString("Drug Corner");
        //

        s.setSpan(new TypefaceSpan(this, "gothic.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, s.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

// Update the action bar title with the TypefaceSpan instance
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(s);
        mTitle = mDrawerTitle = s.toString();


        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setTitle(Html.fromHtml("<font color='#999999'>DrugCorner </font>"));

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.mipmap.dc_menu, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
               // getSupportActionBar().setTitle(mDrawerTitle);
                //getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
              //  getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            //displayView(0);
        }

    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                //fragment = new HomeFragment();
                //Toast.makeText(this,"I am Clicked",Toast.LENGTH_SHORT);
                Log.v("Navigation","Yes");
                break;
            case 1:
                //fragment = new FindPeopleFragment();
                break;
            case 2:
                //fragment = new PhotosFragment();
                break;
            case 3:
                //fragment = new CommunityFragment();
                break;
            case 4:
                //fragment = new PagesFragment();
                break;
            case 5:
                //fragment = new WhatsHotFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            //fragmentManager.beginTransaction()
              //      .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
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
