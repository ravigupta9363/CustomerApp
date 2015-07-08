package com.example.ravi_gupta.slider.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ravi_gupta.slider.R;
import com.example.ravi_gupta.slider.Details.ShopListDetails;

import java.util.ArrayList;

/**
 * Created by Ravi-Gupta on 7/2/2015.
 */
public class ShopListAdapter extends ArrayAdapter<ShopListDetails> {

    ArrayList<ShopListDetails> shopListDetailses  = new ArrayList<ShopListDetails>();
    Context context;
    int resource;

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
        Typeface typeface3 = Typeface.createFromAsset(context.getAssets(),"fonts/Lato-Regular.ttf");
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

            row.setTag(holder);
        }
        else {

            holder = (ShopListHolder)row.getTag();
        }
        //Assigning custom fonts

        holder.shopName.setTypeface(typeface);
        holder.discount.setTypeface(typeface2);
        holder.address.setTypeface(typeface2);
        holder.isReturn.setTypeface(typeface2);
        holder.deliveryTime.setTypeface(typeface2);
        holder.orderFulfilment.setTypeface(typeface2);


        ShopListDetails shopListDetails = shopListDetailses.get(position);
        holder.shopName.setText(shopListDetails.shopName);
        holder.discount.setText(String.valueOf("- "+(shopListDetails.discount)+"% off"));
        holder.address.setText(shopListDetails.address);
        if(shopListDetails.Isopen && shopListDetails.Isreturn){
            holder.isReturn.setText("Return");
            holder.isReturn.setTextColor(Color.parseColor("#36B666"));
        }
        else if(shopListDetails.Isopen && !shopListDetails.Isreturn) {
            holder.isReturn.setText(" No Return");
           // holder.isReturn.setTextColor(Color.parseColor("#36B666"));
        }
        else {
            holder.isReturn.setText("Closed");
            holder.isReturn.setTextColor(Color.RED);
        }
        holder.deliveryTime.setText(String.valueOf(shopListDetails.deliveryTime) + " min");
        holder.orderFulfilment.setText(String.valueOf("Order fulfilment "+shopListDetails.orderFulfilment)+"%");

        return row;
    }


    static class ShopListHolder {
        TextView shopName;
        TextView discount;
        TextView address;
        TextView isReturn;
        TextView deliveryTime;
        TextView orderFulfilment;
    }
}
