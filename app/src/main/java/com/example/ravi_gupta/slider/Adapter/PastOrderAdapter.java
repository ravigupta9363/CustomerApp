package com.example.ravi_gupta.slider.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ravi_gupta.slider.Details.PastOrdersDetail;
import com.example.ravi_gupta.slider.MainActivity;
import com.example.ravi_gupta.slider.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ravi-Gupta on 7/13/2015.
 */
public class PastOrderAdapter extends ArrayAdapter<PastOrdersDetail>{

    Context context;
    int resource;
    ArrayList<PastOrdersDetail> pastOrdersDetails = new ArrayList<PastOrdersDetail>();
    MainActivity mainActivity;

    public PastOrderAdapter(Context context, int resource, ArrayList<PastOrdersDetail> pastOrdersDetails) {
        super(context, resource,pastOrdersDetails);
        this.context = context;
        this.resource = resource;
        this.pastOrdersDetails = pastOrdersDetails;
    }

    static class PastOrderHolder {
        ImageView prescription;
        TextView orderId;
        TextView date;
        TextView time;
        //TextView price;
        TextView address;
        Button reorder;
        Button cancel;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View row = convertView;
        mainActivity = (MainActivity) getContext();
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
            //holder.price = (TextView)row.findViewById(R.id.past_order_layout_textview4);
            holder.address = (TextView)row.findViewById(R.id.past_order_layout_textview5);
            holder.reorder = (Button)row.findViewById(R.id.past_order_layout_button2);
            holder.cancel = (Button)row.findViewById(R.id.past_order_layout_button1);
            holder.prescription = (ImageView)row.findViewById(R.id.past_order_layout_imageview1);

            row.setTag(holder);
        }
        else {

            holder = (PastOrderHolder)row.getTag();
        }
        //Assigning custom fonts

        holder.orderId.setTypeface(typeface2);
        holder.date.setTypeface(typeface2);
        holder.time.setTypeface(typeface2);
        //holder.price.setTypeface(typeface2);
        holder.address.setTypeface(typeface2);
        //holder.status.setTypeface(typeface2);

        final PastOrdersDetail pastOrdersDetail = pastOrdersDetails.get(position);
        holder.date.setText(pastOrdersDetail.date);
        holder.time.setText(pastOrdersDetail.time);
        holder.orderId.setText(pastOrdersDetail.orderId);
        //holder.price.setText(String.valueOf(pastOrdersDetail.price) + "/-");
        holder.address.setText(pastOrdersDetail.address);
        final List<Map<String, String>> mapList = pastOrdersDetail.drawable;
        Map<String, String> imageThumbnail = mapList.get(0);
        Object thumb = "thumb";
        Uri imageUri = Uri.parse(mainActivity.baseURL + imageThumbnail.get(thumb));
        Picasso.with(mainActivity).load(imageUri).into(holder.prescription);

        if(pastOrdersDetail.isDelivered)
            holder.cancel.setVisibility(View.GONE);

        holder.reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.replaceFragment(R.id.past_order_layout_button2, null);//Open OTP if data is entered
            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.replaceFragment(R.id.past_order_layout_button1,null);//Open Status
            }
        });

        holder.prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("server","Big View");
                mainActivity.replaceFragment(R.id.past_order_layout_imageview1,mapList);//Open Dialog
                //Toast.makeText(getContext(),"Image Dialog will appear",Toast.LENGTH_SHORT).show();
            }
        });

        return row;
    }

}
