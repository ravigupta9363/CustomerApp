package com.example.ravi_gupta.slider.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ravi_gupta.slider.Details.PastOrdersDetail;
import com.example.ravi_gupta.slider.R;

import java.util.ArrayList;

/**
 * Created by Ravi-Gupta on 7/13/2015.
 */
public class PastOrderAdapter extends ArrayAdapter<PastOrdersDetail>{

    Context context;
    int resource;
    ArrayList<PastOrdersDetail> pastOrdersDetails = new ArrayList<PastOrdersDetail>();

    public PastOrderAdapter(Context context, int resource, ArrayList<PastOrdersDetail> pastOrdersDetails) {
        super(context, resource,pastOrdersDetails);
        this.context = context;
        this.resource = resource;
        this.pastOrdersDetails = pastOrdersDetails;
    }

    static class PastOrderHolder {
        TextView orderId;
        TextView date;
        TextView time;
        TextView price;
        TextView address;
        TextView status;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View row = convertView;
        PastOrderHolder holder = null;
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(context.getAssets(),"fonts/OpenSans-Regular.ttf");
        Typeface typeface3 = Typeface.createFromAsset(context.getAssets(),"fonts/Lato-Regular.ttf");
        if(row == null)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            row = layoutInflater.inflate(resource,parent,false);
            holder = new PastOrderHolder();
            holder.date = (TextView)row.findViewById(R.id.past_order_layout_textview1);
            holder.time = (TextView)row.findViewById(R.id.past_order_layout_textview2);
            holder.orderId = (TextView)row.findViewById(R.id.past_order_layout_textview3);
            holder.price = (TextView)row.findViewById(R.id.past_order_layout_textview4);
            holder.address = (TextView)row.findViewById(R.id.past_order_layout_textview5);
            holder.status = (TextView)row.findViewById(R.id.past_order_layout_textview6);
            row.setTag(holder);
        }
        else {

            holder = (PastOrderHolder)row.getTag();
        }
        //Assigning custom fonts

        holder.orderId.setTypeface(typeface2);
        holder.date.setTypeface(typeface2);
        holder.time.setTypeface(typeface2);
        holder.price.setTypeface(typeface2);
        holder.address.setTypeface(typeface2);
        holder.status.setTypeface(typeface2);

        PastOrdersDetail pastOrdersDetail = pastOrdersDetails.get(position);
        holder.date.setText(pastOrdersDetail.date);
        holder.time.setText(pastOrdersDetail.time);
        holder.orderId.setText(pastOrdersDetail.orderId);
        holder.price.setText(String.valueOf(pastOrdersDetail.price)+"/-");
        holder.address.setText(pastOrdersDetail.address);
        holder.status.setText(pastOrdersDetail.status);

        return row;
    }

}
