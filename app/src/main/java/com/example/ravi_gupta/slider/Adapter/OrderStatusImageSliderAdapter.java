package com.example.ravi_gupta.slider.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ravi_gupta.slider.R;

/**
 * Created by Ravi-Gupta on 9/19/2015.
 */
public class OrderStatusImageSliderAdapter  extends PagerAdapter {

    int[] mResources;
    Context mContext;
    LayoutInflater mLayoutInflater;

    public OrderStatusImageSliderAdapter(Context context,int[] mResources) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mResources = mResources;
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.no_address_found_slider, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.no_address_found_slider_imageview1);
        imageView.setBackgroundResource(mResources[position]);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}

