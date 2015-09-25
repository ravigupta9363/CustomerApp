package com.example.ravi_gupta.slider.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ravi_gupta.slider.ActivityHelper;
import com.example.ravi_gupta.slider.Details.ShopListDetails;
import com.example.ravi_gupta.slider.Models.Constants;
import com.example.ravi_gupta.slider.R;

import java.util.ArrayList;

/**
 * Created by Ravi-Gupta on 7/2/2015.
 */
public class ShopListAdapter extends ArrayAdapter<ShopListDetails> {

    ArrayList<ShopListDetails> shopListDetailses  = new ArrayList<ShopListDetails>();
    Context context;
    int resource;
    int counter = 0;

    public ShopListAdapter(Context context, int resource, ArrayList<ShopListDetails> shopListDetailses) {
        super(context, resource,shopListDetailses);
        this.context = context;
        this.resource = resource;
        this.shopListDetailses = shopListDetailses;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View row = convertView;
        ShopListHolder holder = null;
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(context.getAssets(),"fonts/OpenSans-Regular.ttf");
        Typeface boldTypeface = Typeface.create(typeface2, Typeface.BOLD);
        if(row == null)
        {

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            row = layoutInflater.inflate(resource,parent,false);
            holder = new ShopListHolder();
            holder.shopName = (TextView)row.findViewById(R.id.shop_list_textview1);
            holder.discount = (TextView)row.findViewById(R.id.shop_list_textview2);
            holder.address = (TextView)row.findViewById(R.id.shop_list_textview3);
            holder.isReturn = (TextView)row.findViewById(R.id.shop_list_textview5);
            holder.deliveryTime = (TextView)row.findViewById(R.id.shop_list_textview4);
            holder.orderFulfilment = (TextView) row.findViewById(R.id.shop_list_textview6);
            holder.icon = (TextView) row.findViewById(R.id.shop_list_textview7);

            row.setTag(holder);
        }
        else {

            holder = (ShopListHolder)row.getTag();
        }
        //Assigning custom fonts

        holder.shopName.setTypeface(typeface2);
        holder.discount.setTypeface(boldTypeface);
        holder.address.setTypeface(typeface2);
        holder.isReturn.setTypeface(typeface2);
        holder.deliveryTime.setTypeface(typeface2);
        holder.orderFulfilment.setTypeface(typeface2);


        ShopListDetails shopListDetails = shopListDetailses.get(position);
        String firstCharacter = String.valueOf(shopListDetails.shopName.charAt(0));
        String shopName = ActivityHelper.toCamelCase(shopListDetails.shopName);
        String shopAddress = ActivityHelper.toCamelCase(shopListDetails.address);

        holder.shopName.setText(shopName);
        Double d = new Double(shopListDetails.discount);
        int discountNumber = d.intValue();
        if(discountNumber == 0) {
            holder.discount.setVisibility(View.GONE);
        }else{
            holder.discount.setVisibility(View.VISIBLE);
            holder.discount.setText("upto "+String.valueOf((discountNumber)+"% off"));
        }

        holder.address.setText(shopAddress);


        if(!shopListDetails.IsClosed && shopListDetails.Isreturn){
            holder.isReturn.setText(Constants.returnDays);
            holder.isReturn.setTextColor(Color.parseColor("#AAAAAA"));
        }
        else if(!shopListDetails.IsClosed && !shopListDetails.Isreturn) {
            holder.isReturn.setText("No Return");
            holder.isReturn.setTextColor(Color.parseColor("#AAAAAA"));
            // holder.isReturn.setTextColor(Color.parseColor("#36B666"));
        }
        else {
            holder.isReturn.setText("Closed");
            holder.isReturn.setTextColor(Color.RED);
        }
        holder.deliveryTime.setText(String.valueOf(shopListDetails.deliveryTime) + " min");
        if(shopListDetails.orderFulfilment == 0) {
            holder.orderFulfilment.setVisibility(View.GONE);
        }
        else {
            holder.orderFulfilment.setText(String.valueOf("Order fulfilment " + shopListDetails.orderFulfilment) + "%");
        }
        //holder.icon.setImageDrawable(shopListDetails.icon);
        holder.icon.setText(firstCharacter);
        switch (counter){
            case 0:
                holder.icon.setBackgroundResource(R.drawable.icon_color_one);
                counter = 1;
                break;
            case 1:
                holder.icon.setBackgroundResource(R.drawable.icon_color_two);
                counter = 2;
                break;
            case 2:
                holder.icon.setBackgroundResource(R.drawable.icon_color_three);
                counter = 3;
                break;
            case 3:
                holder.icon.setBackgroundResource(R.drawable.icon_color_four);
                counter = 0;
                break;

        }



        return row;
    }

    static class ShopListHolder {
        TextView shopName;
        TextView discount;
        TextView address;
        TextView isReturn;
        TextView deliveryTime;
        TextView orderFulfilment;
        TextView icon;
    }
}
