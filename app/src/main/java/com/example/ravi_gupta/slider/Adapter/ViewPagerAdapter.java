package com.example.ravi_gupta.slider.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.ravi_gupta.slider.R;
import com.example.ravi_gupta.slider.ViewPager.ViewPagerCustomDuration;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ravi-Gupta on 6/30/2015.
 */
public class ViewPagerAdapter extends PagerAdapter {
    Context context;
    List<RequestCreator> sliderItems;
    LayoutInflater layoutInflater;
    ViewPagerCustomDuration viewPager;
    boolean enabled;
    //List<java.io.File> imageFileArray;
    ArrayList<Drawable> imageFileArray;
    private boolean doNotifyDataSetChangedOnce = false;

    public ViewPagerAdapter(Context context, ArrayList<Drawable> imageFileArray
            , final ViewPagerCustomDuration viewPager) {
        this.context = context;
        this.imageFileArray = imageFileArray;
        this.viewPager = viewPager;
       // requestCreatorListIterator = sliderItems.listIterator();
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
        return imageFileArray.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o == view;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.viewpager_item, container, false);
        /**
         * Loading the image view
         */
        // Locate the ImageView in viewpager_item.xml
        ImageView sliderItem = (ImageView) itemView.findViewById(R.id.viewpagerImageView1);

        // Capture position and set to the ImageView

        Drawable localImage = imageFileArray.get(position);
        sliderItem.setImageDrawable(localImage);
        Log.v("myAdd",localImage+"");


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
                    if (position == 0) {
                        ((ViewPager) container).setCurrentItem(imageFileArray.size() - 2);
                    }

                    // skip fake page (last), go to first page
                    if (position == imageFileArray.size() - 1) {
                        ((ViewPager) container).setCurrentItem(1); //notice how this jumps to position 1, and not position 0. Position 0 is the fake page!
                    }

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
