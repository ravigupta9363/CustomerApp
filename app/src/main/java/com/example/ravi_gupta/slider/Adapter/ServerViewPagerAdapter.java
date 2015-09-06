package com.example.ravi_gupta.slider.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.Models.Constants;
import com.example.ravi_gupta.slider.R;
import com.example.ravi_gupta.slider.TouchImageView;
import com.example.ravi_gupta.slider.ViewPager.ViewPagerCustomDuration;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/**
 * Created by Ravi-Gupta on 8/18/2015.
 */
public class ServerViewPagerAdapter extends PagerAdapter {
    Context context;
    List<Map<String, String>> mapList;
    LayoutInflater layoutInflater;
    ViewPagerCustomDuration viewPager;
    boolean enabled;
    int page = 0;
    MainActivity mainActivity;

    public ServerViewPagerAdapter(Context context, final List<Map<String, String>> mapList, final ViewPagerCustomDuration viewPager) {
        this.context = context;
        this.mapList = mapList;
        this.viewPager = viewPager;
        enabled = true;

       /* viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // not needed
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });*/



    }


    @Override
    public int getCount() {
        //return sliderItems.length;
        Log.v("server","Size = "+mapList.size()+"");
        return mapList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == ((RelativeLayout)o);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.server_viewpager_item, container, false);
        mainActivity = (MainActivity) context;

        // Locate the ImageView in viewpager_item.xml
        TouchImageView sliderItem = (TouchImageView)itemView.findViewById(R.id.server_viewpager_item);

        // Capture position and set to the ImageView
        Map<String, String> imageThumbnail = mapList.get(position);
        Object bigImage = "image";
        Uri imageUri = Uri.parse(Constants.apiUrl + imageThumbnail.get(bigImage));
        Log.v("server","BigImage = "+imageUri+"");
        Picasso.with(mainActivity).load(imageUri).into(sliderItem);
       // sliderItem.setImageResource(sliderItems[position]);

        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);

        //http://stackoverflow.com/questions/21368693/how-to-do-circular-scrolling-on-viewpager
        ((ViewPager) container).addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // skip fake page (first), go to last page
               /* if (position == 0) {
                    ((ViewPager) container).setCurrentItem(sliderItems.length-2);
                }

                // skip fake page (last), go to first page
                if (position == sliderItems.length-1) {
                    ((ViewPager) container).setCurrentItem(1); //notice how this jumps to position 1, and not position 0. Position 0 is the fake page!
                }*/

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
