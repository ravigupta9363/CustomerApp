package com.example.ravi_gupta.slider.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ravi_gupta.slider.Details.OrderStatusDetail;
import com.example.ravi_gupta.slider.R;

import java.util.ArrayList;

/**
 * Created by Ravi-Gupta on 7/12/2015.
 */
public class OrderStatusDetailAdapter extends ArrayAdapter<OrderStatusDetail> {

    ArrayList<OrderStatusDetail> orderStatusDetailses  = new ArrayList<OrderStatusDetail>();
    Context context;
    int resource;

    public OrderStatusDetailAdapter(Context context, int resource,ArrayList<OrderStatusDetail> orderStatusDetails) {
        super(context, resource, orderStatusDetails);
        this.context = context;
        this.resource = resource;
        this.orderStatusDetailses = orderStatusDetails;

    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View row = convertView;
        OrderStatusHolder holder = null;
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/gothic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(context.getAssets(),"fonts/OpenSans-Regular.ttf");
        Typeface typeface3 = Typeface.createFromAsset(context.getAssets(),"fonts/Lato-Regular.ttf");
        if(row == null)
        {

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            row = layoutInflater.inflate(resource,parent,false);
            holder = new OrderStatusHolder();
            holder.medicineName = (TextView)row.findViewById(R.id.order_status_medicine_list_textview1);
            holder.quantity = (TextView)row.findViewById(R.id.order_status_medicine_list_textview2);
            holder.price = (TextView)row.findViewById(R.id.order_status_medicine_list_textview3);
            holder.total = (TextView)row.findViewById(R.id.order_status_medicine_list_textview4);
            holder.companyName = (TextView)row.findViewById(R.id.order_status_medicine_list_textview5);

            holder.medicineName.setTypeface(typeface2);
            holder.quantity.setTypeface(typeface2);
            holder.price.setTypeface(typeface2);
            holder.total.setTypeface(typeface2);
            holder.companyName.setTypeface(typeface2);

            row.setTag(holder);
        }
        else {

            holder = (OrderStatusHolder)row.getTag();
        }
        //Assigning custom fonts

        holder.medicineName.setTypeface(typeface2);
        holder.quantity.setTypeface(typeface2);
        holder.price.setTypeface(typeface2);
        holder.total.setTypeface(typeface2);
        holder.companyName.setTypeface(typeface2);

        OrderStatusDetail orderStatusDetail2 = orderStatusDetailses.get(position);
        holder.medicineName.setText(orderStatusDetail2.medicineName);
        holder.quantity.setText(String.valueOf((orderStatusDetail2.medicineQuantity)+" x"));
        holder.price.setText(String.valueOf((orderStatusDetail2.medicinePrice)+""));
        holder.total.setText(String.valueOf(orderStatusDetail2.totalPrice) + " /-");
        holder.companyName.setText(orderStatusDetail2.companyName);


        return row;
    }


    static class OrderStatusHolder {
        TextView medicineName;
        TextView quantity;
        TextView price;
        TextView total;
        TextView companyName;
    }
}
