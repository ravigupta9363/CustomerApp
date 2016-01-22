package com.example.ravi_gupta.slider.Adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.example.ravi_gupta.slider.Models.Constants;
import com.example.ravi_gupta.slider.Models.Order;
import com.example.ravi_gupta.slider.MyApplication;
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
        TextView cancelOrDelivered;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View row = convertView;
        mainActivity = (MainActivity) getContext();
        PastOrderHolder holder = null;

        Typeface typeface2 = Typeface.createFromAsset(context.getAssets(),"fonts/OpenSans-Regular.ttf");

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
            holder.cancelOrDelivered = (TextView)row.findViewById(R.id.past_order_layout_button1);
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
        holder.address.setText(pastOrdersDetail.address.substring(0,30)+"...");
        //If Delivered == true than

        final List<Map<String, Map>> mapList = pastOrdersDetail.drawable;
        Map<String, Map> imageThumbnail = mapList.get(0);
        Object thumb = "thumb";
        Uri imageUri = Uri.parse(Constants.apiUrl + "/containers/" +
                imageThumbnail.get(thumb).get("container") + "/download/" + imageThumbnail.get(thumb).get("name") );

        Log.d(Constants.TAG, imageUri.toString());

        Picasso.with(mainActivity).load(imageUri).resize(105, 130).into(holder.prescription);

        if(pastOrdersDetail.status.equals("5000")) {
            holder.cancelOrDelivered.setText("New Order");
            holder.cancelOrDelivered.setTextColor(Color.parseColor("#36B666"));
        }
        else if(pastOrdersDetail.status.equals("5001")) {
            holder.cancelOrDelivered.setText("Preparing Order");
            holder.cancelOrDelivered.setTextColor(Color.parseColor("#36B666"));
        }
        else if(pastOrdersDetail.status.equals("5002")) {
            holder.cancelOrDelivered.setText("At Retailer");
            holder.cancelOrDelivered.setTextColor(Color.parseColor("#36B666"));
        }
        else if(pastOrdersDetail.status.equals("5003")) {
            holder.cancelOrDelivered.setText("Delivered");
            holder.cancelOrDelivered.setTextColor(Color.parseColor("#36B666"));
        }
        else if(pastOrdersDetail.status.equals("5004")) {
            holder.cancelOrDelivered.setText("Cancelled");
            holder.cancelOrDelivered.setTextColor(Color.parseColor("#FF0000"));
        }
        else{
            /**
             * Show error
             */
            Log.e(Constants.TAG, "In PastOrderAdapter Error status not recognised");
            holder.cancelOrDelivered.setVisibility(View.GONE);
        }

        holder.reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.replaceFragment(R.id.past_order_layout_button2, null);//Open OTP if data is entered
                //Create an order when reorder button is pressed..
                /**
                 * Create an order
                 * Status code 5000 for new order
                 */
                createOrder(mainActivity, pastOrdersDetail.drawable, pastOrdersDetail.retailerId, "5000");
            }
        });


        holder.prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("server","Big View");
                mainActivity.replaceFragment(R.id.past_order_layout_imageview1, mapList);//Open Dialog
                //Toast.makeText(getContext(),"Image Dialog will appear",Toast.LENGTH_SHORT).show();
            }
        });

        return row;
    }

    /**
     * Create order when reorder button is pressed
     * @param activity
     * @param prescription
     * @param retailerId
     * @param prototypeStatusCode
     */
    private void createOrder(
            MainActivity activity,
            List<Map<String, Map>> prescription,
            String retailerId,
            String prototypeStatusCode
    ){
        //Now add this order to the application order making it the current order..
        MyApplication myApplication =  (MyApplication)activity.getApplication();
        Order order = myApplication.getOrder(mainActivity);
        order.setPrescription(prescription);
        order.setRetailerId(retailerId);
        order.setPrototypeStatusCode(prototypeStatusCode);
    }
}
