package com.example.ravi_gupta.slider.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ravi_gupta.slider.Details.NavigationDrawerItemDetails;
import com.example.ravi_gupta.slider.R;

import java.util.ArrayList;

/**
 * Created by Ravi-Gupta on 7/9/2015.
 */
public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavigationDrawerItemDetails> navigationDrawerItemDetailses;

    public NavDrawerListAdapter(Context context, ArrayList<NavigationDrawerItemDetails> navigationDrawerItemDetailses){
        this.context = context;
        this.navigationDrawerItemDetailses = navigationDrawerItemDetailses;
    }

    @Override
    public int getCount() {
        return navigationDrawerItemDetailses.size();
    }

    @Override
    public Object getItem(int position) {
        return navigationDrawerItemDetailses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }

        ImageView drawerIcon = (ImageView) convertView.findViewById(R.id.drawer_list_item_imageview1);
        TextView drawerText = (TextView) convertView.findViewById(R.id.drawer_list_item_textview1);
        Typeface typeFace=Typeface.createFromAsset(drawerText.getContext().getAssets(), "fonts/gothic.ttf");
        drawerText.setTypeface(typeFace);

        drawerIcon.setImageResource(navigationDrawerItemDetailses.get(position).getIcon());
        drawerText.setText(navigationDrawerItemDetailses.get(position).getTitle());

        return convertView;
    }
}
